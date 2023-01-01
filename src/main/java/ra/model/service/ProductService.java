package ra.model.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.model.entity.Catalog;
import ra.model.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> findAll();
    Product findById(int productId);
    Product saveOfUpdate(Product product);
    void delete(int productId);
    List<Product> searchByName(String productName);
    List<Product> searchProductByCatalogId(int catalogId);
    Page<Product> sortByNameAndPagination(Pageable pageable);

}
