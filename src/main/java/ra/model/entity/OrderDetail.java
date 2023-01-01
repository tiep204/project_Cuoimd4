package ra.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.criterion.Order;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "OrderDetail")
@Data
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderDetailId")
    private int orderDetailId;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "totalPrice")
    private float totalPrice;
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "orderId")
    private Orders orders;
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "productId")
    private Product product;
    @Column(name = "orderStatus")
    private boolean orderStatus;
    @Column(name = "createDate")
    private Date cteateDate;
    @Column(name = "price")
    private float price;

}
