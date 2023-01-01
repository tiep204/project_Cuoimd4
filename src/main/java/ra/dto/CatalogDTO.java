package ra.dto;

import lombok.Data;
import ra.model.entity.Product;

import java.util.ArrayList;
import java.util.List;
@Data
public class CatalogDTO {
private int catalogId;
private String catalogName;
private boolean catalogStatus;
private List<Product> listProduct = new ArrayList<>();
}
