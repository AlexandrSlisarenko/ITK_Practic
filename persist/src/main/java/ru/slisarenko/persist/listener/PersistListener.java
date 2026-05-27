package ru.slisarenko.persist.listener;

import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.slisarenko.entity_library.dto.payment.PaymentRequestDTO;
import ru.slisarenko.entity_library.dto.persist.PersistDTO;
import ru.slisarenko.entity_library.dto.shipping.ShippingRequestDTO;
import ru.slisarenko.persist.service.ShopService;

import static ru.slisarenko.entity_library.constants.ServiceTopicNames.SENT_PERSIST_REQUEST_TOPIC;
import static ru.slisarenko.entity_library.constants.ServiceTopicNames.SENT_PERSIST_RESPONSE_TOPIC;

@Slf4j
@Component
@RequiredArgsConstructor
public class PersistListener {

    private final ShopService shopService;
    private final KafkaTemplate<String, PaymentRequestDTO> kafkaTemplatePaymentRequest;
    private final KafkaTemplate<String, ShippingRequestDTO> kafkaTemplateShippingRequest;
    private final KafkaTemplate<String, PersistDTO> kafkaTemplateOrder;

    @KafkaListener(topics = SENT_PERSIST_REQUEST_TOPIC, containerFactory = "kafkaListenerContainerFactory")
    public void listenForPersistDTO(@Payload Object message) {
        var dtoName = ((ConsumerRecord) message).value();
        switch (dtoName.getClass().getSimpleName()) {
            case "PersistDTO" -> listenPersistDTO((PersistDTO) dtoName);
            case "PaymentRequestDTO" -> listenPaymentRequestDTO((PaymentRequestDTO) dtoName);
            case "ShippingRequestDTO" -> listenShippingRequestDTO((ShippingRequestDTO) dtoName);
        }
    }

    private void listenPersistDTO(PersistDTO message) {
        log.info("PersistDTO: {}", message);
        var response = this.shopService.createOrder(message);
        log.info("createOrder => {}", response);
        sentToOrder(response);
    }

    private void listenPaymentRequestDTO(PaymentRequestDTO message) {
        log.info("PaymentRequestDTO: {}", message);
        var response = this.shopService.payOrder(message);
        log.info("payOrder => {}", response);
        sentToPayment(response);
    }

    private void listenShippingRequestDTO(ShippingRequestDTO message) {
        log.info("ShippingRequestDTO: {}", message);
        var response = this.shopService.deliveryOrder(message);
        log.info("deliveryOrder => {}", response);
        sentToShipping(response);
    }

    private void sentToOrder(PersistDTO order) {
        kafkaTemplateOrder.executeInTransaction(operations -> {
            try {
                SendResult<String, PersistDTO> result = operations
                        .send(SENT_PERSIST_RESPONSE_TOPIC, order.requestUUId(), order).get();
                log.info("result partition {}", result.getRecordMetadata().partition());
                log.info("result offset {}", result.getRecordMetadata().offset());
                log.info("result timestamp {}", result.getRecordMetadata().timestamp());
                log.info("result topic {}", result.getRecordMetadata().topic());
                log.info("result key message {}", result.getProducerRecord().key());
            } catch (InterruptedException | ExecutionException e) {
                log.error(e.getMessage());
                throw new KafkaException(e.getMessage());
            }
            return true;
        });

    }

    private void sentToPayment(PersistDTO order) {
        var message = PaymentRequestDTO.builder()
                .orderId(order.orderId())
                .requestUUId(order.requestUUId())
                .customerId(order.customerId())
                .build();

        kafkaTemplatePaymentRequest.executeInTransaction(operations -> {
            try {
                SendResult<String, PaymentRequestDTO> result = operations
                        .send(SENT_PERSIST_RESPONSE_TOPIC, order.requestUUId(), message).get();
                log.info("result partition {}", result.getRecordMetadata().partition());
                log.info("result offset {}", result.getRecordMetadata().offset());
                log.info("result timestamp {}", result.getRecordMetadata().timestamp());
                log.info("result topic {}", result.getRecordMetadata().topic());
                log.info("result key message {}", result.getProducerRecord().key());
            } catch (InterruptedException | ExecutionException e) {
                log.error(e.getMessage());
                throw new KafkaException(e.getMessage());
            }
            return true;
        });

    }

    private void sentToShipping(PersistDTO order) {
        var message = ShippingRequestDTO.builder()
                .orderId(order.orderId())
                .requestUUId(order.requestUUId())
                .customerId(order.customerId())
                .address("ADDRESS")
                .build();

        kafkaTemplateShippingRequest.executeInTransaction(operations -> {
            try {
                SendResult<String, ShippingRequestDTO> result = kafkaTemplateShippingRequest
                        .send(SENT_PERSIST_RESPONSE_TOPIC, order.requestUUId(), message).get();
                log.info("result partition {}", result.getRecordMetadata().partition());
                log.info("result offset {}", result.getRecordMetadata().offset());
                log.info("result timestamp {}", result.getRecordMetadata().timestamp());
                log.info("result topic {}", result.getRecordMetadata().topic());
                log.info("result key message {}", result.getProducerRecord().key());
            } catch (InterruptedException | ExecutionException e) {
                log.error(e.getMessage());
                throw new KafkaException(e.getMessage());
            }
            return true;
        });

    }


}
