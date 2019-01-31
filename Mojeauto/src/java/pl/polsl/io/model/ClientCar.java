package pl.polsl.io.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @version @author
 */
@Entity
public class ClientCar {

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CLIENT_CAR_ID")
    private Integer clientCarID;

    @Basic(optional = false)
    @Column(name = "BRAND")
    private String brand;

    @Basic(optional = false)
    @Column(name = "MODEL")
    private String model;

    @Basic(optional = false)
    @Column(name = "LICENSE_NUMBER")
    private String licenseNumber;

    @Basic(optional = false)
    @Column(name = "PRODUCTION_YEAR")
    private Integer productionYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLIENT_ID")
    private Client owner;

    public ClientCar(){};
    
    public ClientCar(String brand, String model, String licenseNumber, Integer productionYear, Client owner) {
        this.brand = brand;
        this.model = model;
        this.licenseNumber = licenseNumber;
        this.productionYear = productionYear;
        this.owner = owner;
    }

}
