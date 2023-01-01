package ra.model.serviceImp;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ra.model.entity.Product;
import ra.model.repository.ProductRepository;
import ra.model.service.ProductService;

import java.util.List;

@Service
public class ProductServiceImp implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(int productId) {
        return productRepository.findById(productId).get();
    }

    @Override
    public Product saveOfUpdate(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void delete(int productId) {
        productRepository.deleteById(productId);
    }

    @Override
    public List<Product> searchByName(String productName) {
        return null;
    }

    @Override
    public List<Product> searchProductByCatalogId(int catalogId) {
        return productRepository.findByCatalog_CatalogId(catalogId);
    }

    @Override
    public Page<Product> sortByNameAndPagination(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
}
