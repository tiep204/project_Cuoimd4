package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ra.jwt.JwtTokenProvider;
import ra.model.entity.Cart;
import ra.model.entity.ERole;
import ra.model.entity.Roles;
import ra.model.entity.Users;
import ra.model.service.CartService;
import ra.model.service.RoleService;
import ra.model.service.UserService;
import ra.payload.request.LoginRequest;
import ra.payload.request.SignupRequest;
import ra.payload.response.JwtResponse;
import ra.payload.response.MessageResponse;
import ra.security.CustomUserDetails;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    CartService cartService;
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
        if (userService.existsByUserName(signupRequest.getUserName())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: UserName cua ban da ton tai"));
        }
        if (userService.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: email cua ban da ton tai"));
        }
        Cart cart = new Cart();
        cartService.saveCart(cart);
        Users user = new Users();
        user.getCartList().add(cart);
        user.setUserName(signupRequest.getUserName());
        user.setPassword(encoder.encode(signupRequest.getPassword()));
        user.setEmail(signupRequest.getEmail());
        user.setPhone(signupRequest.getPhone());
        user.setUserStatus(true);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dateNow = new Date();
        String strNow = sdf.format(dateNow);
        try {
            user.setCreated(sdf.parse(strNow));
        }catch (Exception ex){
            ex.printStackTrace();
        }
        Set<String> strRoles = signupRequest.getListRoles();
        Set<Roles> listRoles = new HashSet<>();
        if (strRoles==null){
            //User quyen mac dinh
            Roles userRole = roleService.findByRoleName(ERole.ROLE_USER).orElseThrow(()->new RuntimeException("Error: Role is not found"));
            listRoles.add(userRole);
        }else {
            strRoles.forEach(role->{
                switch (role){
                    case "admin":
                        Roles adminRole = roleService.findByRoleName(ERole.ROLE_ADMIN)
                                .orElseThrow(()->new RuntimeException("Error: khong tim thai vai tro"));
                        listRoles.add(adminRole);
                    case "moderator":
                        Roles modRole = roleService.findByRoleName(ERole.ROLE_MODERATOR)
                                .orElseThrow(()->new RuntimeException("Error: khong tim thai vai tro"));
                        listRoles.add(modRole);
                    case "user":
                        Roles userRole = roleService.findByRoleName(ERole.ROLE_USER)
                                .orElseThrow(()->new RuntimeException("Error: khong tim thai vai tro"));
                        listRoles.add(userRole);
                }
            });
        }
        user.setListRoles(listRoles);
        userService.saveOrUpdate(user);
        return ResponseEntity.ok(new MessageResponse("dang ky thanh cong"));
    }
    @PostMapping("/signin")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUserName(),loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails customUserDetail = (CustomUserDetails) authentication.getPrincipal();
        //Sinh JWT tra ve client
        String jwt = tokenProvider.generateToken(customUserDetail);
        //Lay cac quyen cua user
        List<String> listRoles = customUserDetail.getAuthorities().stream()
                .map(item->item.getAuthority()).collect(Collectors.toList());
        if (customUserDetail.isUserStatus()){
            return ResponseEntity.ok(new JwtResponse(jwt,customUserDetail.getUsername(),customUserDetail.getEmail(),
                    customUserDetail.getPhone(),listRoles));
        }else {
            return ResponseEntity.ok("Tai khoan nay da bi block");
        }


    };
    @GetMapping("getAllUser")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<Users> getALl(){
        return userService.findAll();
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public Users getUserById(@PathVariable("userId") int userId){
       return userService.findById(userId);
    }
////////////////////blockUser///////////////////////////////////
    @PutMapping("/blockUser/{userId}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<String> blockUser(@PathVariable("userId") int userId){
        try {
            Users users = userService.findById(userId);
            users.setUserStatus(false);
            userService.saveOrUpdate(users);
            return ResponseEntity.ok("Chúc mừng bạn đã khóa thành công");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.ok("Bạn chưa cập nhật thành công");
        }
    }
//////////////////////////UnlockUser//////////////////////////////////////
    @PutMapping("/unlockUser/{userId}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<String> unlockUser(@PathVariable("userId") int userId){
        try {
            Users users = userService.findById(userId);
            users.setUserStatus(true);
            userService.saveOrUpdate(users);
            return ResponseEntity.ok("Chúc mừng bạn đã mở khoa thành công thành công");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.ok("Bạn chưa cập nhật thành công");
        }
    }

    @GetMapping("/searchUser")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<Users> searchUsersss(@RequestParam("userName") String userName){
        return userService.findByUserName(userName);
    }
    @GetMapping("/getPaggingAndSortByName")
    public ResponseEntity<Map<String,Object>> getPaggingAndSortByName(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam String direction){
        Sort.Order order;
        if (direction.equals("asc")){
            order= new Sort.Order(Sort.Direction.ASC,"userName");
        }else {
            order = new Sort.Order(Sort.Direction.DESC,"userName");
        }
        Pageable pageable = PageRequest.of(page, size,Sort.by(order));
        Page<Users> usersPage = userService.sortByNameAndPagination(pageable);
        Map<String,Object> data = new HashMap<>();
        data.put("user",usersPage.getContent());
        data.put("total",usersPage.getSize());
        data.put("totalItems",usersPage.getTotalElements());
        data.put("totalPages",usersPage.getTotalPages());
        return new ResponseEntity<>(data, HttpStatus.OK);
    }


}
