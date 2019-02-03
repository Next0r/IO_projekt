package pl.polsl.io.controller.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;
import pl.polsl.io.model.AssistanceRequest;
import pl.polsl.io.model.Client;
import pl.polsl.io.model.ClientCar;
import pl.polsl.io.model.Product;
import pl.polsl.io.model.ProductType;
import pl.polsl.io.model.SingleService;
import pl.polsl.io.model.UserAccount;
import pl.polsl.io.service.DatabaseService;
import pl.polsl.io.service.InputDataService;

/**
 *
 * @author Michal
 */
public class LoadRequest extends HttpServlet {

    /**
     * EntityManagerFactory injection field, used for creating EntityManager
     * objects.
     */
    @PersistenceUnit
    private EntityManagerFactory emf;
    /**
     * UserTransaction injection field, used for managing transactions when
     * interacting with database.
     */
    @Resource
    private UserTransaction utx;

    private DatabaseService databaseService;
    private InputDataService inputDataService;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        databaseService = (DatabaseService) request.getSession().getAttribute("databaseService");
        inputDataService = (InputDataService) request.getSession().getAttribute("inputDataService");

        UserAccount acc = null;
        Client cln = null;
        ArrayList<SingleService> sServices;
        
        String option = request.getParameter("submit");

        // try to load client data
        try {
            acc = (UserAccount) databaseService.getUserAccountEntity((String) request.getSession().getAttribute("currentUser"), "", emf);
            cln = databaseService.getClientEntityByAccount(acc, emf);
        } catch (Exception e) {
            // db exception
            inputDataService.generateErrorResultMessage();
            inputDataService.setResultMessageAttribute(null, request);
        }
        
        if (acc == null || cln == null) {
            request.getRequestDispatcher("/RequestAssistancePage.jsp").forward(request, response);
        } else if (option == null || !option.equals("submit")) {   
            // Assistance request form was not filled.
            displayClientData(request, acc, cln);
            displayServices(request);
            displayClientCars(request, cln);
            request.getRequestDispatcher("/RequestAssistancePage.jsp").forward(request, response);
        } else {
           
            String licenseNumber = inputDataService.nullStringTrim(request.getParameter("selectedCar"));
            ClientCar car;
            List<Product> availableProducts;
            try {
                car = databaseService.getClientCarByLicenseNumber(licenseNumber, emf);
                availableProducts = databaseService.getProductsByClientCar(car, emf);
            } catch (Exception e) {
                // db exception
                inputDataService.generateErrorResultMessage();
                inputDataService.setResultMessageAttribute(null, request);
                request.getRequestDispatcher("/RequestAssistancePage.jsp").forward(request, response);
                return;
            }
            
            String description = inputDataService.nullStringTrim(request.getParameter("description"));
            ArrayList<SingleService> selectedServices = getSelectedServices(request);

            if ((selectedServices == null || selectedServices.isEmpty()) && (description == null || description.isEmpty())) {
                inputDataService.setResultMessageAttribute("Select at least one service or insert description.", request);
                request.getRequestDispatcher("/RequestAssistancePage.jsp").forward(request, response);
                return;
            }
              
            List<Product> neededProducts = getNeededProducts(availableProducts, selectedServices);
            if (neededProducts == null) {
                inputDataService.setResultMessageAttribute("You don't own the required products to make this request. Please check your account and buy neccessary products.", request);
                request.getRequestDispatcher("/RequestAssistancePage.jsp").forward(request, response);
                return;
            }
            
            //Create assistance request.
            try {
                addAssistanceRequest(car,neededProducts,description);
            } catch (Exception e) {
                inputDataService.generateErrorResultMessage();
                inputDataService.setResultMessageAttribute(null, request);
                request.getRequestDispatcher("/RequestAssistancePage.jsp").forward(request, response);
                return;
            }

            inputDataService.setResultMessageAttribute("Thank you for your request. Our best specialists will contact with you soon.", request);
            request.getRequestDispatcher("/RequestAssistancePage.jsp").forward(request, response);
        }
    }
    
    private void displayClientData(HttpServletRequest request, UserAccount acc, Client cln){
                    if (acc != null && cln != null) {
                // fetching user paramters to webpage
                if (cln.getName().equals("_")) {
                    request.getSession().setAttribute("clientName", null);
                } else {
                    request.getSession().setAttribute("clientName", cln.getName());
                }
                if (cln.getSurname().equals("_")) {
                    request.getSession().setAttribute("clientSurname", null);
                } else {
                    request.getSession().setAttribute("clientSurname", cln.getSurname());
                }
                request.getSession().setAttribute("clientPhone", cln.getPhoneNumber());
            }
    }
    
    private void displayServices(HttpServletRequest request) {
            try {
                ArrayList<SingleService> sServices = new ArrayList<>(databaseService.getSingleServices(emf));
                SingleService unncessarySingleService = null;
                for (SingleService s : sServices) {
                    if (s.getName().equals("Request assistance")) {
                        unncessarySingleService = s;
                    }
                }
                sServices.remove(unncessarySingleService);
                request.getSession().setAttribute("services", sServices);
            } catch (Exception e) {
                //db exception
                inputDataService.generateErrorResultMessage();
                inputDataService.setResultMessageAttribute(null, request);
            }
    }
    
    private void displayClientCars(HttpServletRequest request, Client cln){
        ArrayList<ClientCar> clientCars;
        try {
            clientCars = new ArrayList<>(databaseService.getClientCarsByClient(cln, emf));
        } catch (Exception e) {
            // db exception
            inputDataService.generateErrorResultMessage();
            inputDataService.setResultMessageAttribute(null, request);
            return;
        }
        // request car selection display
        request.getSession().setAttribute("clientCars", clientCars);
    }
    
    private List<Product> getNeededProducts(List<Product> availableProducts, ArrayList<SingleService> selectedServices) {
        
        List<Product> neededProducts = new ArrayList<>();
        for (ProductType selectedService : selectedServices) {
            boolean productExists = false;
            for (Product p : availableProducts) {
                if (p.getProductType().getName().equals(selectedService.getName())){
                    productExists = true;
                    neededProducts.add(p);
                    break;
                } else if (p.getProductType().getName().contains("Package")) {
                    pl.polsl.io.model.Package pck = (pl.polsl.io.model.Package) p.getProductType();
                    for (SingleService s : pck.getSingleServices()) {
                        if (s.getName().equals(selectedService.getName())) {
                            productExists = true;
                            neededProducts.add(p);
                            break;
                        }
                    }
                }
            }
            if (!productExists){
                return null;
            }
        }
                
        for (Product p : availableProducts) {
            if (p.getProductType().getName().contains("Request assistance") || p.getProductType().getName().contains("Package")) {
                return neededProducts;
            }
        }
        return null;
    }
    
    private ArrayList<SingleService> getSelectedServices(HttpServletRequest request){
        ArrayList<SingleService> sServices = null;
        ArrayList<SingleService> selectedServices = new ArrayList<>();
        try {
            sServices = new ArrayList<>(databaseService.getSingleServices(emf));
        } catch (Exception e) {
            inputDataService.generateErrorResultMessage();
            inputDataService.setResultMessageAttribute(null, request);
            return null;
        }
            // load selected services
            for (SingleService service : sServices) {
                if (request.getParameter(service.getProductTypeID().toString()) != null) {
                    selectedServices.add(service);
                }
            }
        return selectedServices;
    }
    
    private void addAssistanceRequest(ClientCar car, List<Product> neededProducts, String description) throws Exception {
        AssistanceRequest assistanceRequest = new AssistanceRequest(car, neededProducts, description);
        databaseService.addEntities(new Object[]{assistanceRequest}, emf, utx);
    }
   

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
