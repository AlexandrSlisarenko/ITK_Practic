package ru.slisarenko.payment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.slisarenko.entity_library.dto.order.OrderRequestDTO;

import static ru.slisarenko.entity_library.constants.ServiceTopicNames.NEW_ORDERS_REQUEST_TOPIC;

@Slf4j
@Component
@RequiredArgsConstructor
@KafkaListener(topics = {NEW_ORDERS_REQUEST_TOPIC})
public class KafkaController {
    @KafkaHandler
    public void handleSaga(OrderRequestDTO requestDTO) {
        log.info("Saga received: {}", requestDTO);
    }
}
