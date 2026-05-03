package ru.slisarenko.jsonview.model.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.slisarenko.jsonview.model.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @EntityGraph(attributePaths = {"orders"})
    Optional<Customer> findCustomerWithOrdersById(Long id);
}
