package pl.polsl.io.service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import pl.polsl.io.model.Client;
import pl.polsl.io.model.UserAccount;

/**
 *
 * @author Michal
 */
public class DatabaseService {

    public DatabaseService() {
    }

    public void addEntities(Object[] entities, EntityManagerFactory emf, UserTransaction utx) {
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

    public UserAccount getUserAccountEntity(String login, String password, EntityManagerFactory emf) {
        EntityManager em = null;
        UserAccount acc = null;
        try {
            em = emf.createEntityManager();
            if (password.isEmpty()) {
                acc = (UserAccount) em.createQuery("select a from UserAccount a WHERE a.login LIKE :login")
                        .setParameter("login", login)
                        .getSingleResult();
            } else {
                acc = (UserAccount) em.createQuery("select a from UserAccount a WHERE a.login LIKE :login and a.password LIKE :password")
                        .setParameter("login", login)
                        .setParameter("password", password)
                        .getSingleResult();
            }
        } catch (Exception e) {

        } finally {
            if (em != null) {
                em.close();
            }
        }
        return acc;
    }
    
    public Client getClientEntityByAccount(UserAccount acc, EntityManagerFactory emf, UserTransaction utx){
        Client cln = null;
        EntityManager em = null;
        if(acc != null){
            try {
                em = emf.createEntityManager();
                cln = (Client) em.createQuery("select c from Client c WHERE c.userAccount = :acc")
                        .setParameter("acc", acc)
                        .getSingleResult();
            } catch (Exception e) {

            } finally {
                if (em != null) {
                    em.close();
                }
            }
        }
        return cln;
    }

}
