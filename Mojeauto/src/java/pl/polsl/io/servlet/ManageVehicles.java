package pl.polsl.io.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
import pl.polsl.io.model.UserAccount;
import pl.polsl.io.service.InputDataService;
import pl.polsl.io.service.CookieService;
import pl.polsl.io.service.DatabaseService;

/**
 *
 * @author Michal
 */
public class ManageVehicles extends HttpServlet {

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
        cookieService = (CookieService) request.getSession().getAttribute("cookieService");
        inputDataService = (InputDataService) request.getSession().getAttribute("inputDataService");

        UserAccount acc;
        Client cln;

        String brand = inputDataService.nullStringTrim(request.getParameter("brand"));
        String model = inputDataService.nullStringTrim(request.getParameter("model"));
        String licenseNumber = inputDataService.nullStringTrim(request.getParameter("lnumber"));
        String pYear = inputDataService.nullStringTrim(request.getParameter("pyear"));
        String hidden = inputDataService.nullStringTrim(request.getParameter("hidden"));

        // add car request
        if (hidden != null && hidden.equals("add")) {
            Integer productionYear = inputDataService.getCorrectProductionYear(pYear);
            Boolean isCorrectVehicleData;

            try {
                isCorrectVehicleData = inputDataService.isCorrectVehicleData(brand, model, licenseNumber, emf);
            } catch (Exception e) {
                //db exception
                inputDataService.generateErrorResultMessage();
                inputDataService.setResultMessageAttribute(null, request);
                request.getRequestDispatcher("AddVehiclePage.jsp").forward(request, response);
                return;
            }

            if (productionYear == null || isCorrectVehicleData == false) {
                if (productionYear == null) {
                    inputDataService.setResultMessageAttribute("Vehicle production year is incorrect.", request);
                }
                inputDataService.setResultMessageAttribute(null, request);
                request.getRequestDispatcher("AddVehiclePage.jsp").forward(request, response);
                return;
            } else {
                // vehicle can be inserted to db
                try {
                    acc = databaseService.getUserAccountEntity((String) request.getSession().getAttribute("currentUser"), "", emf);
                    cln = databaseService.getClientEntityByAccount(acc, emf);
                    ClientCar car = new ClientCar(brand, model, licenseNumber.toUpperCase(), productionYear, cln);
                    databaseService.addEntities(new Object[]{car}, emf, utx);
                    inputDataService.setResultMessageAttribute("New vehicle has been added.", request);
                } catch (Exception e) {
                    //db exception
                    inputDataService.generateErrorResultMessage();
                    inputDataService.setResultMessageAttribute(null, request);
                    request.getRequestDispatcher("AddVehiclePage.jsp").forward(request, response);
                    return;
                }
            }
            request.getRequestDispatcher("AddVehiclePage.jsp").forward(request, response);
            return;
        }

        // remove car
        if (hidden != null) {
            Integer removedCarId = Integer.valueOf(hidden);

            try {
                ClientCar car = databaseService.getClientCarByCarId(removedCarId, emf);
                databaseService.deleteEntity(car, emf, utx);
                inputDataService.setResultMessageAttribute("Vehicle has been removed.", request);
            } catch (Exception e) {
                // db exception
                inputDataService.generateErrorResultMessage();
                inputDataService.setResultMessageAttribute(null, request);
            }

        }

        // fetch car data
        ArrayList<ClientCar> clientCars = null;
        try {
            acc = databaseService.getUserAccountEntity((String) request.getSession().getAttribute("currentUser"), "", emf);
            cln = databaseService.getClientEntityByAccount(acc, emf);
            clientCars = new ArrayList<ClientCar>(databaseService.getClientCarsByClient(cln, emf));
        } catch (Exception e) {
            //db exception
            inputDataService.generateErrorResultMessage();
            inputDataService.setResultMessageAttribute(null, request);
        }
        if (clientCars != null) {
            request.getSession().setAttribute("clientCars", clientCars);
        }

        request.getRequestDispatcher("MyVehiclesPage.jsp").forward(request, response);

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
