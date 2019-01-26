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

        Cookie[] cookies = request.getCookies();
        String alreadyPopulated = "false";
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals("alreadyPopulated")) {
                    alreadyPopulated = c.getValue();
                    break;
                }
            }
        }

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
           

            EntityManager em = null;
            try {
                System.out.println("START OF TRANSACTION");
                utx.begin();
                em = emf.createEntityManager();
                em.persist(acc1);
                em.persist(acc2);
                em.persist(acc3);
                em.persist(acc4);
                em.persist(client1);
                em.persist(client2);
                em.persist(client3);
                em.persist(client4);
                utx.commit();
                System.out.println("END OF TRANSACTION");

            } catch (Exception e) {
                throw new ServletException(e);
            } finally {
                if (em != null) {
                    em.close();
                }
            }

            Cookie cookie = new Cookie("alreadyPopulated", "true");
            cookie.setMaxAge(60 * 5);
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
