package training.g2.controller.admin;

import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import training.g2.dto.Request.User.UpdateUserReqDTO;
import training.g2.dto.Response.User.CreateUserResDTO;
import training.g2.dto.Response.User.UpdateUserResDTO;
import training.g2.dto.Response.User.UsersResDTO;
import training.g2.dto.common.PaginationDTO;
import training.g2.model.ApiResponse;
import training.g2.model.User;
import training.g2.service.UserService;

@RestController
@RequestMapping("/api/v1/admin/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CreateUserResDTO> createUser(@Valid @RequestBody User user) {
        CreateUserResDTO result = userService.createUser(user);
        return new ApiResponse<>("Tạo người dùng thành công", result);
    }

    @GetMapping
    public ApiResponse<PaginationDTO<List<UsersResDTO>>> getAllUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Boolean isDeleted,
            @RequestParam(required = false) Instant fromDate,
            @RequestParam(required = false) Instant toDate,
            @RequestParam(defaultValue = "createdAt") String sortField,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        PaginationDTO<List<UsersResDTO>> result = userService.getAllUser(page - 1, size, email, isDeleted, fromDate,
                toDate,
                sortField, sortDirection);

        return new ApiResponse<>("Lấy danh sách người dùng thành công", result);
    }

    @GetMapping("/{id}")
    public ApiResponse<UsersResDTO> getUserById(@PathVariable("id") Long id) {
        UsersResDTO result = userService.getUserById(id);
        return new ApiResponse<>("Lấy thông tin người dùng thành công", result);
    }

    @PutMapping
    public ApiResponse<UpdateUserResDTO> updateUser(@RequestBody UpdateUserReqDTO dto) {
        UpdateUserResDTO result = userService.updateUser(dto);
        return new ApiResponse<>("Cập nhật người dùng thành công", result);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteOrRestoreUser(@PathVariable("id") long id) {
        userService.deleteUser(id);

        return new ApiResponse<>("ok", null);
    }
}
