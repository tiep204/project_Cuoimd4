package ra.model.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ra.model.entity.Orders;
import ra.model.repository.OrderRepository;
import ra.model.service.OrderService;

import java.util.List;

@Service
public class OrderServiceImp implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Override
    public Orders saveOrder(Orders orders) {
        return orderRepository.save(orders);
    }

    @Override
    public void deleteOrder(int orderId) {
        orderRepository.deleteById(orderId);
    }

    @Override
    public Orders findById(int orderId) {
        return orderRepository.findById(orderId).get();
    }

    @Override
    public List<Orders> findAllOrder() {
        return orderRepository.findAll();
    }


    @Override
    public Page<Orders> sortByNameAndPagination(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Override
    public List<Orders> findByUsers_UserId(int userId) {
        return orderRepository.findByUsers_UserId(userId);
    }
}
