package ru.slisarenko.objectmapper.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.slisarenko.objectmapper.service.dto.CustomerRequestDTO;
import ru.slisarenko.objectmapper.service.interfaces.CustomerService;
import ru.slisarenko.objectmapper.service.mapper.MapperJson;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    private final MapperJson mapper;

    @GetMapping("/?email={email}&&number={number}")
    public ResponseEntity<String> getAllInformation(@PathVariable @Valid String email,  @PathVariable String number) throws JsonProcessingException {
        var requestDto = CustomerRequestDTO.builder().email(email).contactNumber(number).build();
        var customer = customerService.getAllInformation(requestDto);
        var json = this.mapper.serializeCustomerResponseDto(customer);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(json);
    }

    @PostMapping
    public ResponseEntity<String> createProduct(@Valid @RequestBody String jsonRequestDto) throws JsonProcessingException {
        var requestDto = this.mapper.deserializeCustomerCreateDto(jsonRequestDto);
        var customer = customerService.createCustomer(requestDto);
        var json = this.mapper.serializeCustomerResponseDto(customer);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(json);
    }

}
