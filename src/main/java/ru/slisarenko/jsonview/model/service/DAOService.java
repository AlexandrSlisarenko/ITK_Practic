package ru.slisarenko.jsonview.model.service;

import java.util.List;
import java.util.Optional;
import ru.slisarenko.jsonview.model.entity.BaseEntity;

public interface DAOService<T extends BaseEntity> {

    Optional<T> findById(Long id);
    List<T> getAll();
    T saveOrUpdate(T entity);
    T updateInfo(T entity);
    boolean delete(Long id);
    List<T> getAllInformation();
    Optional<T> findEntityWithFullInformationById(Long id);
}
