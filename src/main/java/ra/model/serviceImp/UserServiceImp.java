package ra.model.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ra.model.entity.Users;
import ra.model.repository.UserRepository;
import ra.model.service.UserService;

import java.util.List;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserRepository userRepository;



    @Override
    public List<Users> findByUserName(String userName) {
        return userRepository.findAll();
    }

    @Override
    public boolean existsByUserName(String userName) {
        return userRepository.existsByUserName(userName);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Users saveOrUpdate(Users user) {
        return userRepository.save(user);
    }

    @Override
    public List<Users> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Users findById(int userId) {
        return userRepository.findById(userId).get();
    }

    @Override
    public Page<Users> sortByNameAndPagination(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Users updateStatus(Users users) {
        return userRepository.save(users);
    }
}
