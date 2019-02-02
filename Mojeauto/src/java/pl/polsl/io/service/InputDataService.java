package pl.polsl.io.service;

import java.util.Calendar;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.UserTransaction;
import pl.polsl.io.model.Client;
import pl.polsl.io.model.ClientCar;
import pl.polsl.io.model.UserAccount;

/**
 *
 * @author Michal
 */
public class InputDataService {

    private Integer minimumVehicleProductionYear;
    private String resultMessage;
    private DatabaseService databaseService;

    public InputDataService(DatabaseService databaseService) {
        this.databaseService = databaseService;
        minimumVehicleProductionYear = 1900;
    }

    public void setMinimumVehicleProductionYear(Integer minimumVehicleProductionYear) {
        this.minimumVehicleProductionYear = minimumVehicleProductionYear;
    }

    public String getResultMessage() {
        return this.resultMessage;
    }

    public Boolean isClientDataComplete(Client cln) {
        if (cln.getName().equals("_") || cln.getSurname().equals("_")) {
            resultMessage = "You still have to set up your personal details.";
            return false;
        }
        return true;
    }

    public String nullStringTrim(String string) {
        if (string == null) {
            return string;
        } else {
            return string.trim();
        }
    }

    public Integer getCorrectProductionYear(String pYear) {
        if (pYear == null) {
            return null;
        }
        Integer productionYear;
        try {
            productionYear = Integer.valueOf(pYear);
        } catch (NumberFormatException e) {
            return null;
        }
        if (productionYear > Calendar.getInstance().get(Calendar.YEAR) || productionYear < minimumVehicleProductionYear) {
            return null;
        }
        return productionYear;
    }

    public Boolean isCorrectClientData(String name, String surname, String phoneNumber) {
        if (name == null || surname == null || phoneNumber == null) {
            resultMessage = "One of required fields is empty.";
            return false;
        }
        if (name.isEmpty() || surname.isEmpty() || phoneNumber.isEmpty()) {
            resultMessage = "One of required fields is empty.";
            return false;
        }
        if (phoneNumber.split("\\s+").length > 1) {
            resultMessage = "Insert phone number as single string of characters.";
            return false;
        }
        if (phoneNumber.length() > 10 || phoneNumber.length() < 9) {
            resultMessage = "Phone number is incorrect.";
            return false;
        }
        return true;
    }

    public Boolean isCorrectVehicleData(String brand, String model, String licenseNumber, EntityManagerFactory emf, Boolean existanceCheck) throws Exception {
        if (brand == null || model == null || licenseNumber == null) {
            resultMessage = "One of required fields is empty.";
            return false;
        }
        if (brand.isEmpty() || model.isEmpty() || licenseNumber.isEmpty()) {
            resultMessage = "One of required fields is empty.";
            return false;
        }
        if (licenseNumber.split("\\s+").length > 1) {
            resultMessage = "Insert license number as single string of characters.";
            return false;
        }
        if (existanceCheck == true) {
            ClientCar car = (ClientCar) databaseService.getClientCarByLicenseNumber(licenseNumber, emf);
            if (car != null) {
                resultMessage = "Vehicle with such license number already exists. If you consider this an error - try to contact us.";
                return false;
            }
        }
        return true;
    }

    public void setResultMessageAttribute(String message, HttpServletRequest request) {
        if (message == null) {
            request.getSession().setAttribute("resultMessage", resultMessage);
        } else {
            request.getSession().setAttribute("resultMessage", message);
        }
    }

    public Boolean isCorrectLogInData(String login, String password, EntityManagerFactory emf, UserTransaction utx) throws Exception {

        UserAccount acc;
        if (login == null || password == null) {
            resultMessage = "Login and password cannot be empty.";
            return false;
        }
        if (login.isEmpty() || password.isEmpty()) {
            resultMessage = "Login and password cannot be empty.";
            return false;
        }
        acc = (UserAccount) databaseService.getUserAccountEntity(login, password, emf);
        if (acc == null) {
            resultMessage = "Incorrect login or password.";
            return false;
        }
        return true;
    }

    public Boolean isCorrectRegisterData(String login, String password, String repassword, EntityManagerFactory emf, UserTransaction utx) throws Exception {
        UserAccount acc;
        if (login == null || password == null || repassword == null) {
            resultMessage = "One of required fields is empty.";
            return false;
        }
        if (login.isEmpty() || password.isEmpty() || repassword.isEmpty()) {
            resultMessage = "One of required fields is empty.";
            return false;
        }
        if (!password.equals(repassword)) {
            resultMessage = "Password and password retype field value does not match.";
            return false;
        }
        acc = (UserAccount) databaseService.getUserAccountEntity(login, "", emf);
        if (acc != null) {
            resultMessage = "This login is already taken, choose different one.";
            return false;
        }
        resultMessage = "Account successfully created.";
        return true;
    }

    public String verifyChangedParameter(String hidden, String[] params, EntityManagerFactory emf) throws Exception {
        // params: name, surname, login, password, repassword
        switch (hidden) {
            case "name":
                if (params[0] == null || params[0].isEmpty()) {
                    resultMessage = "Name cannot be empty.";
                    return null;
                }
                return params[0];

            case "surname":
                if (params[1] == null || params[1].isEmpty()) {
                    resultMessage = "Surname cannot be empty.";
                    return null;
                }
                return params[1];
            case "login":
                if (params[2] == null || params[2].isEmpty()) {
                    resultMessage = "New login cannot be empty.";
                    return null;
                }
                UserAccount acc = (UserAccount) databaseService.getUserAccountEntity(params[2], "", emf);
                if (acc != null) {
                    resultMessage = "Account with this login already exists, choose different one.";
                    return null;
                }
                return params[2];
            case "password":
                if (params[3] == null || params[3].isEmpty()) {
                    resultMessage = "New password cannot be empty.";
                    return null;
                }
                if (params[4] == null || params[4].isEmpty()) {
                    resultMessage = "Password retype field cannot be empty.";
                    return null;
                }
                if (!params[3].equals(params[4])) {
                    resultMessage = "Password and password retype field value does not match.";
                    return null;
                }
                return params[3];

            default:
                return null;
        }
    }

    public void generateErrorResultMessage() {
        resultMessage = "Oops! Something went wrong or somebody just stolen our database! Maybe make yourself a cup of tea and then try again?";
    }

}
