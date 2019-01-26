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
 * @version
 * @author 
 */
@Entity
public class Submission {

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SUBMISSION_ID")
    private Integer submissionID;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "CAR_ID")
    private ClientCar clientCar;
}
