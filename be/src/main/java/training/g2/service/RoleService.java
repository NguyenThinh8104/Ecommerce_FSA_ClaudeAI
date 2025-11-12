package training.g2.service;

import training.g2.dto.Request.Attribute.RoleReq;
import training.g2.dto.common.PaginationDTO;
import training.g2.model.Role;

public interface RoleService {

    Role create(RoleReq req);

    Role update(long id, RoleReq req);

    Role getById(long id);

    PaginationDTO<java.util.List<Role>> getAll(int page, int size);

}
