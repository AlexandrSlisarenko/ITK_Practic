package ru.slisarenko.jsonview.model.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.slisarenko.jsonview.exceptions.CustomerNotFoundException;
import ru.slisarenko.jsonview.model.entity.Customer;
import ru.slisarenko.jsonview.model.repository.CustomerRepository;
import ru.slisarenko.jsonview.model.repository.OrderRepository;
import ru.slisarenko.jsonview.model.repository.ProductRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerDAOServiceImpl implements DAOService<Customer> {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    public Optional<Customer> findById(Long id) {
        return this.customerRepository.findCustomerWithOrdersById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Customer> getAll() {
        return this.customerRepository.findAll();
    }

    @Override
    public Customer saveOrUpdate(Customer entity) {
        var customerFromDB = this.customerRepository.save(entity);
        entity.getOrders().forEach(order -> {
            order.setCustomer(customerFromDB);
            var orderFromDB = this.orderRepository.save(order);
            order.getProducts().forEach(product -> {
                product.setOrder(orderFromDB);
                productRepository.save(product);
            });
        });
        return customerFromDB;
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
    public List<Customer> getAllInformation() {
        var information = this.customerRepository.findAllCustomerBy();
        return information.stream().map(element -> Customer.builder()
                .id(element.getId())
                .name(element.getName())
                .email(element.getEmail())
                .build()).toList();


    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Customer> findEntityWithFullInformationById(Long id) {
        var customer = this.customerRepository.findCustomerWithOrdersById(id);
        customer.ifPresent(customerEntity -> customerEntity.getOrders()
                .forEach(order -> order.getProducts().size()));
        return customer;
    }


}
