package pl.polsl.io.controller.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
import pl.polsl.io.model.Product;
import pl.polsl.io.model.UserAccount;
import pl.polsl.io.service.DatabaseService;
import pl.polsl.io.service.InputDataService;

/**
 *
 * @author Michal
 */
public class FetchServices extends HttpServlet {

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

        UserAccount acc;
        Client cln;
        ArrayList<ClientCar> clientCars = new ArrayList<>();
        ArrayList<Product> clientProducts = new ArrayList<>();

        try {
            acc = (UserAccount) databaseService.getUserAccountEntity((String) request.getSession().getAttribute("currentUser"), "", emf);
            cln = databaseService.getClientEntityByAccount(acc, emf);
            clientCars = new ArrayList<>(databaseService.getClientCarsByClient(cln, emf));
            Stream stream = clientProducts.stream();
            for (ClientCar car : clientCars) {
                stream = Stream.concat(stream, databaseService.getProductsByClientCar(car, emf).stream());
            }
            clientProducts = (ArrayList<Product>) stream.collect(Collectors.toList());
            
        } catch (Exception e) {
            // db exception
            inputDataService.generateErrorResultMessage();
            inputDataService.setResultMessageAttribute(null, request);
            request.getRequestDispatcher("/MyServicesPage.jsp").forward(request, response);
            return;
        }

        if (clientProducts.isEmpty()) {
            inputDataService.setResultMessageAttribute("It looks like you still do not have any of our services. How about visiting our offer page?", request);
        } else {
            request.getSession().setAttribute("clientProducts", clientProducts);
        }

        request.getRequestDispatcher("/MyServicesPage.jsp").forward(request, response);

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
