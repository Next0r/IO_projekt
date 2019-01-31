package pl.polsl.io.servlet;

import java.io.IOException;
import java.io.PrintWriter;
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
import pl.polsl.io.service.AccountService;
import pl.polsl.io.service.CookieService;
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
    private CookieService cookieService;
    private AccountService accountService;

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
        accountService = (AccountService) request.getSession().getAttribute("accountService");

        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String repassword = request.getParameter("repassword");

        Boolean isCorrectRegisterData = false;
        try {
            isCorrectRegisterData = accountService.isCorrectRegisterData(login, password, repassword, emf, utx);
        } catch (Exception e) {
            accountService.generateErrorMessage();
        }
        if (isCorrectRegisterData) {
            UserAccount acc = new UserAccount(login, password);
            Client client = new Client("_", "_", acc);
            try {
                databaseService.addEntities(new Object[]{acc, client}, emf, utx);
                request.getSession().setAttribute("accountMessage", accountService.getAccountMessage());
            } catch (Exception e) {
                // db exception
            }
            request.getRequestDispatcher("/LoginRegisterPage.jsp").forward(request, response);
        } else {
            // print register fail message
            request.getSession().setAttribute("accountMessage", accountService.getAccountMessage());
            request.getRequestDispatcher("/LoginRegisterPage.jsp").forward(request, response);
        }

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
