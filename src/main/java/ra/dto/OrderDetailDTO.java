package ra.dto;

import lombok.Data;
import ra.model.entity.Product;

import java.util.Date;

@Data
public class OrderDetailDTO {
    private int orderDetailId;
    private int quantity;
    private float totalPrice;
    private float price;
    private Date cteateDate;
    private String productName;
}
