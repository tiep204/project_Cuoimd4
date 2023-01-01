package ra.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "Cart")
@Data
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cartId")
    private int cartId;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "totalPrice")
    private float totalPrice;
    @ManyToOne
    @JoinColumn(name = "userId")
    @JsonIgnore
    private Users users;
    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;
}
