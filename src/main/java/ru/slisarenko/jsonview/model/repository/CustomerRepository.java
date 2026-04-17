package ru.slisarenko.jsonview.model.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.slisarenko.jsonview.model.entity.Customer;
import ru.slisarenko.jsonview.model.entity.CustomerInformation;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<CustomerInformation> findAllCustomerBy();

    @EntityGraph(attributePaths = {"orders"})
    Optional<Customer> findCustomerWithOrdersById(Long id);
}
