package training.g2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import training.g2.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {

    List<Category> findByParentIsNotNullAndDeletedFalse();
    List<Category> findAllByDeletedFalse();
    List<Category> findAllByDeletedFalseAndParentIsNull();

    @Query("SELECT c.name, COUNT (p.id) " +
            "FROM Category c " +
            "LEFT JOIN c.products p " +
            "WHERE c.parent IS NULL AND c.deleted = false " +
            "GROUP BY c.id, c.name")
    List<Object[]> countProductsByCategory();

}
