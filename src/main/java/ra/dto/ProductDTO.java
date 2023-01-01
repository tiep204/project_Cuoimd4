package ra.dto;

import lombok.Data;
import ra.model.entity.Catalog;
import ra.model.entity.Image;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ProductDTO {
    private int productId;
    private String productName;
    private String productTitle;
    private String productImage;
    private float productPrice;
    private Date productDateAdd;
    private int productQuantity;
    private String productDecription;
    private boolean productStatus;
    private String catalogName;
    private List<Image> listImage = new ArrayList<>();
}
