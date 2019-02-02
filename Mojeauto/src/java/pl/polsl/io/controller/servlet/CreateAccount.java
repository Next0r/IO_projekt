package pl.polsl.io.controller.servlet;

import java.io.IOException;
import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;
import pl.polsl.io.model.Client;
import pl.polsl.io.model.UserAccount;
import pl.polsl.io.service.InputDataService;
import pl.polsl.io.service.DatabaseService;

/**
 *
 * @author Michal
 */
public class CreateAccount extends HttpServlet {

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

        String login = inputDataService.nullStringTrim(request.getParameter("login"));
        String password = inputDataService.nullStringTrim(request.getParameter("password"));
        String repassword = inputDataService.nullStringTrim(request.getParameter("repassword"));

        Boolean isCorrectRegisterData = false;
        try {
            isCorrectRegisterData = inputDataService.isCorrectRegisterData(login, password, repassword, emf, utx);
        } catch (Exception e) {
            inputDataService.generateErrorResultMessage();
        }
        if (isCorrectRegisterData) {
            UserAccount acc = new UserAccount(login, password);
            Client client = new Client("_", "_", acc);
            try {
                databaseService.addEntities(new Object[]{acc, client}, emf, utx);
                inputDataService.setResultMessageAttribute(null, request);
            } catch (Exception e) {
                // db exception
            }
            request.getRequestDispatcher("/LoginRegisterPage.jsp").forward(request, response);
        } else {
            // print register fail message
            inputDataService.setResultMessageAttribute(null, request);
            request.getRequestDispatcher("/LoginRegisterPage.jsp").forward(request, response);
        }

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
