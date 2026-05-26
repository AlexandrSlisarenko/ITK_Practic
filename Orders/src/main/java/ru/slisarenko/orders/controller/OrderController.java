package ru.slisarenko.orders.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.slisarenko.entity_library.dto.ShopOrderInformationStatusDTO;
import ru.slisarenko.entity_library.dto.order.OrderRequestDTO;
import ru.slisarenko.entity_library.enums.OrderStatus;
import ru.slisarenko.orders.service.KafkaService;

@Log4j2
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final KafkaService kafkaService;

    @PostMapping
    public ResponseEntity<ShopOrderInformationStatusDTO> addOrder(@Validated @RequestBody OrderRequestDTO order) {
        log.info(order);
        var key = this.kafkaService.createOrder(order);
        var result = ShopOrderInformationStatusDTO.builder()
                .orderId(0L)
                .requestUUId(key)
                .status(OrderStatus.CREATED)
                .build();
        return new ResponseEntity<>(result, HttpStatus.CREATED) ;
    }

}
