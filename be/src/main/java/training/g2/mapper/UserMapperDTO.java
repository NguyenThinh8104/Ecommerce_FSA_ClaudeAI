package training.g2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import training.g2.dto.Response.User.CreateUserResDTO;
import training.g2.dto.Response.User.UpdateUserResDTO;
import training.g2.dto.Response.User.UsersResDTO;
import training.g2.model.User;
import training.g2.service.impl.EntityMapper;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapperDTO extends EntityMapper<UsersResDTO, User> {
    @Override
    @Mapping(target = "role.id", source = "role.id")
    UsersResDTO toDTO(User user);

    CreateUserResDTO toCreateDTO(User user);

    UpdateUserResDTO toUpdateDTO(User user);
}
