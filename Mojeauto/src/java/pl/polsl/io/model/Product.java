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
 * @version @author
 */
@Entity
public class Product {

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_ID")
    private Integer productID;

    @Basic(optional = true)
    @Temporal(TemporalType.DATE)
    @Column(name = "EXPIRATION_DATE")
    private Date expirationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OWNER_ID")
    private UserAccount ownerAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_TYPE_ID")
    private ProductType productType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAYMENT_ID")
    private Payment payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CAR_ID")
    private ClientCar clientCar;

    public Product(){};
    
    public Product(Date expirationDate, UserAccount ownerAccount, ProductType productType, Payment payment, ClientCar clientCar){
        this.expirationDate = expirationDate;
        this.ownerAccount = ownerAccount;
        this.productType = productType;
        this.payment = payment;
        this.clientCar = clientCar;
    }
    
    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public UserAccount getOwnerAccount() {
        return ownerAccount;
    }

    public void setOwnerAccount(UserAccount ownerAccount) {
        this.ownerAccount = ownerAccount;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public ClientCar getClientCar() {
        return clientCar;
    }

    public void setClientCar(ClientCar clientCar) {
        this.clientCar = clientCar;
    }
    
    

}
