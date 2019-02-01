package pl.polsl.io.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
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
import pl.polsl.io.model.ClientCar;
import pl.polsl.io.model.SingleService;
import pl.polsl.io.model.Package;
import pl.polsl.io.model.UserAccount;
import pl.polsl.io.service.CookieService;
import pl.polsl.io.service.DatabaseService;

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

    private DatabaseService databaseService;
    private CookieService cookieService;

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

        UserAccount acc1 = new UserAccount("nowak", "123");
        UserAccount acc2 = new UserAccount("piotrowicz", "123");
        UserAccount acc3 = new UserAccount("kapok", "123");
        UserAccount acc4 = new UserAccount("leetgamer69", "123");

        Client client1 = new Client("Adam", "Nowak", acc1);
        Client client2 = new Client("Piotr", "Piotrowicz", acc2);
        Client client3 = new Client("Zuzanna", "Kapok", acc3);
        Client client4 = new Client("Jan", "Morszczyn", acc4);

        SingleService s1 = new SingleService("Engine oil replacement", "Replacement of engine oil in a car. The oil is chosen based on brand and engine type.", 150.00);
        SingleService s2 = new SingleService("Power steering fluid replacement", "Replacement of power steering fluid in a car.", 150.00);
        SingleService s3 = new SingleService("Coolant fluid replacement", "Replacement of coolant fluid in a car.", 150.00);

        SingleService s4 = new SingleService("service4", "s4 description", 40.0);
        SingleService s5 = new SingleService("service5", "s5 description", 50.0);
        SingleService s6 = new SingleService("service6", "s6 description", 60.0);

        SingleService s7 = new SingleService("service7", "s7 description", 70.0);
        SingleService s8 = new SingleService("service8", "s8 description", 80.0);
        SingleService s9 = new SingleService("service9", "s9 description", 90.0);

        ClientCar car1 = new ClientCar("TestCar1", "Test01", "LN1234", 2019, client1);
        ClientCar car2 = new ClientCar("TestCar2", "Test02", "LN1235", 2019, client1);
        ClientCar car3 = new ClientCar("TestCar3", "Test03", "LN1236", 2019, client1);

        ArrayList<SingleService> list = new ArrayList<>(Arrays.asList(s1, s2, s3));
        Package p1 = new Package("General fluid replacement", "Replacement of engine oil, power steering fluid and coolant fluid in a car.", 350.00, list);
        list = new ArrayList<>(Arrays.asList(s4, s5, s6));
        Package p2 = new Package("Package2", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam dignissim consectetur neque, id finibus nulla volutpat sit amet. Suspendisse vulputate, odio nec viverra interdum, odio tortor dignissim est, eu egestas nibh purus a dui. Sed metus quam, porttitor nec feugiat quis, efficitur nec enim. Nam euismod semper elit, ac cursus est ultrices non. Aliquam vitae justo blandit, consectetur massa quis, scelerisque lacus. Ut pellentesque quam ac risus egestas, eget eleifend magna bibendum. Sed a tincidunt leo. Nam vitae ligula bibendum, convallis mi eget, accumsan neque. Fusce malesuada pharetra nunc, in lacinia ipsum aliquam sit amet. ", 200.00, list);
        list = new ArrayList<>(Arrays.asList(s7, s8, s9));
        Package p3 = new Package("Package3", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam dignissim consectetur neque, id finibus nulla volutpat sit amet. Suspendisse vulputate, odio nec viverra interdum, odio tortor dignissim est, eu egestas nibh purus a dui. Sed metus quam, porttitor nec feugiat quis, efficitur nec enim. Nam euismod semper elit, ac cursus est ultrices non. Aliquam vitae justo blandit, consectetur massa quis, scelerisque lacus. Ut pellentesque quam ac risus egestas, eget eleifend magna bibendum. Sed a tincidunt leo. Nam vitae ligula bibendum, convallis mi eget, accumsan neque. Fusce malesuada pharetra nunc, in lacinia ipsum aliquam sit amet. ", 300.00, list);

        UserAccount acc = null;
        try {
            acc = databaseService.getUserAccountEntity("nowak", "123", emf);
        } catch (Exception e) {
            // db exception
        }

        if (acc != null) {
            request.getRequestDispatcher("Homepage.jsp").forward(request, response);
        } else {
            try {
                databaseService.addEntities(new Object[]{acc1, acc2, acc3, acc4}, emf, utx);
                databaseService.addEntities(new Object[]{client1, client2, client3, client4}, emf, utx);
                databaseService.addEntities(new Object[]{s1, s2, s3, s4, s5, s6, s7, s8, s9}, emf, utx);

                databaseService.addEntities(new Object[]{car1, car2, car3}, emf, utx);

                databaseService.addEntities(new Object[]{p1, p2, p3}, emf, utx);

            } catch (Exception e) {
                System.err.print(e.getMessage());
                // db exception
            }
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
