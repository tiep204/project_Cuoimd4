package ra.dto;

import lombok.Data;

@Data
public class CartDTO {
    private int cartId;
    private String productName;
    private String productImage;
    private float productPrice;
    private int quantity;
    private float totalPrice;
}
