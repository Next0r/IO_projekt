package pl.polsl.io.model;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @version
 * @author 
 */
@Entity
public class Payment {
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PAYMENT_ID")
    private Integer paymentID;
    
    @Basic(optional = false)
    @Column(name = "AMOUNT")
    private Double amount;
    
    @Basic(optional = true)
    @Column(name = "METHOD")
    private String paymentMethod;
    
    @Basic(optional = false)
    @Column(name = "FINALIZED")
    private Boolean finalized;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "PAYMENT_DATE")
    private Date paymentDate; 
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLIENT_ID")
    private Client client;
    
    public Payment(){};
    
    public Payment(Double amount, String paymentMethod, Boolean finalized, Date paymentDate, Client client){
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.finalized = finalized;
        this.paymentDate = paymentDate;
        this.client = client;
    }
    
}
