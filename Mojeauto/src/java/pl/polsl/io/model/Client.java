package pl.polsl.io.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @version
 * @author 
 */
@Entity
@Table(name = "CLIENT")
public class Client {
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CLIENT_ID")
    private Integer clientID;
    
    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;
    
    @Basic(optional = false)
    @Column(name = "SURNAME")
    private String surname;
    
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ACCOUNT_ID")
    private UserAccount userAccount;
    
    public Client(){
    }
    
    public Client(String name, String surname, UserAccount account){
        this.name = name;
        this.surname = surname;
        this.userAccount = account;
    }
    
}
