package training.g2.service;

import java.time.Instant;
import java.util.List;

import training.g2.dto.Request.User.RegisterReqDTO;
import training.g2.dto.Request.User.UpdateUserReqDTO;
import training.g2.dto.Response.User.CreateUserResDTO;
import training.g2.dto.Response.User.UpdateUserResDTO;
import training.g2.dto.Response.User.UsersResDTO;
import training.g2.dto.common.PaginationDTO;
import training.g2.model.User;

public interface UserService {

    CreateUserResDTO createUser(User user);

    CreateUserResDTO register(RegisterReqDTO registerDTO);

    UpdateUserResDTO updateUser(UpdateUserReqDTO userDTO);

    UsersResDTO getUserById(long id);

    UsersResDTO deleteUser(long id);

    User getUserByEmail(String email);

    PaginationDTO<List<UsersResDTO>> getAllUser(int page, int size, String email, Boolean isDeleted,
            Instant fromDate, Instant toDate,
            String sortField, String sortDirection);

}
