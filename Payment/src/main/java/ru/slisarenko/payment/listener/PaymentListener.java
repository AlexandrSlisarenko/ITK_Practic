package ru.slisarenko.payment.listener;

import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import ru.slisarenko.entity_library.dto.ShopOrderInformationStatusDTO;
import ru.slisarenko.entity_library.dto.payment.PaymentRequestDTO;
import ru.slisarenko.entity_library.dto.persist.PersistDTO;
import ru.slisarenko.entity_library.dto.shipping.ShippingRequestDTO;

import static ru.slisarenko.entity_library.constants.ServiceTopicNames.PAYED_ORDER_REQUEST_TOPIC;
import static ru.slisarenko.entity_library.constants.ServiceTopicNames.SENT_PERSIST_REQUEST_TOPIC;
import static ru.slisarenko.entity_library.constants.ServiceTopicNames.SENT_PERSIST_RESPONSE_TOPIC;
import static ru.slisarenko.entity_library.constants.ServiceTopicNames.SENT_SHIPPING_ORDER_REQUEST_TOPIC;

@Slf4j
@Component
@RequiredArgsConstructor
@KafkaListener(topics = {PAYED_ORDER_REQUEST_TOPIC, SENT_PERSIST_RESPONSE_TOPIC})
public class PaymentListener {
    private final KafkaTemplate<String, PaymentRequestDTO> kafkaTemplate;
    private final KafkaTemplate<String, ShopOrderInformationStatusDTO> kafkaTemplateInformation;
    private final KafkaTemplate<String, ShippingRequestDTO> kafkaTemplateShipping;

    @KafkaHandler
    public void handleSaga(PaymentRequestDTO requestDTO) {
        log.info("Payment invoice: {}", requestDTO);
       SendResult<String, PaymentRequestDTO> result = null;
        try {
            result = kafkaTemplate.send(SENT_PERSIST_REQUEST_TOPIC, requestDTO.requestUUId(), requestDTO).get();
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

    @KafkaHandler
    public void listen(PersistDTO order) {
        log.info("Order UUID => {}, The bill is => {}", order.requestUUId(), order.status());
        sendToShipping(order);
        sentToInformation(order);
    }

    private void sendToShipping(PersistDTO order){
        var message = ShippingRequestDTO.builder()
                .requestUUId(order.requestUUId())
                .customerId(order.customerId())
                .orderId(order.orderId())
                .build();
        SendResult<String, ShippingRequestDTO> result = null;
        try {
            result = kafkaTemplateShipping.send(SENT_SHIPPING_ORDER_REQUEST_TOPIC, order.requestUUId(), message).get();
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
            result = kafkaTemplateInformation.send(PAYED_ORDER_REQUEST_TOPIC, order.requestUUId(), message).get();
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

