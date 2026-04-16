package ru.slisarenko.jsonview.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.slisarenko.jsonview.exceptions.CustomerNotFoundException;
import ru.slisarenko.jsonview.model.entity.Customer;
import ru.slisarenko.jsonview.model.service.DAOService;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final DAOService<Customer> customerDAOService;

    public Customer createOrderForNewCustomer(Customer testCustomer) {
        return customerDAOService.saveOrUpdate(testCustomer);
    }

    public List<Customer> getAllCustomers() {
        return customerDAOService.getAll();
    }

    public boolean deleteCustomer(Customer testCustomer) {
        return customerDAOService.delete(testCustomer);
    }


    public Customer updateInformCustomer(Customer testCustomer) {
        return customerDAOService.saveOrUpdate(testCustomer);
    }

    public List<Customer> getInformationAllCustomers() {
        return customerDAOService.getAllInformation();
    }

    public Customer getCustomerById(Long id) {
        var message = String.format("Customer with id = %s not found", id);
        return customerDAOService.findEntityWithFullInformationById(id)
                .orElseThrow(()-> new CustomerNotFoundException(message));
    }
}
