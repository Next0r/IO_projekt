package pl.polsl.io.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;
import pl.polsl.io.model.Client;
import pl.polsl.io.model.ProductType;
import pl.polsl.io.model.UserAccount;
import pl.polsl.io.service.CookieManager;
import pl.polsl.io.service.DatabaseManager;

/**
 *
 * @author Sobocik
 */
public class TEMP_PopulateDB extends HttpServlet {

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
    
    
    private DatabaseManager databaseManager;
    private CookieManager cookieManager;
      
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

        databaseManager = (DatabaseManager) request.getSession().getAttribute("databaseManager");
        cookieManager = (CookieManager) request.getSession().getAttribute("cookieManager");
        
        
        String alreadyPopulated = "false";
        alreadyPopulated = cookieManager.getCookieValue("alreadyPopulated", request);

        if (alreadyPopulated.equals("true")) {
            request.getRequestDispatcher("Homepage.jsp").forward(request, response);
        } else {

            UserAccount acc1 = new UserAccount("nowak", "123");
            UserAccount acc2 = new UserAccount("piotrowicz", "123");
            UserAccount acc3 = new UserAccount("kapok", "123");
            UserAccount acc4 = new UserAccount("leetgamer69", "123");

            Client client1 = new Client("Adam", "Nowak", acc1);
            Client client2 = new Client("Piotr", "Piotrowicz", acc2);
            Client client3 = new Client("Zuzanna", "Kapok", acc3);
            Client client4 = new Client("Jan", "Morszczyn", acc4);
                 
            databaseManager.addEntities(new Object[]{acc1, acc2, acc3, acc4,
            client1, client2, client3, client4}, emf, utx);

            Cookie cookie = new Cookie("alreadyPopulated", "true");
            //cookie.setMaxAge(60 * 5);
            response.addCookie(cookie);
            
            request.getRequestDispatcher("Homepage.jsp").forward(request, response);
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
