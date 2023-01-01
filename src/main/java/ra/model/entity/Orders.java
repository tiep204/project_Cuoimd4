package ra.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Orders")
@Data
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderId")
    private int orderId;
    @Column(name = "totalAmount")
    private float totalAmount;
    @Column(name = "created")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date created;
    @Column(name = "ordersStatus")
    private boolean ordersStatus;
    @Column(name = "email")
    private String email;
    @Column(name = "phone",columnDefinition = "nvarchar(11)")
    private String phone;
    @Column(name = "address",columnDefinition = "nvarchar(50)")
    private String address;
    @Column(name = "fullName",columnDefinition = "nvarchar(50)")
    private String fullName;
    @Column(name = "note",columnDefinition = "text")
    private String note;
    @ManyToOne
    @JoinColumn(name = "userId")
    private Users users;
    @OneToMany(mappedBy = "orders")
    List<OrderDetail> listOrderDetail = new ArrayList<>();
}
