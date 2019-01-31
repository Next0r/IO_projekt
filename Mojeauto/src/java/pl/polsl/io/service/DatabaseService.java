package pl.polsl.io.service;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import pl.polsl.io.model.Client;
import pl.polsl.io.model.ClientCar;
import pl.polsl.io.model.UserAccount;

/**
 *
 * @author Michal
 */
public class DatabaseService {

    public DatabaseService() {
    }

    public void addEntities(Object[] entities, EntityManagerFactory emf, UserTransaction utx) throws Exception {
        EntityManager em;
        utx.begin();
        em = emf.createEntityManager();
        for (Object entity : entities) {
            em.persist(entity);
        }
        utx.commit();
        em.close();
    }

    public UserAccount getUserAccountEntity(String login, String password, EntityManagerFactory emf) throws Exception {
        EntityManager em;
        UserAccount acc;
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
        em.close();
        return acc;
    }

    public Client getClientEntityByAccount(UserAccount acc, EntityManagerFactory emf) throws Exception {
        Client cln = null;
        EntityManager em = null;
        if (acc != null) {
            em = emf.createEntityManager();
            cln = (Client) em.createQuery("select c from Client c WHERE c.userAccount = :acc")
                    .setParameter("acc", acc)
                    .getSingleResult();
            em.close();
        }
        return cln;
    }

    public void updateClientParameter(String paramName, String paramValue, Integer clientId, EntityManagerFactory emf, UserTransaction utx) throws Exception {
        EntityManager em;
        if (paramName != null && paramValue != null) {
            if (!paramName.isEmpty() && !paramValue.isEmpty()) {
                Boolean valueIsNumber;
                utx.begin();
                em = emf.createEntityManager();
                //System.out.println("update Client set " + paramName + " = " + paramValue + " where clientID = " + clientId);
                try {
                    Integer.valueOf(paramValue);
                    valueIsNumber = true;
                } catch (NumberFormatException ex) {
                    valueIsNumber = false;
                }

                if (valueIsNumber) {
                    em.createQuery("update Client set " + paramName + " = " + Integer.valueOf(paramValue) + " where clientID = " + clientId)
                            .executeUpdate();
                } else {
                    em.createQuery("update Client set " + paramName + " = \'" + paramValue + "\' where clientID = " + clientId)
                            .executeUpdate();
                }
                utx.commit();

                em.close();
            }
        }
    }

    public Boolean isUserAccountField(String fieldName) {
        if (fieldName == null) {
            return false;
        }
        try {
            UserAccount.class.getDeclaredField(fieldName);
            return true;
        } catch (NoSuchFieldException | SecurityException e) {
            return false;
        }
    }

    public void updateUserAccountParameter(String paramName, String paramValue, Integer accountId, EntityManagerFactory emf, UserTransaction utx) throws Exception {
        EntityManager em;
        if (paramName != null && paramValue != null) {
            if (!paramName.isEmpty() && !paramValue.isEmpty()) {
                Boolean valueIsNumber;
                utx.begin();
                em = emf.createEntityManager();
                try {
                    Integer.valueOf(paramValue);
                    valueIsNumber = true;
                } catch (NumberFormatException ex) {
                    valueIsNumber = false;
                }
                if (valueIsNumber) {
                    em.createQuery("update UserAccount set " + paramName + " = " + paramValue + " where accountID = " + accountId)
                            .executeUpdate();
                } else {
                    em.createQuery("update UserAccount set " + paramName + " = '" + paramValue + "' where accountID = " + accountId)
                            .executeUpdate();
                }
                utx.commit();
                em.close();
            }
        }
    }

    public List getClientCarsByClient(Client cln, EntityManagerFactory emf) throws Exception {
        List clientCars;
        if (cln == null) {
            return null;
        }
        EntityManager em = emf.createEntityManager();
        clientCars = em.createQuery("select c from ClientCar c where c.clientCarID = :p1")
                .setParameter("p1", cln)
                .getResultList();
        em.close();
        return clientCars;
    }

}
