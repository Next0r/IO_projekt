package pl.polsl.io.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
import pl.polsl.io.model.UserAccount;
import pl.polsl.io.service.CookieService;
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
    private CookieService cookieService;
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
        cookieService = (CookieService) request.getSession().getAttribute("cookieService");
        inputDataService = (InputDataService) request.getSession().getAttribute("inputDataService");

        
        // check if user already selected cars
        String carsSelected = request.getParameter("carsSelected");
        if(carsSelected != null){
            // user met all requirements
            // and selected cars
            // package id in selectedPackageId session attribute
            Integer selectedPackageId = (Integer) request.getSession().getAttribute("selectedPackageId");
            // cars displayed in clientCars session attribute
            ArrayList<ClientCar> clientCars = (ArrayList<ClientCar>) request.getSession().getAttribute("clientCars");
            
            //todo: find which cars has been selected
            // by comparing checkbox values (named with license numbers)
            
            // create payment object
            
            // redirect to paymet site
            
            // create products /related to user acc/car ids/single payment/ of package type
            
            // send purchase finalised message           
            
            
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
            if(!inputDataService.isClientDataComplete(cln)){
                inputDataService.setResultMessageAttribute(null, request);
                request.getRequestDispatcher("/OurProductsPage.jsp").forward(request, response);
                return;
            }
            // check if client has cars
            if(clientCars.isEmpty()){
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
        ArrayList<Package> packageOffer;
        try {
            packageOffer = new ArrayList<Package>(databaseService.getPackages(emf));
            request.getSession().setAttribute("packages", packageOffer);
        } catch (Exception e) {
            // db exception
        }
        request.getRequestDispatcher("/OurProductsPage.jsp").forward(request, response);

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
