package pl.polsl.io.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
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
@DiscriminatorColumn(name="REF_TYPE")
public abstract class ProductType {
    
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_TYPE_ID")
    private Integer productTypeID;
    
    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;
    
    @Basic(optional = false)
    @Column(name = "DESCRIPTION", length = 1000)
    private String description;
    @Basic(optional = false)
    @Column(name = "PRICE")
    private Double price;
    
    public ProductType(){
        
    }
    
    public ProductType(String name, String description, Double price){
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public Integer getProductTypeID() {
        return productTypeID;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }
    
    
}
