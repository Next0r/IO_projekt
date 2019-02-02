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

        // try to load client data
        try {
            acc = (UserAccount) databaseService.getUserAccountEntity((String) request.getSession().getAttribute("currentUser"), "", emf);
            cln = databaseService.getClientEntityByAccount(acc, emf);
        } catch (Exception e) {
            // db exception
            inputDataService.generateErrorResultMessage();
            inputDataService.setResultMessageAttribute(null, request);
        }

        String option = request.getParameter("submit");
        // check if user just submitted his request
        if (option != null) {
            String name = inputDataService.nullStringTrim(request.getParameter("name"));
            String surname = inputDataService.nullStringTrim(request.getParameter("surname"));
            String phoneNumber = inputDataService.nullStringTrim(request.getParameter("phone"));
            if (!inputDataService.isCorrectClientData(name, surname, phoneNumber)) {
                inputDataService.setResultMessageAttribute(null, request);
                request.getRequestDispatcher("/RequestAssistancePage.jsp").forward(request, response);
                return;
            }
            String brand = inputDataService.nullStringTrim(request.getParameter("brand"));
            String model = inputDataService.nullStringTrim(request.getParameter("model"));
            String licenseNumber = inputDataService.nullStringTrim(request.getParameter("lNumber"));
            String productionYear = inputDataService.nullStringTrim(request.getParameter("pYear"));
            Boolean isCorrectVehicleData = false;
            try {
                isCorrectVehicleData = inputDataService.isCorrectVehicleData(brand, model, licenseNumber, emf, false);
            } catch (Exception e) {
                // db exception
                inputDataService.generateErrorResultMessage();
                inputDataService.setResultMessageAttribute(null, request);
            }
            if (!isCorrectVehicleData) {
                inputDataService.setResultMessageAttribute(null, request);
                request.getRequestDispatcher("/RequestAssistancePage.jsp").forward(request, response);
                return;
            }
            try {
                sServices = new ArrayList<SingleService>(databaseService.getSingleServices(emf));
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
            // if there is no such client create new one
            if (cln == null) {
                cln = new Client(name, surname, null);
                cln.setPhoneNumber(phoneNumber);
                try {
                    databaseService.addEntities(new Object[]{cln}, emf, utx);
                } catch (Exception e) {
                    // db exception
                    inputDataService.generateErrorResultMessage();
                    inputDataService.setResultMessageAttribute(null, request);
                    request.getRequestDispatcher("/RequestAssistancePage.jsp").forward(request, response);
                    return;
                }
            } else if (cln.getPhoneNumber() == null || cln.getPhoneNumber().isEmpty()) {
                // if client exists but has not set phone number
                try {
                    databaseService.updateClientParameter("phoneNumber", phoneNumber, cln.getClientID(), emf, utx);
                } catch (Exception e) {
                    // db exception
                    inputDataService.generateErrorResultMessage();
                    inputDataService.setResultMessageAttribute(null, request);
                    request.getRequestDispatcher("/RequestAssistancePage.jsp").forward(request, response);
                    return;
                }

            }
            Boolean vehicleNotInDB = false;
            try {
                vehicleNotInDB = inputDataService.isCorrectVehicleData(brand, model, licenseNumber, emf, true);
            } catch (Exception e) {
                // db exception
                inputDataService.generateErrorResultMessage();
                inputDataService.setResultMessageAttribute(null, request);
                request.getRequestDispatcher("/RequestAssistancePage.jsp").forward(request, response);
                return;
            }
            ClientCar car;
            // if such car alrady exists in db
            if (!vehicleNotInDB) {
                try {
                    car = databaseService.getClientCarByLicenseNumber(licenseNumber, emf);
                } catch (Exception e) {
                    // db exception
                    inputDataService.generateErrorResultMessage();
                    inputDataService.setResultMessageAttribute(null, request);
                    request.getRequestDispatcher("/RequestAssistancePage.jsp").forward(request, response);
                    return;
                }
            } else {
                // if car is not present in db add it
                Integer productionYearInt;
                try {
                    productionYearInt = Integer.valueOf(productionYear);
                } catch (NumberFormatException ex) {
                    inputDataService.setResultMessageAttribute("Production Year needs to be a number!", request);
                    request.getRequestDispatcher("/RequestAssistancePage.jsp").forward(request, response);
                    return;
                }
      
                car = new ClientCar(brand, model, licenseNumber.toUpperCase(), productionYearInt, cln);
                try {
                    databaseService.addEntities(new Object[]{car}, emf, utx);
                } catch (Exception e) {
                    // db exception
                    inputDataService.generateErrorResultMessage();
                    inputDataService.setResultMessageAttribute(null, request);
                    request.getRequestDispatcher("/RequestAssistancePage.jsp").forward(request, response);
                    return;
                }
            }

            ArrayList<Product> products = new ArrayList<>();
            ArrayList<Payment> payments = new ArrayList<>();
            ArrayList<AssistanceRequest> assistanceRq = new ArrayList<>();

            // if services were selected crete products based on them
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
            return;
        }

        // check if user is already logged in
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

        // fetch services to display
        try {
            sServices = new ArrayList<SingleService>(databaseService.getSingleServices(emf));
            request.getSession().setAttribute("services", sServices);
        } catch (Exception e) {
            //db exception
            inputDataService.generateErrorResultMessage();
            inputDataService.setResultMessageAttribute(null, request);
            request.getRequestDispatcher("/RequestAssistancePage.jsp").forward(request, response);
            return;
        }
        request.getRequestDispatcher("/RequestAssistancePage.jsp").forward(request, response);
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
