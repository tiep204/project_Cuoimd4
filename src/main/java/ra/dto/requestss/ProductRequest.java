package ra.dto.requestss;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ProductRequest {
    private int productId;
    private String productName;
    private String productTitle;
    private String productImage;
    private float productPrice;
    private Date productDateAdd;
    private int productQuantity;
    private String productDecription;
    private boolean productStatus;
    private int catalog;
    private List<String> listImage = new ArrayList<>();
}
