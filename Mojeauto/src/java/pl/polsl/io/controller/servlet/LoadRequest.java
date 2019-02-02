package pl.polsl.io.controller.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
import pl.polsl.io.model.Payment;
import pl.polsl.io.model.Product;
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
            try {
                car = databaseService.getClientCarByLicenseNumber(licenseNumber, emf);
            } catch (Exception e) {
                // db exception
                inputDataService.generateErrorResultMessage();
                inputDataService.setResultMessageAttribute(null, request);
                request.getRequestDispatcher("/RequestAssistancePage.jsp").forward(request, response);
                return;
            }
            
            try {
                sServices = new ArrayList<>(databaseService.getSingleServices(emf));
            } catch (Exception e) {
                inputDataService.generateErrorResultMessage();
                inputDataService.setResultMessageAttribute(null, request);
                request.getRequestDispatcher("/RequestAssistancePage.jsp").forward(request, response);
                return;
            }
            String description = inputDataService.nullStringTrim(request.getParameter("description"));
            ArrayList<SingleService> selectedServices = new ArrayList<>();
            // load selected services
            for (SingleService service : sServices) {
                if (request.getParameter(service.getProductTypeID().toString()) != null) {
                    selectedServices.add(service);
                }
            }
            if (selectedServices.isEmpty() && (description == null || description.isEmpty())) {
                inputDataService.setResultMessageAttribute("Select at least one service or insert description.", request);
                request.getRequestDispatcher("/RequestAssistancePage.jsp").forward(request, response);
                return;
            }

            ArrayList<Product> products = new ArrayList<>();
            ArrayList<Payment> payments = new ArrayList<>();
            ArrayList<AssistanceRequest> assistanceRq = new ArrayList<>();

            // if services were selected create products based on them
            for (SingleService service : selectedServices) {
                Payment payment = new Payment(service.getPrice(), "", false, new Date());
                products.add(new Product(null, null, service, payment, car));
                payments.add(payment);
            }

            assistanceRq.add(new AssistanceRequest(cln, products, description));
            Stream stream = Stream.concat(products.stream(), payments.stream());
            stream = Stream.concat(stream, assistanceRq.stream());

            // put new payments and products (single services) with assistance request to db
            try {
                databaseService.addEntities(stream.toArray(), emf, utx);
            } catch (Exception e) {
                // db exception
                inputDataService.generateErrorResultMessage();
                inputDataService.setResultMessageAttribute(null, request);
                request.getRequestDispatcher("/RequestAssistancePage.jsp").forward(request, response);
                return;
            }
            inputDataService.setResultMessageAttribute("Thank you for your request. Our best specialists will contact with you soon.", request);
            request.getRequestDispatcher("/RequestAssistancePage.jsp").forward(request, response);
        }
    }
    
    void displayClientData(HttpServletRequest request, UserAccount acc, Client cln){
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
    
    void displayServices(HttpServletRequest request) {
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
    
    void displayClientCars(HttpServletRequest request, Client cln){
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
