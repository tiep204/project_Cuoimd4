package ra.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.model.entity.Catalog;

import java.util.List;

@Repository
public interface CatalogRepository extends JpaRepository<Catalog,Integer> {
    List<Catalog> findByCatalogNameContaining(String catalogName);
    boolean existsByCatalogName(String userName);
}
