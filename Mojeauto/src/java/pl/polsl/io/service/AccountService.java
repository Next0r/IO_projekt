package pl.polsl.io.service;

import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import pl.polsl.io.model.UserAccount;

/**
 *
 * @author Michal
 */
public class AccountService {

    private String accountMessage;
    private DatabaseService databaseService;

    public AccountService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public String getAccountMessage() {
        return this.accountMessage;
    }

    public Boolean isCorrectLogInData(String login, String password, EntityManagerFactory emf, UserTransaction utx) {
        if (login == null || password == null) {
            accountMessage = "Login and password cannot be empty.";
            return false;
        }
        if (login.isEmpty() || password.isEmpty()) {
            accountMessage = "Login and password cannot be empty.";
            return false;
        }
        UserAccount acc = (UserAccount) databaseService.getUserAccountEntity(login, password, emf);
        if (acc == null) {
            accountMessage = "Incorrect login or password.";
            return false;
        }
        return true;
    }

    public Boolean isCorrectRegisterData(String login, String password, String repassword, EntityManagerFactory emf, UserTransaction utx) {
        if (login == null || password == null || repassword == null) {
            accountMessage = "One of required fields is empty.";
            return false;
        }
        if (login.isEmpty() || password.isEmpty() || repassword.isEmpty()) {
            accountMessage = "One of required fields is empty.";
            return false;
        }
        if (!password.equals(repassword)) {
            accountMessage = "Password and password retype field value does not match.";
            return false;
        }
        UserAccount acc = (UserAccount) databaseService.getUserAccountEntity(login, "", emf);
        if (acc != null) {
            accountMessage = "This login is already taken, choose different one.";
            return false;
        }
        accountMessage = "Account successfuly created.";
        return true;
    }

    public String verifyChangedParameter(String hidden, String[] params, EntityManagerFactory emf) {
        // params: name, surname, login, password, repassword
        switch (hidden) {
            case "name":
                if (params[0] == null || params[0].isEmpty()) {
                    accountMessage = "Name cannot be empty.";
                    return null;
                }
                return params[0];

            case "surname":
                if (params[1] == null || params[1].isEmpty()) {
                    accountMessage = "Surname cannot be empty.";
                    return null;
                }
                return params[1];
            case "login":
                if (params[2] == null || params[2].isEmpty()) {
                    accountMessage = "New login cannot be empty.";
                    return null;
                }
                UserAccount acc = (UserAccount) databaseService.getUserAccountEntity(params[2], "", emf);
                if (acc != null) {
                    accountMessage = "Account with this login already exists, choose different one.";
                    return null;
                }
                return params[2];
            case "password":
                if (params[3] == null || params[3].isEmpty()) {
                    accountMessage = "New password cannot be empty.";
                    return null;
                }
                if (params[4] == null || params[4].isEmpty()) {
                    accountMessage = "Password retype field cannot be empty.";
                    return null;
                }
                if (!params[3].equals(params[4])) {
                    accountMessage = "Password and password retype field value does not match.";
                    return null;
                }
                return params[3];

            default:
                return null;
        }
    }
}
