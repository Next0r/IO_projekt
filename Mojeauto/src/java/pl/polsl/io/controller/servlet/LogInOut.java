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
import pl.polsl.io.service.InputDataService;
import pl.polsl.io.service.DatabaseService;

/**
 *
 * @author Michal
 */
public class LogInOut extends HttpServlet {

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

        String hidden = request.getParameter("hidden");
        // handle log out
        if (hidden != null) {
            request.getSession().setAttribute("currentUser", null);
            request.getSession().setAttribute("clientName", null);
            request.getSession().setAttribute("clientSurname", null);
            request.getSession().setAttribute("clientCars", null);
            request.getSession().setAttribute("clientPhone", null);
            request.getRequestDispatcher("/Homepage.jsp").forward(request, response);
            return;
        }

        // handle log in
        String login = inputDataService.nullStringTrim(request.getParameter("login"));
        String password = inputDataService.nullStringTrim(request.getParameter("password"));
        Boolean isCorrectUserData = false;
        try {
            isCorrectUserData = inputDataService.isCorrectLogInData(login, password, emf, utx);
        } catch (Exception e) {
            inputDataService.generateErrorResultMessage();
        }

        if (isCorrectUserData) {
            request.getSession().setAttribute("currentUser", login);
            request.getRequestDispatcher("/Homepage.jsp").forward(request, response);
        } else {
            // send account manager fail message
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
