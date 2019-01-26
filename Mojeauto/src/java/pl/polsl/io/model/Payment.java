package pl.polsl.io.model;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    
    @Basic(optional = false)
    @Column(name = "METHOD")
    private String paymentMethod;
    
    @Basic(optional = false)
    @Column(name = "FINALIZED")
    private Boolean finalized;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "PAYMENT_DATE")
    private Date paymentDate; 
}
