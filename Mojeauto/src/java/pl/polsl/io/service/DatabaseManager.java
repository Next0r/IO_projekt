package pl.polsl.io.service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.transaction.UserTransaction;
import pl.polsl.io.model.UserAccount;

/**
 *
 * @author Michal
 */
public class DatabaseManager {

    public DatabaseManager() {
    }

    public void addEntities(Object[] entities, EntityManagerFactory emf, UserTransaction utx){
        EntityManager em = null;
        try {
            utx.begin();
            em = emf.createEntityManager();
            for (Object entity : entities) {
                em.persist(entity);
            }
            utx.commit();

        } catch (Exception e) {

        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public UserAccount getUserAccountEntity(String login, String password, EntityManagerFactory emf, UserTransaction utx) {
        EntityManager em = null;
        UserAccount acc = null;
        try {
            utx.begin();
            em = emf.createEntityManager();
            acc = (UserAccount) em.createQuery("select a from UserAccount a WHERE a.login LIKE :login and a.password LIKE :password")
                    .setParameter("login", login)
                    .setParameter("password", password)
                    .getSingleResult();
            utx.commit();
        } catch (Exception e) {
            
        } finally {
            if(em != null){
                em.close();
            }
        }
        return acc;
    }

}
