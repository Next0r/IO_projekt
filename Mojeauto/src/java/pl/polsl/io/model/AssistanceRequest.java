package pl.polsl.io.model;

import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @version
 * @author 
 */
@Entity
@Table(name = "ASSISTANCE_REQUEST")
public class AssistanceRequest {

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REQUEST_ID")
    private Integer requestID;
    
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "CLIENT_ID")
    private Client client;
    
    @OneToMany(fetch=FetchType.LAZY)
    @JoinColumn(name = "PRODUCTS")
    private List<Product> products;
     
    @Basic(optional = true)
    @Column(name = "CLIENT_DESCRIPTION", length = 1000)
    private String clnDescription;

    public AssistanceRequest() {}
    
    public AssistanceRequest(Client client, List<Product> product, String clnDescription){
       this.client = client;
       this.products = product;
       this.clnDescription = clnDescription;
    }

    public String getClnDescription() {
        return clnDescription;
    }

    public void setClnDescription(String clnDescription) {
        this.clnDescription = clnDescription;
    }
    
    public Integer getRequestID() {
        return requestID;
    }

    public void setRequestID(Integer requestID) {
        this.requestID = requestID;
    }


    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<Product> getProduct() {
        return products;
    }

    public void setProduct(List<Product> products) {
        this.products = products;
    }
    
    
    
}
