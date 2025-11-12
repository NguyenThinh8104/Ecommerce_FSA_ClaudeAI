package training.g2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import training.g2.dto.Request.Category.CategoryReqDTO;
import training.g2.dto.Response.Category.CategoryCreateResDTO;
import training.g2.dto.Response.Category.CategoryResDTO;
import training.g2.dto.Response.Category.CategoryUpdateRes;
import training.g2.model.Category;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    @Mapping(source = "parent.id", target = "parentId")
    CategoryResDTO toDto(Category category);

    @Mapping(source = "parent.id", target = "parentId")
    CategoryCreateResDTO toCreatedDTO(Category category);

    @Mapping(source = "parent.id", target = "parentId")
    CategoryUpdateRes toUpdateDTO(Category category);

    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "children", ignore = true)
    Category toEntity(CategoryReqDTO dto);
}
