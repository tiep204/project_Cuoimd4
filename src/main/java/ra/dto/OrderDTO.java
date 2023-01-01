package ra.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class OrderDTO {
    private int orderId;
    private float totalAmount;
    private Date created;
    private boolean ordersStatus;
    private String email;
    private String phone;
    private String address;
    private String fullName;
    private String note;
    private List<OrderDetailDTO> listOrderDetail = new ArrayList<>();

}
