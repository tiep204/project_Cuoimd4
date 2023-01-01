package ra.model.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ra.model.entity.Catalog;
import ra.model.repository.CatalogRepository;
import ra.model.service.CatalogService;

import java.util.List;
@Service
public class CatalogServiceImp implements CatalogService {
    @Autowired
    private CatalogRepository catalogRepository;

    @Override
    public List<Catalog> findAll() {
        return catalogRepository.findAll();
    }

    @Override
    public Catalog findById(int catalogId) {
        return catalogRepository.findById(catalogId).get();
    }

    @Override
    public Catalog saveOfUpdate(Catalog catalog) {
        return catalogRepository.save(catalog);
    }

    @Override
    public void delete(int catalogId) {
        catalogRepository.deleteById(catalogId);
    }

    @Override
    public List<Catalog> searchByName(String catalogName) {
        return catalogRepository.findByCatalogNameContaining(catalogName);
    }

    @Override
    public Page<Catalog> sortByNameAndPagination(Pageable pageable) {
        return catalogRepository.findAll(pageable);
    }

    @Override
    public boolean exitCatalogByName(String catalogName) {
        return catalogRepository.existsByCatalogName(catalogName);
    }
}
