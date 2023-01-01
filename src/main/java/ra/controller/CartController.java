package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.Repository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ra.dto.CartDTO;
import ra.dto.requestss.CartRequest;
import ra.model.entity.Cart;
import ra.model.entity.Product;
import ra.model.service.CartService;
import ra.model.service.ProductService;
import ra.model.service.UserService;
import ra.security.CustomUserDetails;
import ra.security.CustomUserDetailsService;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("http://localhost:8080")
@RequestMapping("api/v1/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;

    @GetMapping("/getAllCart")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getAllCart() {
        List<CartDTO> listCartDTO = new ArrayList<>();
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Cart> listCart = cartService.findUserById(customUserDetails.getUserId());
        for (Cart cart : listCart) {
            CartDTO cartDTO = new CartDTO();
            cartDTO.setCartId(cart.getCartId());
            cartDTO.setProductName(cart.getProduct().getProductName());
            cartDTO.setProductImage(cart.getProduct().getProductImage());
            cartDTO.setProductPrice(cart.getProduct().getProductPrice());
            cartDTO.setQuantity(cart.getQuantity());
            cartDTO.setTotalPrice(cart.getTotalPrice());
            listCartDTO.add(cartDTO);
        }
        return ResponseEntity.ok(listCartDTO);
    }

    @GetMapping("/{cartId}")
    public Cart getByIdCart(@PathVariable("cartId") int cartId) {
        return cartService.findById(cartId);
    }

    @PostMapping("/createCart")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createCartt(@RequestBody CartRequest cartRequest) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean check = true;
        boolean checkExit = false;
        Cart cart = new Cart();
        List<Cart> listCart = cartService.findAllCart(customUserDetails.getUserId());
        try {
            if (listCart != null) {

                for (Cart cartt : listCart) {
                    if (cartt.getProduct().getProductId() == cartRequest.getProductId()) {
                        checkExit = true;
                        cart = cartt;
                        break;
                    }
                }
                if (checkExit) {
                    cart.setQuantity(cart.getQuantity() + cartRequest.getQuantity());
                    cartService.saveCart(cart);
                } else {
                    cart.setQuantity(cartRequest.getQuantity());
                    cart.setProduct(productService.findById(cartRequest.getProductId()));
                    cart.setTotalPrice(cart.getProduct().getProductPrice() * cart.getQuantity());
                    cart.setUsers(userService.findById(customUserDetails.getUserId()));
                    cartService.saveCart(cart);
                }
            } else {
                cart.setQuantity(cartRequest.getQuantity());
                cart.setProduct(productService.findById(cartRequest.getProductId()));
                cart.setTotalPrice(cart.getProduct().getProductPrice() * cart.getQuantity());
                cart.setUsers(userService.findById(customUserDetails.getUserId()));
                cartService.saveCart(cart);
            }
        } catch (Exception e) {
            check = false;
            e.printStackTrace();
        }

        if (check) {
            return ResponseEntity.ok("bạn đã thêm sản phẩm thành công");
        } else {
            return ResponseEntity.ok("đã có lỗi trong quá trình sử lý");
        }
    }

    @PutMapping("updateCart")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateCart(@RequestParam("quantity") int quantity, @RequestParam("cartId") int cartId) {
        try {
            Cart cart = cartService.findById(cartId);
            if (quantity > 0) {
                cart.setQuantity(quantity);
                cart.setTotalPrice(cart.getProduct().getProductPrice() * cart.getQuantity());
                cartService.saveCart(cart);
            } else {
                cartService.deleteCart(cartId);
            }
            return ResponseEntity.ok("banj ddax caapj nhaatj thanh cong cart");
        } catch (Exception e) {
            return ResponseEntity.ok("banj suar gior hangf daxd thatas bai");
        }
    }

}
