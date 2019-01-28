package pl.polsl.io.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @version
 * @author 
 */
@Entity
public class Package extends ProductType {
    
    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "PACKAGE_ID")
    private List<SingleService> singleServices = new ArrayList<>();
    
    public Package() {
    }
    
    public Package(String name, String description, Double price, List<SingleService> services) {
        super(name,description,price);
        this.singleServices = new ArrayList<>(services);
    }

}
