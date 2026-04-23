package ru.slisarenko.objectmapper.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.slisarenko.objectmapper.service.interfaces.CustomerService;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;


    @GetMapping
    public ResponseEntity<String> getInformationCustomer(@RequestBody String request) throws JsonProcessingException {
        var jsonResponse = customerService.getCustomerInformation(request);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonResponse);
    }

    @PostMapping
    public ResponseEntity<String> createCustomer(@RequestBody String jsonRequestDto) throws JsonProcessingException {
        var jsonCustomer = customerService.createCustomer(jsonRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonCustomer);
    }

}
