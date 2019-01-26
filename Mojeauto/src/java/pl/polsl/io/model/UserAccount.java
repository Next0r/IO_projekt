package pl.polsl.io.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @version
 * @author 
 */
@Entity
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
    
}
