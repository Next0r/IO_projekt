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
import pl.polsl.io.model.UserAccount;
import pl.polsl.io.service.AccountService;
import pl.polsl.io.service.CookieService;
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

    private DatabaseService databaseManager;
    private CookieService cookieManager;
    private AccountService accountManager;

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
        databaseManager = (DatabaseService) request.getSession().getAttribute("databaseManager");
        cookieManager = (CookieService) request.getSession().getAttribute("cookieManager");
        accountManager = (AccountService) request.getSession().getAttribute("accountManager");
        

        
        String hidden = request.getParameter("hidden");
        // handle log out
        if(hidden != null){
            request.getSession().setAttribute("currentUser", "");
            request.getRequestDispatcher("/Homepage.jsp").forward(request, response);
            return;
        }
        
        // handle log in
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        
        if(accountManager.isCorrectLogInData(login, password, emf, utx)){
            request.getSession().setAttribute("currentUser", login);
            request.getRequestDispatcher("/Homepage.jsp").forward(request, response);
        }
        else{
            // send account manager fail message
            request.getSession().setAttribute("accountMessage", accountManager.getAccountMessage());
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
