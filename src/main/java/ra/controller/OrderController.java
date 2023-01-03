package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ra.dto.OrderDTO;
import ra.dto.OrderDetailDTO;
import ra.dto.requestss.OrderRequest;
import ra.model.entity.Cart;
import ra.model.entity.OrderDetail;
import ra.model.entity.Orders;
import ra.model.entity.Users;
import ra.model.service.*;
import ra.security.CustomUserDetails;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("api/v1/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderDetailService orderDetailService;

    @GetMapping("/admin/getAllOrder")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> getAllOrder() {
        List<OrderDTO> listOrderDTO = new ArrayList<>();
        List<Orders> listOrder = orderService.findAllOrder();
        for (Orders orders : listOrder) {
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setOrderId(orders.getOrderId());
            orderDTO.setTotalAmount(orders.getTotalAmount());
            orderDTO.setCreated(orders.getCreated());
            orderDTO.setOrdersStatus(orders.isOrdersStatus());
            orderDTO.setEmail(orders.getEmail());
            orderDTO.setPhone(orders.getPhone());
            orderDTO.setAddress(orders.getAddress());
            orderDTO.setFullName(orders.getFullName());
            orderDTO.setNote(orders.getNote());
            for (OrderDetail orderDetail : orders.getListOrderDetail()) {
                OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
                orderDetailDTO.setOrderDetailId(orderDetail.getOrderDetailId());
                orderDetailDTO.setQuantity(orderDetail.getQuantity());
                orderDetailDTO.setTotalPrice(orderDetail.getTotalPrice());
                orderDetailDTO.setPrice(orderDetail.getPrice());
                orderDetailDTO.setCteateDate(orderDetail.getCteateDate());
                orderDetailDTO.setProductName(orderDetail.getProduct().getProductName());
                orderDTO.getListOrderDetail().add(orderDetailDTO);
            }
            listOrderDTO.add(orderDTO);
        }
        return ResponseEntity.ok(listOrder);
    }

    @GetMapping("/user/getAllOrder")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getAlluserOrder() {
        List<OrderDTO> listOrderDTO = new ArrayList<>();
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Orders> listOrder = orderService.findByUsers_UserId(customUserDetails.getUserId());
        for (Orders orders : listOrder) {
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setOrderId(orders.getOrderId());
            orderDTO.setTotalAmount(orders.getTotalAmount());
            orderDTO.setCreated(orders.getCreated());
            orderDTO.setOrdersStatus(orders.isOrdersStatus());
            orderDTO.setEmail(orders.getEmail());
            orderDTO.setPhone(orders.getPhone());
            orderDTO.setAddress(orders.getAddress());
            orderDTO.setFullName(orders.getFullName());
            orderDTO.setNote(orders.getNote());
            for (OrderDetail orderDetail : orders.getListOrderDetail()) {
                OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
                orderDetailDTO.setOrderDetailId(orderDetail.getOrderDetailId());
                orderDetailDTO.setQuantity(orderDetail.getQuantity());
                orderDetailDTO.setTotalPrice(orderDetail.getTotalPrice());
                orderDetailDTO.setPrice(orderDetail.getPrice());
                orderDetailDTO.setCteateDate(orderDetail.getCteateDate());
                orderDetailDTO.setProductName(orderDetail.getProduct().getProductName());
                orderDTO.getListOrderDetail().add(orderDetailDTO);
            }
            listOrderDTO.add(orderDTO);
        }
        return ResponseEntity.ok(listOrder);
    }

    @PostMapping("/user/createOrder")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Orders orders = new Orders();

        Users users = userService.findById(customUserDetails.getUserId());
        orders.setAddress(orderRequest.getAddress());
        orders.setEmail(orderRequest.getEmail());
        orders.setCreated(new Date());
        orders.setFullName(orderRequest.getFullName());
        orders.setPhone(orderRequest.getPhone());
        orders.setUsers(users);
       try {
           orders = orderService.saveOrder(orders);
           float totalAmount = 0;
           for (int i = 0; i < orderRequest.getListCart().size(); i++) {
               Cart cart = cartService.findById(orderRequest.getListCart().get(i));
               OrderDetail orderDetail = new OrderDetail();
               orderDetail.setPrice(cart.getProduct().getProductPrice());
               orderDetail.setQuantity(orderRequest.getListQuantity().get(i));
               orderDetail.setTotalPrice(orderDetail.getPrice()*orderDetail.getQuantity());
               orderDetail= orderDetailService.saveOrderDetail(orderDetail);
               orders.getListOrderDetail().add(orderDetail);
               totalAmount+=orderDetail.getTotalPrice();
           }
           orders.setTotalAmount(totalAmount);
           orderService.saveOrder(orders);
           return ResponseEntity.ok("Chuc mung ban da mua thanh cong");
       }catch (Exception e){
           e.printStackTrace();
           return ResponseEntity.ok("Ban da mua that bai");
       }
    }



}
