package ru.slisarenko.jsonview.controller;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.slisarenko.jsonview.controller.Views.CustomerDetails;
import ru.slisarenko.jsonview.controller.Views.CustomerSummary;
import ru.slisarenko.jsonview.service.CustomerService;
import ru.slisarenko.jsonview.service.dto.CustomerInformationDTO;
import ru.slisarenko.jsonview.service.dto.OrderRequestDTO;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor

public class CustomerController {
    private final CustomerService customerService;

    @GetMapping
    @JsonView(CustomerSummary.class)
    public ResponseEntity<Page<CustomerInformationDTO>> getAllCustomers(@RequestParam int page, @RequestParam int size) {
        return new ResponseEntity<>(this.customerService.getInformationAllCustomers(page, size), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @JsonView(CustomerDetails.class)
    public ResponseEntity<CustomerInformationDTO> getCustomerDetails(@PathVariable int id) {
        return new ResponseEntity<>(this.customerService.getCustomerById((long) id), HttpStatus.OK);
    }

    @PostMapping("/add")
    @JsonView(CustomerSummary.class)
    public ResponseEntity<CustomerInformationDTO> createCustomers(@RequestBody CustomerInformationDTO request) {
        return new ResponseEntity<>(this.customerService.createNewCustomer(request), HttpStatus.CREATED);
    }

    @PostMapping("/add_order")
    @JsonView(CustomerDetails.class)
    public ResponseEntity<CustomerInformationDTO> addOrderFromCustomers(@RequestBody OrderRequestDTO request) {
        return new ResponseEntity<>(this.customerService.addOrderFromCustomer(request), HttpStatus.OK);
    }

    @PutMapping
    @JsonView(CustomerSummary.class)
    public ResponseEntity<CustomerInformationDTO> updateCustomerDetails(@RequestBody CustomerInformationDTO request) {
        return new ResponseEntity<>(this.customerService.updateInformCustomer(request), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteCustomerDetails(@PathVariable int id) {
        return new ResponseEntity<>(this.customerService.deleteCustomer((long) id), HttpStatus.OK);
    }





}
