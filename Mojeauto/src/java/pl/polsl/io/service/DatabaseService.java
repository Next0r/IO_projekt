package pl.polsl.io.service;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
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
        try {
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
        } catch (NoResultException e) {
            acc = null;
        }
        em.close();
        return acc;
    }

    public Client getClientEntityByAccount(UserAccount acc, EntityManagerFactory emf) throws Exception {
        Client cln = null;
        EntityManager em = null;
        if (acc != null) {
            try {
                em = emf.createEntityManager();
                cln = (Client) em.createQuery("select c from Client c WHERE c.userAccount = :acc")
                        .setParameter("acc", acc)
                        .getSingleResult();
                em.close();
            } catch (NoResultException e) {
                cln = null;
            }
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
        clientCars = em.createQuery("select c from ClientCar c where c.owner = :p1")
                .setParameter("p1", cln)
                .getResultList();
        em.close();
        return clientCars;
    }

    public ClientCar getClientCarByCarId(Integer carId, EntityManagerFactory emf) throws Exception {

        if (carId == null) {
            return null;
        }
        ClientCar car;
        EntityManager em;
        em = emf.createEntityManager();
        try {
            car = (ClientCar) em.createQuery("select c from ClientCar c where c.clientCarID = :p1")
                    .setParameter("p1", carId)
                    .getSingleResult();
        } catch (NoResultException e) {
            car = null;
        }
        return car;
    }

    public ClientCar getClientCarByLicenseNumber(String licenseNumber, EntityManagerFactory emf) throws Exception {

        if (licenseNumber == null) {
            return null;
        }
        ClientCar car;
        EntityManager em;
        em = emf.createEntityManager();
        try {
            car = (ClientCar) em.createQuery("select c from ClientCar c where c.licenseNumber = :p1")
                    .setParameter("p1", licenseNumber.toUpperCase())
                    .getSingleResult();
        } catch (NoResultException e) {
            car = null;
        }
        return car;
    }

    public List getPackages(EntityManagerFactory emf) throws Exception {
        EntityManager em;
        em = emf.createEntityManager();
        List packages = em.createQuery("select p from Package p")
                .getResultList();
        em.close();
        return packages;
    }

    public pl.polsl.io.model.Package getPackageById(Integer packageId, EntityManagerFactory emf) throws Exception {
        if (packageId == null) {
            return null;
        }
        EntityManager em;
        em = emf.createEntityManager();
        pl.polsl.io.model.Package pckg = null;
        try {
            pckg = (pl.polsl.io.model.Package) em.createQuery("select p from Package p where p.productTypeID = :p1")
                    .setParameter("p1", packageId)
                    .getSingleResult();
        } catch (NoResultException e) {
        }
        return pckg;
    }

    public void deleteEntity(Object entity, EntityManagerFactory emf, UserTransaction utx) throws Exception {
        if (entity == null) {
            return;
        }
        EntityManager em;
        utx.begin();
        em = emf.createEntityManager();
        if (em.contains(entity)) {
            em.remove(entity);
        } else {
            em.remove(em.merge(entity));
        }
        utx.commit();
        em.close();
    }

}
