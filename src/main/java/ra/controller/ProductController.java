package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ra.dto.ProductDTO;
import ra.dto.requestss.ProductRequest;
import ra.model.entity.Catalog;
import ra.model.entity.Image;
import ra.model.entity.Product;
import ra.model.service.CatalogService;
import ra.model.service.ImageService;
import ra.model.service.ProductService;

import java.util.*;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("api/v1/product")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CatalogService catalogService;
    @Autowired
    private ImageService imageService;

    @GetMapping("/getAllProduct")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<ProductDTO> getAllProduct() {
        List<ProductDTO> listProductDTO = new ArrayList<>();
        List<Product> listProduct = productService.findAll();
        for (Product pro : listProduct) {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setProductId(pro.getProductId());
            productDTO.setProductName(pro.getProductName());
            productDTO.setProductTitle(pro.getProductTitle());
            productDTO.setProductImage(pro.getProductImage());
            productDTO.setProductPrice(pro.getProductPrice());
            productDTO.setProductDateAdd(pro.getProductDateAdd());
            productDTO.setProductQuantity(pro.getProductQuantity());
            productDTO.setProductDecription(pro.getProductDecription());
            productDTO.setProductStatus(pro.isProductStatus());
            productDTO.setCatalogName(pro.getCatalog().getCatalogName());
            productDTO.getListImage().addAll(pro.getListImage());
            listProductDTO.add(productDTO);
        }
        return listProductDTO;
    }

    @GetMapping("/{productId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ProductDTO getProductById(@PathVariable("productId") int productId) {
        ProductDTO productDTO = new ProductDTO();
        Product product = productService.findById(productId);
        productDTO.setProductId(product.getProductId());
        productDTO.setProductName(product.getProductName());
        productDTO.setProductTitle(product.getProductTitle());
        productDTO.setProductImage(product.getProductImage());
        productDTO.setProductPrice(product.getProductPrice());
        productDTO.setProductDateAdd(product.getProductDateAdd());
        productDTO.setProductQuantity(product.getProductQuantity());
        productDTO.setProductDecription(product.getProductDecription());
        productDTO.setProductStatus(product.isProductStatus());
        productDTO.setCatalogName(product.getCatalog().getCatalogName());
        productDTO.getListImage().addAll(product.getListImage());
        return productDTO;
    }

    @PostMapping("createProduct")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> saveProduct(@RequestBody ProductRequest product) {
        try {
            Catalog catalog = catalogService.findById(product.getCatalog());
            Product proNew = new Product();
            proNew.setCatalog(catalog);
            proNew.setProductName(product.getProductName());
            proNew.setProductTitle(product.getProductTitle());
            proNew.setProductImage(product.getProductImage());
            proNew.setProductPrice(product.getProductPrice());
            proNew.setProductDateAdd(new Date());
            proNew.setProductQuantity(product.getProductQuantity());
            proNew.setProductDecription(product.getProductDecription());
            proNew.setProductStatus(true);
            proNew = productService.saveOfUpdate(proNew);
            for (String str : product.getListImage()) {
                Image image = new Image();
                image.setImageURL(str);
                image.setImageStatus(true);
                image.setProduct(proNew);
                imageService.saveOfUpdate(image);
            }
            return ResponseEntity.ok("Thêm mới Product thành công😘");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.ok("đã có lỗi trong quá trình sử lý!!! vui lòng ngủ một giấc rồi quay lại nhe😛");
        }
    }
    @PutMapping("/updateProduct/{productId}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> updateProduct(@PathVariable("productId") int productId,@RequestBody ProductRequest product){
        try {
            Catalog catalog = catalogService.findById(product.getCatalog());
            Product proUpdate = productService.findById(productId);
            proUpdate.setCatalog(catalog);
            proUpdate.setProductName(product.getProductName());
            proUpdate.setProductTitle(product.getProductTitle());
            proUpdate.setProductImage(product.getProductImage());
            proUpdate.setProductPrice(product.getProductPrice());
            proUpdate.setProductDateAdd(new Date());
            proUpdate.setProductQuantity(product.getProductQuantity());
            proUpdate.setProductDecription(product.getProductDecription());
            proUpdate.setProductStatus(true);
            proUpdate = productService.saveOfUpdate(proUpdate);
            for (Image image :proUpdate.getListImage()) {
                imageService.delete(image.getImageId());
            }
            for (String str :product.getListImage()) {
                Image image = new Image();
                image.setImageURL(str);
                image.setImageStatus(true);
                image.setProduct(proUpdate);
                 imageService.saveOfUpdate(image);
//                proNew.getListImage().add(image);
            }
            return ResponseEntity.ok("Bạn đã cap nhaật thành công");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.ok("Có lỗi trong quá trình xử lý vui lòng thử lại!");
        }
    }

    @DeleteMapping("/delete/{producId}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteproduct(@PathVariable("producId") int productId) {
        try{
            Product product  = productService.findById(productId);
            for (Image image:product.getListImage()){
                imageService.delete(image.getImageId());
            }
            productService.delete(productId);
            return ResponseEntity.ok("Đã xóa thành công ");
        }catch (Exception e){
            return ResponseEntity.ok("Chưa xóa được kiểm tra lại ");
        }

    }


    @GetMapping("/search")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<Product> searchProduct(@RequestParam("productName") String productName){
        return productService.searchByName(productName);
    }
    @GetMapping("/getPaggingAndSortByName")
    public ResponseEntity<Map<String,Object>> getPaggingAndSortByName(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam String direction){
        Sort.Order order;
        if (direction.equals("asc")){
            order=new Sort.Order(Sort.Direction.ASC,"productName");
        }else{
            order=new Sort.Order(Sort.Direction.DESC,"productName");
        }
        Pageable pageable = PageRequest.of(page,size,Sort.by(order));
        Page<Product> productPage = productService.sortByNameAndPagination(pageable);
        Map<String,Object> data = new HashMap<>();
        data.put("catalog",productPage.getContent());
        data.put("total",productPage.getSize());
        data.put("totalItems",productPage.getTotalElements());
        data.put("totalPages",productPage.getTotalPages());
        return  new ResponseEntity<>(data, HttpStatus.OK);
    }
}
