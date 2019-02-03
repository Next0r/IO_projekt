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
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "CLIENTCAR_ID")
    private ClientCar clientCar;
    
    @OneToMany(fetch=FetchType.LAZY)
    @JoinColumn(name = "PRODUCTS")
    private List<Product> products;
     
    @Basic(optional = true)
    @Column(name = "CLIENT_DESCRIPTION", length = 1000)
    private String clnDescription;

    public AssistanceRequest() {}
    
    public AssistanceRequest(ClientCar clientCar, List<Product> product, String clnDescription){
       this.clientCar = clientCar;
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


    public ClientCar getClientCar() {
        return clientCar;
    }

    public void setClientCar(ClientCar clientCar) {
        this.clientCar = clientCar;
    }

    public List<Product> getProduct() {
        return products;
    }

    public void setProduct(List<Product> products) {
        this.products = products;
    }

}
