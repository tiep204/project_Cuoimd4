package ra.model.entity;

import lombok.Data;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.persistence.*;

@Entity
@Table(name = "Catalog")
@Data

public class Catalog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "catalogId")
    private int catalogId;
    @Column(name = "catalogName")
    private String catalogName;
    @Column(name = "catalogStatus")
    private boolean catalogStatus;

}
