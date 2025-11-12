package training.g2.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;
import training.g2.dto.Request.Category.CategoryReqDTO;
import training.g2.dto.Response.Category.CategoryCreateResDTO;
import training.g2.dto.Response.Category.CategoryResDTO;
import training.g2.dto.Response.Category.CategoryUpdateRes;
import training.g2.dto.common.PaginationDTO;
import training.g2.exception.common.BusinessException;
import training.g2.mapper.CategoryMapper;
import training.g2.model.Category;
import training.g2.repository.CategoryRepository;
import training.g2.service.CategoryService;
import static training.g2.constant.Constants.Message.*;

@Service
public class CategoryServiceImp implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImp(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public CategoryCreateResDTO createCategory(CategoryReqDTO reqDTO) {
        try {
            Category category = categoryMapper.toEntity(reqDTO);
            if (reqDTO.getParentId() != null) {
                Category cateParent = categoryRepository.findById(reqDTO.getParentId()).orElseThrow(
                        () -> new BusinessException(HttpStatus.NOT_FOUND, CATEGORY_NOT_FOUND));
                category.setParent(cateParent);
            } else {
                category.setParent(null);
            }

            Category save = categoryRepository.save(category);
            CategoryCreateResDTO dto = categoryMapper.toCreatedDTO(save);
            return dto;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, ADD_CATEGORY_FAIL, e);
        }
    }

    @Override
    public CategoryResDTO getCategoryById(long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, CATEGORY_NOT_FOUND));
        CategoryResDTO dto = categoryMapper.toDto(category);
        return dto;
    }

    @Override
    public CategoryUpdateRes updateCategory(long id, CategoryReqDTO reqDTO) {
        try {
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, CATEGORY_NOT_FOUND));

            category.setName(reqDTO.getName());
            category.setDescription(reqDTO.getDescription());

            if (reqDTO.getParentId() != null) {
                Category parent = categoryRepository.findById(reqDTO.getParentId())
                        .orElseThrow(() -> new BusinessException(CATEGORY_PARENT_NOT_FOUND));
                category.setParent(parent);
            }

            Category updated = categoryRepository.save(category);
            return categoryMapper.toUpdateDTO(updated);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, UPDATE_CATEGORY_FAIL, e);
        }
    }

    @Override
    public void deleteCategory(long id) {
        try {
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> BusinessException.of(HttpStatus.NOT_FOUND, CATEGORY_NOT_FOUND));

            category.setDeleted(true);
            categoryRepository.save(category);

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, DELETE_CATEGORY_FAIL, e);
        }
    }

    @Override
    public PaginationDTO<List<CategoryResDTO>> getAllCategory(int page, int size, String name, Long parentId,
            Boolean isDeleted,
            String sortField, String sortDirection) {
        try {
            Sort sort = sortDirection.equalsIgnoreCase("asc")
                    ? Sort.by(sortField).ascending()
                    : Sort.by(sortField).descending();

            Pageable pageable = PageRequest.of(page, size, sort);
            Specification<Category> spec = buildQuerySpecification(name, isDeleted, parentId);
            Page<Category> catePage = categoryRepository.findAll(spec, pageable);
            List<CategoryResDTO> categories = catePage.getContent().stream().map(c -> categoryMapper.toDto(c))
                    .collect(Collectors.toList());

            PaginationDTO<List<CategoryResDTO>> pagingDTO = PaginationDTO.<List<CategoryResDTO>>builder()
                    .page(page + 1)
                    .size(size)
                    .total(catePage.getTotalElements())
                    .items(categories)
                    .build();

            return pagingDTO;
        } catch (Exception e) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, CATEGORY_NOT_FOUND, e);
        }
    }

    public Specification<Category> buildQuerySpecification(String name, Boolean deleted, Long parentId) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.trim().isEmpty()) {
                String likeName = "%" + name.toLowerCase().trim() + "%";
                predicates.add(cb.like(cb.lower(root.get("name")), likeName));

            }

            if (Boolean.TRUE.equals(deleted)) {
                predicates.add(cb.equal(root.get("deleted"), true));
            } else {
                predicates.add(cb.equal(root.get("deleted"), false));
            }

            if (parentId != null) {
                if (parentId == 0L) {
                    predicates.add(cb.isNull(root.get("parent")));
                } else if (parentId > 0L) {
                    predicates.add(cb.equal(root.get("parent").get("id"), parentId));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));

        };
    }

    @Override
    public List<CategoryResDTO> getChildCategories() {
        List<Category> childCategories = categoryRepository.findByParentIsNotNullAndDeletedFalse();
        return childCategories.stream()
                .map(c -> categoryMapper.toDto(c))
                .toList();
    }

}
