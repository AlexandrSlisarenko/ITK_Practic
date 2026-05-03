package ru.slisarenko.jsonview.model.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.slisarenko.jsonview.exceptions.CustomerNotFoundException;
import ru.slisarenko.jsonview.model.entity.Customer;
import ru.slisarenko.jsonview.model.entity.Product;
import ru.slisarenko.jsonview.model.repository.CustomerRepository;
import ru.slisarenko.jsonview.model.repository.ProductRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class DAOServiceImpl implements DAOService {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    @Override
    public Optional<Customer> findCustomerById(Long id) {
        return this.customerRepository.findCustomerWithOrdersById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsCustomerById(Long id) {
        return this.customerRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Product> findProductById(Long id) {
        return this.productRepository.findById(id);
    }

    @Override
    public Product saveOrUpdate(Product entity) {
        return this.productRepository.save(entity);
    }


    @Override
    public Customer saveOrUpdate(Customer entity) {
        return this.customerRepository.save(entity);
    }

    @Override
    public Customer updateInfo(Customer entity) {
        var customerFromDB = this.customerRepository.findById(entity.getId())
                .orElseThrow(() -> new CustomerNotFoundException(entity.getId()));
        customerFromDB.setName(entity.getName());
        customerFromDB.setEmail(entity.getEmail());
        return this.customerRepository.save(customerFromDB);
    }

    @Override
    public boolean delete(Long id) {
        var deleteCustomer = this.customerRepository.findById(id);
        if (deleteCustomer.isPresent()) {
            this.customerRepository.delete(deleteCustomer.get());
            return true;
        } else {
            return false;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Customer> getAllInformation(Pageable pageable) {
        return this.customerRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Customer> findEntityWithFullInformationById(Long id) {
        return this.customerRepository.findCustomerWithOrdersById(id);
    }


}
