package ra.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Product")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productId")
    private int productId;
    @Column(name = "productName")
    private String productName;
    @Column(name = "productTitle")
    private String productTitle;
    @Column(name = "productImage")
    private String productImage;
    @Column(name = "productPrice")
    private float productPrice;
    @Column(name = "productDateAdd")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date productDateAdd;
    @Column(name = "productQuantity")
    private int productQuantity;
    @Column(name = "productDecription")
    private String productDecription;
    @Column(name = "productStatus")
    private boolean productStatus;
    @ManyToOne
    @JoinColumn(name = "catalogId")
    private Catalog catalog;
    @OneToMany(mappedBy = "product")
    private List<Image>listImage = new ArrayList<>();
}
