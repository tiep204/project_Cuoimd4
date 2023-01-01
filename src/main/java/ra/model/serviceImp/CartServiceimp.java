package ra.model.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ra.model.entity.Cart;
import ra.model.repository.CartRepository;
import ra.model.service.CartService;

import java.net.CacheResponse;
import java.util.List;
@Service
public class CartServiceimp implements CartService {
    @Autowired
    private CartRepository cartRepository;
    @Override
    public Cart saveCart(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public List<Cart> getAllCart() {
        return cartRepository.findAll();
    }

    @Override
    public void deleteCart(int cartId) {
        cartRepository.deleteById(cartId);

    }

    @Override
    public Cart findById(int cartId) {
        return cartRepository.findById(cartId).get();
    }

    @Override
    public List<Cart> findAllCart(int id) {
        return null;
    }

    @Override
    public Page<Cart> sortByNameAndPagination(Pageable pageable) {
        return cartRepository.findAll(pageable);
    }

    @Override
    public List<Cart> findUserById(int userId) {
        return cartRepository.findByUsers_UserId(userId);
    }
}
