package pl.polsl.io.model;

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
public class SingleService extends ProductType {
    
    public SingleService() {
        
    }
    
    public SingleService(String name, String description, Double price){
        super(name,description,price);
    }
            
}
