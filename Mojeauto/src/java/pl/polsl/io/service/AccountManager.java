package pl.polsl.io.service;

import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import pl.polsl.io.model.UserAccount;

/**
 *
 * @author Michal
 */
public class AccountManager {
    
    private String accountMessage;
    private DatabaseManager databaseManager;
    
    public AccountManager(DatabaseManager databaseManager){
        this.databaseManager = databaseManager;
    }
       
    public String getAccountMessage(){
        return this.accountMessage;
    }
    
    public Boolean isCorrectLogInData(String login, String password, EntityManagerFactory emf, UserTransaction utx){
        if(login == null || password == null){
            accountMessage = "Login and password cannot be empty.";
            return false;
        }
        if(login.isEmpty() || password.isEmpty()){
            accountMessage = "Login and password cannot be empty";
            return false;
        }
        UserAccount acc = (UserAccount) databaseManager.getUserAccountEntity(login, password, emf);
        if(acc == null){
            accountMessage = "Incorrect login or password";
            return false;
        }
        return true;
    }
    
    public Boolean isCorrectRegisterData(String login, String password, String repassword, EntityManagerFactory emf, UserTransaction utx){
        if(login == null || password == null || repassword == null){
            accountMessage = "One of required fields is empty.";
            return false;
        }
        if(login.isEmpty() || password.isEmpty() || repassword.isEmpty()){
            accountMessage = "One of required fields is empty.";
            return false;
        }
        if(!password.equals(repassword)){
            accountMessage = "Password and password retype field value does not match.";
            return false;
        }
        UserAccount acc = (UserAccount) databaseManager.getUserAccountEntity(login, "", emf);
        if(acc != null){
            accountMessage = "This login is already taken, choose different one.";
            return false;
        }
        accountMessage = "Account successfuly created.";
        return true;
    }
        
}
