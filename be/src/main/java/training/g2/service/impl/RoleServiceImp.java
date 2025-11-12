package training.g2.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import training.g2.dto.Request.Attribute.RoleReq;
import training.g2.dto.common.PaginationDTO;
import training.g2.exception.common.BusinessException;
import training.g2.model.Role;
import training.g2.repository.RoleRepository;
import training.g2.service.RoleService;

import static training.g2.constant.Constants.Message.*;

@Service
public class RoleServiceImp implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImp(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role create(RoleReq req) {
        try {
            if (roleRepository.existsByNameIgnoreCase(req.getName())) {
                throw new BusinessException(ROLE_ALREADY_EXISTS);
            }

            Role entity = new Role();
            entity.setName(req.getName());
            entity.setDescription(req.getDescription());
            return roleRepository.save(entity);

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, ROLE_ADD_FAIL, e);
        }
    }

    @Override
    public Role update(long id, RoleReq req) {
        try {
            Role entity = roleRepository.findById(id)
                    .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, ROLE_NOT_FOUND));

            entity.setName(req.getName());
            entity.setDescription(req.getDescription());

            return roleRepository.save(entity);

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, ROLE_UPDATE_FAIL, e);
        }
    }

    @Override
    public Role getById(long id) {
        try {
            return roleRepository.findById(id)
                    .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, ROLE_NOT_FOUND));
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, ROLE_GET_FAIL, e);
        }
    }

    @Override
    public PaginationDTO<List<Role>> getAll(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Role> rolePage = roleRepository.findAll(pageable);
            List<Role> items = rolePage.getContent();

            return PaginationDTO.<List<Role>>builder()
                    .page(page + 1)
                    .size(size)
                    .total(rolePage.getTotalElements())
                    .items(items)
                    .build();

        } catch (Exception e) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, ROLE_GET_FAIL, e);
        }
    }
}
