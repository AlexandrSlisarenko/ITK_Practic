package ru.slisarenko.persist.listener;

import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.slisarenko.entity_library.dto.ShopOrderInformationStatusDTO;
import ru.slisarenko.entity_library.dto.payment.PaymentRequestDTO;
import ru.slisarenko.entity_library.dto.persist.PersistDTO;
import ru.slisarenko.entity_library.dto.shipping.ShippingRequestDTO;
import ru.slisarenko.persist.service.ShopService;

import static ru.slisarenko.entity_library.constants.ServiceTopicNames.PAYED_ORDER_REQUEST_TOPIC;
import static ru.slisarenko.entity_library.constants.ServiceTopicNames.SENT_NOTIFICATION_TOPIC;
import static ru.slisarenko.entity_library.constants.ServiceTopicNames.SENT_PERSIST_REQUEST_TOPIC;
import static ru.slisarenko.entity_library.constants.ServiceTopicNames.SENT_SHIPPING_ORDER_REQUEST_TOPIC;

@Slf4j
@Component
@RequiredArgsConstructor
public class Listener {

    private final ShopService shopService;
    private final KafkaTemplate<String, ShopOrderInformationStatusDTO> kafkaTemplateShopOrderInformation;
    private final KafkaTemplate<String, PaymentRequestDTO> kafkaTemplatePaymentRequest;
    private final KafkaTemplate<String, ShippingRequestDTO> kafkaTemplateShippingRequest;

    @KafkaListener(topics = SENT_PERSIST_REQUEST_TOPIC, containerFactory = "kafkaListenerContainerFactory")
    public void listenForPersistDTO(@Payload PersistDTO message) {
        var response = this.shopService.createOrder(message);
        sentToInformation(response);
        sentToPayment(response);
    }

    @KafkaListener(topics = SENT_PERSIST_REQUEST_TOPIC, containerFactory = "kafkaListenerContainerFactory")
    public void listenForPersistDTO(@Payload PaymentRequestDTO message) {
        var response = this.shopService.payOrder(message);
        sentToShipping(response);
        sentToInformation(response);
    }

    private void sentToInformation(PersistDTO order) {
        var message = ShopOrderInformationStatusDTO.builder()
                .orderId(order.orderId())
                .requestUUId(order.requestUUId())
                .status(order.status())
                .build();
        SendResult<String, ShopOrderInformationStatusDTO> result = null;
        try {
            result = kafkaTemplateShopOrderInformation.send(SENT_NOTIFICATION_TOPIC, order.requestUUId(), message).get();
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

    private void sentToPayment(PersistDTO order) {
        var message = PaymentRequestDTO.builder()
                .orderId(order.orderId())
                .requestUUId(order.requestUUId())
                .customerId(order.customerId())
                .build();
        SendResult<String, PaymentRequestDTO> result = null;
        try {
            result = kafkaTemplatePaymentRequest.send(PAYED_ORDER_REQUEST_TOPIC, order.requestUUId(), message).get();
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

    private void sentToShipping(PersistDTO order) {
        var message = ShippingRequestDTO.builder()
                .orderId(order.orderId())
                .requestUUId(order.requestUUId())
                .customerId(order.customerId())
                .address("ADDRESS")
                .build();
        SendResult<String, ShippingRequestDTO> result = null;
        try {
            result = kafkaTemplateShippingRequest.send(SENT_SHIPPING_ORDER_REQUEST_TOPIC, order.requestUUId(), message).get();
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

    /*@KafkaListener(topics = "my-topic", containerFactory = "kafkaListenerContainerFactory")
    public void listenForTypeB(@Payload TypeB message, @Header("__TypeId__") String typeId) {
        handleTypeB(message);
    }

    @Listener(topics = "my-topic", containerFactory = "kafkaListenerContainerFactory")
    public void listenForTypeC(@Payload TypeC message, @Header("__TypeId__") String typeId) {
        handleTypeC(message);
    }*/
}
