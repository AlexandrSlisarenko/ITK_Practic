package ru.slisarenko.jsonview.model.service;

import java.util.List;
import java.util.Optional;
import ru.slisarenko.jsonview.model.entity.BaseEntity;

public interface DAOService<T extends BaseEntity> {

    Optional<T> getById(Long id);
    List<T> getAll(Long id);
}
