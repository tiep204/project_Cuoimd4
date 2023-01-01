package ra.model.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.model.entity.Catalog;

import java.util.List;

public interface CatalogService {
    List<Catalog> findAll();
    Catalog findById(int catalogId);
    Catalog saveOfUpdate(Catalog catalog);
    void delete(int catalogId);
    List<Catalog> searchByName(String catalogName);
    Page<Catalog> sortByNameAndPagination(Pageable pageable);
    boolean exitCatalogByName(String catalogName);
}
