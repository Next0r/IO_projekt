package pl.polsl.io.controller.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import pl.polsl.io.service.DatabaseService;
import pl.polsl.io.service.InputDataService;

/**
 *
 * @author Sobocik
 */
public class CreateServices implements Filter {

    /**
     *
     * @param req The servlet request we are processing
     * @param resp The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    public void doFilter(ServletRequest req, ServletResponse resp,
            FilterChain chain)
            throws IOException, ServletException {
        //Create HttpServlet versions of request and response.
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        //Check if service objects exist in session.
        if (request.getSession().getAttribute("databaseService") == null) {
            //Create service objects if they don't exist
            DatabaseService databaseService = new DatabaseService();
            InputDataService inputDataService = new InputDataService(databaseService);
            //Add services to session.
            request.getSession().setAttribute("databaseService", databaseService);
            request.getSession().setAttribute("inputDataService", inputDataService);
        }

        //Continue normally.
        Throwable problem = null;
        try {
            chain.doFilter(request, response);
        } catch (Throwable t) {
            // If an exception is thrown somewhere down the filter chain,
            // we want to rethrow the problem.
            problem = t;
            t.printStackTrace();
        }

    }

    /**
     * Destroy method for this filter
     */
    public void destroy() {
    }

    /**
     * Init method for this filter
     */
    public void init(FilterConfig filterConfig) {
    }
}
