package ra.dto.requestss;

import lombok.Data;
import org.apache.catalina.LifecycleState;
import ra.model.entity.Cart;

import java.util.*;

@Data
public class OrderRequest {
    private List<Integer> listCart = new ArrayList<>();
    private List<Integer> listQuantity = new ArrayList<>();
    private String address;
    private String email;
    private String fullName;
    private String phone;

}
