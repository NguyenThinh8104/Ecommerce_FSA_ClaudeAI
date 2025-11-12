package training.g2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import training.g2.model.AttributeValue;

public interface AttributeValueRepository extends JpaRepository<AttributeValue, Long> {

    boolean existsByValue(String value);

    @Query("""
            SELECT v from AttributeValue v
            where v.attribute.id=:attributeId
            order by v.id asc
            """)
    List<AttributeValue> findAllByAttributeId(@Param("attributeId") Long attributeId);
}
