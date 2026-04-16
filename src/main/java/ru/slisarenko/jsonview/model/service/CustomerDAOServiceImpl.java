package ru.slisarenko.jsonview.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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


    @Transactional(readOnly = true)
    @Override
    public Optional<Customer> getById(Long id) {
        return this.customerRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Customer> getAll() {
        return this.customerRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Customer> getAll(Long id) {
        return List.of();
    }

    @Override
    public Customer saveOrUpdate(Customer entity) {
        var customerFromDB = customerRepository.save(entity);
        var orders = entity.getOrders().stream().
                map(order ->{
                    order.setCustomer(customerFromDB);
                    var orderFormDB = orderRepository.save(order);
                    order = orderFormDB;
                    order.getProducts().stream()
                            .map(product ->{
                                product.setOrder(orderFormDB);
                                return this.productRepository.save(product);
                            });
                    return order;
                }).toList();
        entity.setOrders(orders);
        return customerRepository.save(entity);
    }

    @Override
    public boolean delete(Customer entity) {
        var deleteCustomer = this.customerRepository.findById(entity.getId());
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
        var information = this.customerRepository.findAllProjectedBy();
        return information.stream().map(element -> Customer.builder()
                .id(element.getId())
                .name(element.getName())
                .email(element.getEmail())
                .build()).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Customer> findEntityWithFullInformationById(Long id) {
        System.out.println("ID = " + id);
        var customer = this.customerRepository.findById(id);
        var orders1 = this.orderRepository.findAll();
        orders1.forEach(order -> {System.out.println("Order = " + order);});
        if (customer.isPresent()) {
            var customerEntity = customer.get();
            System.out.println("customerEntity orders COUNT = " + customerEntity.getOrders().size());
            var orders = this.orderRepository.findByCustomer_Id(id);
            System.out.println("OrderCout = " + orders.size());
            orders = orders.stream().map(order -> {
                this.productRepository.getAllByOrder_Id(order.getId()).forEach(order::addProduct);
                System.out.println("Order %: " + order.getId() + " Count: " + order.getProducts().size());
                return order;
            }).toList();
            customerEntity.setOrders(orders);
            System.out.println("Order = " + customerEntity.getOrders().size());
            System.out.println("Product = " + customerEntity.getOrders().get(0).getProducts().size());
        }

        return customer;
    }


}
