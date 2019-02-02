package pl.polsl.io.controller.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
import pl.polsl.io.model.SingleService;
import pl.polsl.io.model.Package;
import pl.polsl.io.model.UserAccount;
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

        UserAccount acc1 = new UserAccount("nowak", "123");
        UserAccount acc2 = new UserAccount("piotrowicz", "123");
        UserAccount acc3 = new UserAccount("kapok", "123");
        UserAccount acc4 = new UserAccount("leetgamer69", "123");

        Client client1 = new Client("Adam", "Nowak", acc1);
        Client client2 = new Client("Piotr", "Piotrowicz", acc2);
        Client client3 = new Client("Zuzanna", "Kapok", acc3);
        Client client4 = new Client("Jan", "Morszczyn", acc4);


        SingleService requestAssistance = new SingleService("Request assistance", "Allows for requesting assistance with issues concerning one's car. Each assistance request will be additionally priced based on the fixed issue.", 50.0);
        ArrayList<SingleService> list = new ArrayList<>(Arrays.asList(requestAssistance));
        Package basic = new Package("Basic Package", "The simplest package, allows for unlimited assistance requests for the package's duration. Each assistance request will be additionally priced based on the fixed issue.", 250.00, list);
        
        SingleService s1 = new SingleService("Engine oil replacement", "Replacement of engine oil in a car. The oil is chosen based on brand and engine type.", 150.00);
        SingleService s2 = new SingleService("Power steering fluid replacement", "Replacement of power steering fluid in a car.", 150.00);
        SingleService s3 = new SingleService("Coolant fluid replacement", "Replacement of coolant fluid in a car.", 150.00);
        SingleService replacementCar = new SingleService("Replacement Car", "A replacement car that's for use of the client during their car's repair.", 200.0);
        list = new ArrayList<>(Arrays.asList(requestAssistance, replacementCar, s1, s2, s3));
        Package advanced = new Package("Advanced Package", "More advanced package, includes unlimited assistance requests for the package's duration, as well as a replacement car for the duration of the repairs. Also includes a single replacement of engine oil, power steering fluid and coolant fluid", 500.00, list);

        SingleService consultantHelp = new SingleService("Consultant Help", "A personal consultant which can help with management and repairs of your car.", 150.0);
        SingleService instantHelp = new SingleService("Instant Help", "Allows for quick help on the road, including towing a broken car and an experts' assistance.", 200.0);
        list = new ArrayList<>(Arrays.asList(requestAssistance, replacementCar, s1, s2, s3, consultantHelp, instantHelp));
        Package premium = new Package("Premium Package", "The most exclusive package, includes the contents of the previous two packages, as well as a personal consultant's help concerning one's car and instant help on the road." , 750.00, list);
        

        ClientCar car1 = new ClientCar("TestCar1", "Test01", "LN1234", 2019, client1);
        ClientCar car2 = new ClientCar("TestCar2", "Test02", "LN1235", 2019, client1);
        ClientCar car3 = new ClientCar("TestCar3", "Test03", "LN1236", 2019, client1);

        
        
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
                databaseService.addEntities(new Object[]{s1, s2, s3, requestAssistance, consultantHelp, instantHelp, replacementCar}, emf, utx);
                databaseService.addEntities(new Object[]{car1, car2, car3}, emf, utx);
                databaseService.addEntities(new Object[]{premium, advanced, basic}, emf, utx);
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
