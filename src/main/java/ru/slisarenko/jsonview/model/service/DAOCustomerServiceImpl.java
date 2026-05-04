package ru.slisarenko.jsonview.model.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.slisarenko.jsonview.exceptions.CustomerNotFoundException;
import ru.slisarenko.jsonview.model.entity.Customer;
import ru.slisarenko.jsonview.model.repository.CustomerRepository;
import ru.slisarenko.jsonview.service.dto.CustomerInformationDTO;

@Service
@RequiredArgsConstructor
@Transactional
public class DAOCustomerServiceImpl implements DAOCustomerService {

    private final CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    @Override
    public Optional<Customer> findCustomerById(Long id) {
        return this.customerRepository.findById(id);
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
    public Optional<CustomerInformationDTO> findEntityWithFullInformationById(Long id) {
       return this.customerRepository.findCustomerWithOrdersById(id);
    }


}
