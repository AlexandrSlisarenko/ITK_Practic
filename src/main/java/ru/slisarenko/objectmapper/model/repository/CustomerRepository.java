package ru.slisarenko.objectmapper.model.repository;

import jakarta.validation.constraints.Email;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.slisarenko.objectmapper.model.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmailOrContactNumber(@Email String email, String contactNumber);
}
