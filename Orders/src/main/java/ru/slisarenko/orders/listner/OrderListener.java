package ru.slisarenko.orders.listner;

import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.slisarenko.entity_library.dto.ShopOrderInformationStatusDTO;
import ru.slisarenko.entity_library.dto.payment.PaymentRequestDTO;
import ru.slisarenko.entity_library.dto.persist.PersistDTO;

import static ru.slisarenko.entity_library.constants.ServiceTopicNames.PAYED_ORDER_REQUEST_TOPIC;
import static ru.slisarenko.entity_library.constants.ServiceTopicNames.SENT_NOTIFICATION_TOPIC;
import static ru.slisarenko.entity_library.constants.ServiceTopicNames.SENT_PERSIST_RESPONSE_TOPIC;

@Slf4j
@Component
@Repository
@KafkaListener(topics = SENT_PERSIST_RESPONSE_TOPIC, containerFactory = "kafkaListenerContainerFactory")
@RequiredArgsConstructor
public class OrderListener {
    private final KafkaTemplate<String, ShopOrderInformationStatusDTO> kafkaTemplateInformation;
    private final KafkaTemplate<String, PaymentRequestDTO> kafkaTemplatePayment;

    @KafkaHandler
    public void listen(PersistDTO order) {
        log.info("Order UUID => {}", order.requestUUId());
        sentToInformation(order);
        sendToPayment(order);
    }

    private void sendToPayment(PersistDTO order){
        var message = PaymentRequestDTO.builder()
                .requestUUId(order.requestUUId())
                .customerId(order.customerId())
                .orderId(order.orderId())
                .build();
        SendResult<String, PaymentRequestDTO> result = null;
        try {
            result = kafkaTemplatePayment.send(PAYED_ORDER_REQUEST_TOPIC, order.requestUUId(), message).get();
            log.info("result partition {}", result.getRecordMetadata().partition());
            log.info("result offset {}", result.getRecordMetadata().offset());
            log.info("result timestamp {}", result.getRecordMetadata().timestamp());
            log.info("result topic {}", result.getRecordMetadata().topic());
            log.info("result key message {}", result.getProducerRecord().key());
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage());
            throw new KafkaException(e.getMessage());
        }
    }

    private void sentToInformation(PersistDTO order) {
        var message = ShopOrderInformationStatusDTO.builder()
                .orderId(order.orderId())
                .requestUUId(order.requestUUId())
                .status(order.status())
                .build();
        SendResult<String, ShopOrderInformationStatusDTO> result = null;
        try {
            result = kafkaTemplateInformation.send(SENT_NOTIFICATION_TOPIC, order.requestUUId(), message).get();
            log.info("result partition {}", result.getRecordMetadata().partition());
            log.info("result offset {}", result.getRecordMetadata().offset());
            log.info("result timestamp {}", result.getRecordMetadata().timestamp());
            log.info("result topic {}", result.getRecordMetadata().topic());
            log.info("result key message {}", result.getProducerRecord().key());
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage());
            throw new KafkaException(e.getMessage());
        }
    }
}
