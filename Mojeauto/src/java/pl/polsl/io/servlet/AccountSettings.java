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
public class AccountSettings extends HttpServlet {

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

        // get data from settings form
        String hidden = request.getParameter("hidden");
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String repassword = request.getParameter("repassword");

        UserAccount acc = null;
        Client cln = null;

        try {
            acc = (UserAccount) databaseService.getUserAccountEntity((String) request.getSession().getAttribute("currentUser"), "", emf);
            cln = databaseService.getClientEntityByAccount(acc, emf);
        } catch (Exception e) {
            // db exception
        }
        if (cln != null) {
            // changing user parameters
            if (hidden != null) {
                // if user want to change something
                String changable = null;
                try {
                    changable = accountService.verifyChangedParameter(hidden, new String[]{name, surname, login, password, repassword}, emf);
                } catch (Exception e) {
                    // db exception
                    accountService.generateErrorMessage();
                }
                if (changable != null) {
                    // changable contains value of parameter that can be changed
                    // hidden contains name of this parameter in db
                    if (databaseService.isUserAccountField(hidden)) {
                        // account param should be changed
                        try {
                            databaseService.updateUserAccountParameter(hidden, changable, acc.getAccountID(), emf, utx);
                            // if login has been changed update current user
                            if (hidden.equals("login")) {
                                request.getSession().setAttribute("currentUser", login);
                            }
                            request.getSession().setAttribute("accountMessage", "Your " + hidden + " has been successfuly updated.");
                        } catch (Exception e) {
                            accountService.generateErrorMessage();
                            request.getSession().setAttribute("accountMessage", accountService.getAccountMessage());
                        }
                    } else {
                        // client param shoudl be changed
                        try {
                            databaseService.updateClientParameter(hidden, changable, cln.getClientID(), emf, utx);
                            request.getSession().setAttribute("accountMessage", "Your " + hidden + " has been successfuly updated.");
                        } catch (Exception e) {
                            accountService.generateErrorMessage();
                            request.getSession().setAttribute("accountMessage", accountService.getAccountMessage());
                        }
                    }
                } else {
                    // print fail message
                    request.getSession().setAttribute("accountMessage", accountService.getAccountMessage());
                }
            }
            // reflesh client data
            try {
                cln = databaseService.getClientEntityByAccount(acc, emf);
            } catch (Exception e) {
                // db exception
                accountService.generateErrorMessage();
                request.getSession().setAttribute("accountMessage", accountService.getAccountMessage());
            }
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
        }

        request.getRequestDispatcher("/AccountSettingsPage.jsp").forward(request, response);

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
