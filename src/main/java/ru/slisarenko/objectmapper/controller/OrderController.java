package ru.slisarenko.objectmapper.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.slisarenko.objectmapper.service.interfaces.OrderService;
import ru.slisarenko.objectmapper.service.mapper.MapperJson;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final MapperJson mapper;

    @PostMapping
    public ResponseEntity<String> createOrder(@Valid @RequestBody String jsonRequestDto) throws JsonProcessingException {
        var requestDto = this.mapper.deserializeOrderRequestDto(jsonRequestDto);
        var order = orderService.createOrder(requestDto);
        var json = this.mapper.serializeOrderResponseDto(order);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(json);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getOrder(@PathVariable Long id) throws JsonProcessingException {
        var order = orderService.getOrder(id);
        var json = this.mapper.serializeOrderResponseDto(order);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(json);
    }
}
