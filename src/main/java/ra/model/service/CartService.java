package ra.model.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.model.entity.Cart;
import ra.model.entity.Catalog;

import java.util.List;

public interface CartService {
    Cart saveCart(Cart cart);
    List<Cart> getAllCart();
    void deleteCart(int cartId);
    Cart findById(int cartId);
    List<Cart> findAllCart(int id);
    Page<Cart> sortByNameAndPagination(Pageable pageable);
    List<Cart> findUserById(int userId);


}
