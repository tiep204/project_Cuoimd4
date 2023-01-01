package ra.model.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.model.entity.Cart;
import ra.model.entity.Orders;

import java.util.List;

public interface OrderService {
    Orders saveOrder(Orders orders);
    void deleteOrder(int orderId);
    Orders findById(int orderId);
    List<Orders> findAllOrder();
    Page<Orders> sortByNameAndPagination(Pageable pageable);
    List<Orders> findByUsers_UserId(int userId);
}
