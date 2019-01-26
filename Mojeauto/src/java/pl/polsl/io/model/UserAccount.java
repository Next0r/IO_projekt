package pl.polsl.io.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @version
 * @author 
 */
@Entity
@Table(name = "USER_ACCOUNT")
public class UserAccount {
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACCOUNT_ID")
    private Integer accountID;
    
    @Basic(optional = false)
    @Column(name = "LOGIN")
    private String login;
    
    @Basic(optional = false)
    @Column(name = "PASSWORD")
    private String password;
    
    public UserAccount(){
    }
    
    public UserAccount(String login, String password) {
        this.login = login;
        this.password = password;
    }
    
    public void setLogin(String login){
        this.login = login;
    }
    
    public void setPassword(String password){
        this.password = password;
    }
    
}
