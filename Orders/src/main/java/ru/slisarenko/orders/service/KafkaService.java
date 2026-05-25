package ru.slisarenko.orders.service;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import ru.slisarenko.entity_library.dto.order.OrderRequestDTO;
import ru.slisarenko.entity_library.dto.persist.PersistDTO;

import static ru.slisarenko.entity_library.constants.ServiceTopicNames.SENT_PERSIST_REQUEST_TOPIC;

@Log4j2
@Service
@RequiredArgsConstructor
public class KafkaService {
    private final KafkaTemplate<String, PersistDTO> persistKafkaTemplate;


    public String createOrder(OrderRequestDTO orderRequestDTO) {
        var requestKafkaId = UUID.randomUUID().toString();
        var persistData = PersistDTO.builder()
                .requestUUId(requestKafkaId)
                .customerId(orderRequestDTO.customerId())
                .productIds(orderRequestDTO.productIds())
                .build();
        SendResult<String, PersistDTO> result = null;
        try {
            result = persistKafkaTemplate.send(SENT_PERSIST_REQUEST_TOPIC, requestKafkaId, persistData).get();
            log.info("result partition {}", result.getRecordMetadata().partition());
            log.info("result offset {}", result.getRecordMetadata().offset());
            log.info("result timestamp {}", result.getRecordMetadata().timestamp());
            log.info("result topic {}", result.getRecordMetadata().topic());
            log.info("result key message {}", result.getProducerRecord().key());
        } catch (InterruptedException | ExecutionException e) {
            log.error(e);
            throw new KafkaException(e.getMessage());
        }
        return requestKafkaId;
    }



}
