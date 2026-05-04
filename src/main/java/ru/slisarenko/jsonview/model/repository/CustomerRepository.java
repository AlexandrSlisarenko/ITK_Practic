package ru.slisarenko.jsonview.model.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.slisarenko.jsonview.model.entity.Customer;
import ru.slisarenko.jsonview.service.dto.CustomerInformationDTO;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT NEW ru.slisarenko.jsonview.service.dto.CustomerInformationDTO(customer.id, customer.name, customer.email) " +
           "FROM Customer customer " +
           "WHERE customer.id = :id")
    Optional<CustomerInformationDTO> findCustomerWithOrdersById(@Param("id") Long id);


}
