package pl.polsl.io.controller.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;
import pl.polsl.io.model.Client;
import pl.polsl.io.model.ClientCar;
import pl.polsl.io.model.Payment;
import pl.polsl.io.model.Product;
import pl.polsl.io.model.UserAccount;
import pl.polsl.io.service.DatabaseService;
import pl.polsl.io.service.InputDataService;

/**
 *
 * @author Michal
 */
public class PackageSales extends HttpServlet {

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

        // check if user already selected cars
        String carsSelected = request.getParameter("carsSelected");
        if (carsSelected != null) {
            // user met all requirements
            // and selected cars
            // package id in selectedPackageId session attribute
            Integer selectedPackageId = (Integer) request.getSession().getAttribute("selectedPackageId");
            // cars displayed in clientCars session attribute
            ArrayList<ClientCar> clientCars = (ArrayList<ClientCar>) request.getSession().getAttribute("clientCars");
            // check box returns null if not checked
            ArrayList<ClientCar> selectedCars = new ArrayList<>();
            for (ClientCar car : clientCars) {
                if (request.getParameter(car.getLicenseNumber()) != null) {
                    selectedCars.add(car);
                }
            }
            // check if any cars selected
            if (selectedCars.isEmpty()) {
                request.getSession().setAttribute("carSelection", true);
                inputDataService.setResultMessageAttribute("Package have to be related to at least one vehicle.", request);
                request.getRequestDispatcher("/OurProductsPage.jsp").forward(request, response);
                return;
            }
            // get selected package
            pl.polsl.io.model.Package pckg;
            try {
                pckg = databaseService.getPackageById(selectedPackageId, emf);
            } catch (Exception e) {
                //db exception
                inputDataService.generateErrorResultMessage();
                inputDataService.setResultMessageAttribute(null, request);
                request.getRequestDispatcher("/OurProductsPage.jsp").forward(request, response);
                return;
            }
            if (pckg != null) {
                UserAccount acc;
                Payment payment = new Payment(selectedCars.size() * pckg.getPrice(), "", false, new Date());
                // fetch account add payment
                try {
                    acc = databaseService.getUserAccountEntity((String) request.getSession().getAttribute("currentUser"), "", emf);
                    databaseService.addEntities(new Object[]{payment}, emf, utx);
                } catch (Exception e) {
                    // db exception
                    inputDataService.generateErrorResultMessage();
                    inputDataService.setResultMessageAttribute(null, request);
                    request.getRequestDispatcher("/OurProductsPage.jsp").forward(request, response);
                    return;
                }
                // add one year to expiration date
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                c.add(Calendar.YEAR, 1);
                // add products 
                for (ClientCar car : selectedCars) {
                    try {
                        Product product = new Product(c.getTime(), acc, pckg, payment, car);
                        databaseService.addEntities(new Object[]{product}, emf, utx);
                    } catch (Exception e) {
                        // db exception
                        inputDataService.generateErrorResultMessage();
                        inputDataService.setResultMessageAttribute(null, request);
                        request.getRequestDispatcher("/OurProductsPage.jsp").forward(request, response);
                        return;
                    }
                }
                inputDataService.setResultMessageAttribute("Good job! With this package your vehicle(s) are safe once again!", request);
            }
            request.getRequestDispatcher("/OurProductsPage.jsp").forward(request, response);
            return;
        }

        // sell package if selected
        String selPackageId = request.getParameter("packageSelected");
        if (selPackageId != null) {
            // fetch client entity and his cars
            UserAccount acc;
            Client cln;
            ArrayList<ClientCar> clientCars;
            try {
                acc = databaseService.getUserAccountEntity((String) request.getSession().getAttribute("currentUser"), "", emf);
                cln = databaseService.getClientEntityByAccount(acc, emf);
                clientCars = new ArrayList<ClientCar>(databaseService.getClientCarsByClient(cln, emf));
            } catch (Exception e) {
                // db exception
                inputDataService.generateErrorResultMessage();
                inputDataService.setResultMessageAttribute(null, request);
                request.getRequestDispatcher("/OurProductsPage.jsp").forward(request, response);
                return;
            }
            // check if client has set up his personal data
            if (!inputDataService.isClientDataComplete(cln)) {
                inputDataService.setResultMessageAttribute(null, request);
                request.getRequestDispatcher("/OurProductsPage.jsp").forward(request, response);
                return;
            }
            // check if client has cars
            if (clientCars.isEmpty()) {
                inputDataService.setResultMessageAttribute("There are no vehicles related to your account.", request);
                request.getRequestDispatcher("/OurProductsPage.jsp").forward(request, response);
                return;
            }
            // request car selection display
            request.getSession().setAttribute("carSelection", true);
            request.getSession().setAttribute("clientCars", clientCars);
            request.getRequestDispatcher("/OurProductsPage.jsp").forward(request, response);

            // selPackageId is allways package id (as generated number)
            Integer selectedPackageId = Integer.valueOf(selPackageId);
            request.getSession().setAttribute("selectedPackageId", selectedPackageId);
            return;
        }

        // fetch package offer
        ArrayList<pl.polsl.io.model.Package> packageOffer;
        try {
            packageOffer = new ArrayList<>(databaseService.getPackages(emf));
            Collections.sort(packageOffer, (pl.polsl.io.model.Package lhs, pl.polsl.io.model.Package rhs) -> lhs.getPrice() > rhs.getPrice() ? 1 : -1);
            request.getSession().setAttribute("packages", packageOffer);
      
        } catch (Exception e) {
            // db exception
        }
        request.getRequestDispatcher("/OurProductsPage.jsp").forward(request, response);

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
