package ru.slisarenko.jsonview.model.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.slisarenko.jsonview.model.entity.Customer;
import ru.slisarenko.jsonview.service.dto.CustomerInformationDTO;

public interface DAOCustomerService {

    Optional<Customer> findCustomerById(Long id);
    Customer saveOrUpdate(Customer entity);
    Customer updateInfo(Customer entity);
    boolean delete(Long id);
    Page<Customer> getAllInformation(Pageable pageable);
    Optional<CustomerInformationDTO> findEntityWithFullInformationById(Long id);
}
