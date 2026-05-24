package ru.slisarenko.orders.service;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import ru.slisarenko.entity_lobrary.dto.order.OrderRequestDTO;

import static ru.slisarenko.entity_lobrary.constants.ServiceTopicNames.NEW_ORDERS_REQUEST_TOPIC;

@Log4j2
@Service
@RequiredArgsConstructor
public class KafkaService {
    private final KafkaTemplate<String, OrderRequestDTO> kafkaTemplate;

    public void createOrder(OrderRequestDTO orderRequestDTO) {
        var requestKafkaId = UUID.randomUUID().toString();
        //var response = createResponse(securityCheckRequestDTO);
        //log.info("AccountingAllocationResponseDTO: {}", response);

        SendResult<String, OrderRequestDTO> result = null;
        try {
            result = kafkaTemplate.send(NEW_ORDERS_REQUEST_TOPIC, requestKafkaId, orderRequestDTO).get();
            log.info("result partition {}", result.getRecordMetadata().partition());
            log.info("result offset {}", result.getRecordMetadata().offset());
            log.info("result timestamp {}", result.getRecordMetadata().timestamp());
            log.info("result topic {}", result.getRecordMetadata().topic());
            log.info("result key message {}", result.getProducerRecord().key());
        } catch (InterruptedException | ExecutionException e) {
            log.error(e);
            throw new KafkaException(e.getMessage());
        }
    }

    /*private ShopOrderInformationStatusDTO createResponse(OrderRequestDTO request) {
        return ShopOrderInformationStatusDTO.builder()
                .requestId(request.requestId())
                .status()
                .passed(true)
                .build();
    }*/
}
