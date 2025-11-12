package training.g2.controller.admin;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import training.g2.dto.Request.Attribute.RoleReq;
import training.g2.dto.common.PaginationDTO;
import training.g2.model.ApiResponse;
import training.g2.model.Role;
import training.g2.service.RoleService;

import static training.g2.constant.Constants.Message.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Role> create(@Valid @RequestBody RoleReq req) {
        Role created = roleService.create(req);
        return new ApiResponse<Role>(ROLE_CREATED_SUCCESS, created);
    }

    @GetMapping
    public ApiResponse<PaginationDTO<List<Role>>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        PaginationDTO<List<Role>> result = roleService.getAll(Math.max(page - 1, 0), size);
        return new ApiResponse<PaginationDTO<List<Role>>>(ROLE_GET_FAIL.replace(" thất bại", " thành công"), result);
    }

    @GetMapping("/{id}")
    public ApiResponse<Role> getById(@PathVariable long id) {
        Role data = roleService.getById(id);
        return new ApiResponse<Role>(ROLE_GET_FAIL.replace(" thất bại", " thành công"), data);
    }

    @PutMapping("/{id}")
    public ApiResponse<Role> update(@PathVariable long id,
            @Valid @RequestBody RoleReq req) {
        Role updated = roleService.update(id, req);
        return new ApiResponse<Role>(ROLE_UPDATED_SUCCESS, updated);
    }

}
