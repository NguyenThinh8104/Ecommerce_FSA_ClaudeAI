package training.g2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import training.g2.dto.Request.Product.ProductReqDTO;
import training.g2.dto.Response.Product.ProductCreateResDTO;
import training.g2.dto.Response.Product.ProductResDTO;
import training.g2.dto.Response.Product.ProductUpdateResDTO;
import training.g2.model.Product;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {
    @Mapping(source = "category.id", target = "category.id")
    ProductResDTO toDTO(Product product);

    @Mapping(source = "category.id", target = "category.id")
    ProductUpdateResDTO toUpdateDTO(Product product);

    @Mapping(source = "category.id", target = "category.id")
    ProductCreateResDTO toCreateDTO(Product product);

    Product toProductEntity(ProductReqDTO dto);
}
