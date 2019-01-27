package pl.polsl.io.service;

import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import pl.polsl.io.model.UserAccount;

/**
 *
 * @author Michal
 */
public class AccountManager {
    
    private String loginMessage;
    private DatabaseManager databaseManager;
    
    public AccountManager(DatabaseManager databaseManager){
        this.databaseManager = databaseManager;
    }
       
    public String getLoginMessage(){
        return this.loginMessage;
    }
    
    public Boolean isCorrectLogInData(String login, String password, EntityManagerFactory emf, UserTransaction utx){
        if(login == null || password == null){
            loginMessage = "Login and password cannot be empty.";
            return false;
        }
        if(login.isEmpty() || password.isEmpty()){
            loginMessage = "Login and password cannot be empty";
            return false;
        }
        UserAccount acc = (UserAccount) databaseManager.getUserAccountEntity(login, password, emf, utx);
        if(acc == null){
            loginMessage = "Incorrect login or password";
            return false;
        }
        return true;
    }
        
}
