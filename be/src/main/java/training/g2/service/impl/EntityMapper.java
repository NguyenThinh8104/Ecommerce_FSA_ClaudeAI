package training.g2.service.impl;

import java.util.List;

public interface EntityMapper<D, E> {
    D toDTO(E entity);

    E toEntity(D dto);

    List<D> toDtoList(List<E> entityList);

    List<E> toEntityList(List<D> dtoList);
}
