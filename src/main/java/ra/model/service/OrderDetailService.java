package ra.model.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.model.entity.OrderDetail;
import ra.model.entity.Orders;

import java.util.List;

public interface OrderDetailService {
    OrderDetail saveOrderDetail(OrderDetail orderDetail);

}
