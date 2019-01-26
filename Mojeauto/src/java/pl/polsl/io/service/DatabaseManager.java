package pl.polsl.io.service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.transaction.UserTransaction;

/**
 *
 * @author Michal
 */
public class DatabaseManager {
    public void DatabaseManager(){};
    
    public void addEntities(Object[] entities, EntityManagerFactory emf, UserTransaction utx) throws ServletException{
        EntityManager em = null;
        try{    
            utx.begin();
            em = emf.createEntityManager();
            for(Object entity : entities){
                em.persist(entity);
            }
            utx.commit();
            
        }catch(Exception e){
            throw new ServletException(e);
        }finally{
            if(em != null){
                em.close();
            }
        }
    }
    
}
