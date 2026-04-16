package ru.slisarenko.jsonview.model.service;

import java.util.List;
import java.util.Optional;
import ru.slisarenko.jsonview.model.entity.BaseEntity;
import ru.slisarenko.jsonview.model.entity.Customer;

public interface DAOService<T extends BaseEntity> {

    Optional<T> getById(Long id);
    List<T> getAll();
    List<T> getAll(Long id);
    T saveOrUpdate(T entity);
    boolean delete(T entity);
    List<T> getAllInformation();
    Optional<T> findEntityWithFullInformationById(Long id);
}
