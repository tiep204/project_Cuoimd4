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
import ra.dto.CatalogDTO;
import ra.model.entity.Catalog;
import ra.model.service.CatalogService;
import ra.model.service.ProductService;
import ra.payload.response.MessageResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("api/v1/catalog")
public class CatalogController {
    @Autowired
    private CatalogService catalogService;

    @Autowired
    private ProductService productService;

    @GetMapping("getAllCatalog")
    @PreAuthorize(" hasRole('MODERATOR') or hasAnyRole('ADMIN')")
    public List<Catalog> getAllCatalog(){
        return catalogService.findAll();
    }
    @GetMapping("/{catalogId}")
    @PreAuthorize(" hasRole('MODERATOR') or hasAnyRole('ADMIN')")
    public CatalogDTO getCatalogById(@PathVariable("catalogId") int catalogId){
        CatalogDTO catalogDTO = new CatalogDTO();
        Catalog catalog = catalogService.findById(catalogId);
        catalogDTO.setCatalogId(catalog.getCatalogId());
        catalogDTO.setCatalogName(catalog.getCatalogName());
        catalogDTO.setCatalogStatus(catalog.isCatalogStatus());
        catalogDTO.setListProduct(productService.searchProductByCatalogId(catalogId));
        return catalogDTO;

    }
    @PostMapping("createCatalog")
    @PreAuthorize(" hasRole('MODERATOR') or hasAnyRole('ADMIN')")
    public ResponseEntity<?> saveCatalog(@RequestBody Catalog catalog){
        if (catalogService.exitCatalogByName(catalog.getCatalogName())){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Catalog C???a b???n ???? t???n t???i????????"));
        }
        catalogService.saveOfUpdate(catalog);
            return ResponseEntity.ok(new MessageResponse("Ch??c m???ng b???n ???? th??m Catalog Th??nh c??ng????????????"));
    }
    @PutMapping("/updateCatalog/{catalogId}")
    @PreAuthorize(" hasRole('MODERATOR') or hasAnyRole('ADMIN')")
    public ResponseEntity<?> updateCatalog(@PathVariable("catalogId") int catalogId, @RequestBody Catalog catalog){
        if (catalogService.exitCatalogByName(catalog.getCatalogName())){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Catalog C???a b???n ???? t???n t???i????????"));
        }
        try {
            Catalog catalogUpdate = catalogService.findById(catalogId);
            catalogUpdate.setCatalogName(catalog.getCatalogName());
            catalogUpdate.setCatalogStatus(catalog.isCatalogStatus());
            catalogService.saveOfUpdate(catalogUpdate);
            return ResponseEntity.ok(new MessageResponse("Ch??c m???ng b???n ???? Update Catalog Th??nh c??ng????????????"));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.ok("Update Th???t b???i");

        }

    }
    @PutMapping("/updateStatusCatalog/{catalogId}")
    @PreAuthorize(" hasRole('MODERATOR') or hasAnyRole('ADMIN')")
    public ResponseEntity<String> updateStatusCatalog(@PathVariable("catalogId") int catalogId){
        try {
            Catalog catalog = catalogService.findById(catalogId);
            catalog.setCatalogStatus(false);
            catalogService.saveOfUpdate(catalog);
            return ResponseEntity.ok("B???n ???? c???p nh???t th??nh c??ng");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.ok("C?? l???i trong qu?? tr??nh x??? l?? vui l??ng th??? l???i!");
        }
    }

    @GetMapping("/search")
    @PreAuthorize(" hasRole('MODERATOR') or hasAnyRole('ADMIN')")
    public List<Catalog> searchCatalog(@RequestParam("catalogName") String catalogName){
        return catalogService.searchByName(catalogName);
    }
    @GetMapping("/getPaggingAndSortByName")
    public ResponseEntity<Map<String,Object>> getPaggingAndSortByName(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam String direction){
        Sort.Order order;
        if (direction.equals("asc")){
            order=new Sort.Order(Sort.Direction.ASC,"catalogName");
        }else{
            order=new Sort.Order(Sort.Direction.DESC,"catalogName");
        }
        Pageable pageable = PageRequest.of(page,size,Sort.by(order));
        Page<Catalog> pageCatalog = catalogService.sortByNameAndPagination(pageable);
        Map<String,Object> data = new HashMap<>();
        data.put("catalog",pageCatalog.getContent());
        data.put("total",pageCatalog.getSize());
        data.put("totalItems",pageCatalog.getTotalElements());
        data.put("totalPages",pageCatalog.getTotalPages());
        return  new ResponseEntity<>(data, HttpStatus.OK);
    }
}
