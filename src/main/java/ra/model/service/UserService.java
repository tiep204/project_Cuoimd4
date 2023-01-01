package ra.model.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.model.entity.Catalog;
import ra.model.entity.Users;

import java.util.List;

public interface UserService {
    List<Users> findByUserName(String userName);
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
    Users saveOrUpdate(Users user);
    List<Users> findAll();
    Users findById(int userId);
    Page<Users> sortByNameAndPagination(Pageable pageable);
    Users updateStatus(Users users);

}