package ru.slisarenko.shipping.listener;

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
import ru.slisarenko.entity_library.dto.persist.PersistDTO;
import ru.slisarenko.entity_library.dto.shipping.ShippingRequestDTO;
import ru.slisarenko.entity_library.enums.OrderStatus;

import static ru.slisarenko.entity_library.constants.ServiceNames.SHIPPING_MODULE;
import static ru.slisarenko.entity_library.constants.ServiceTopicNames.SENT_NOTIFICATION_TOPIC;
import static ru.slisarenko.entity_library.constants.ServiceTopicNames.SENT_PERSIST_REQUEST_TOPIC;
import static ru.slisarenko.entity_library.constants.ServiceTopicNames.SENT_SHIPPING_ORDER_REQUEST_TOPIC;
import static ru.slisarenko.entity_library.constants.ServiceTopicNames.SENT_SHIPPING_ORDER_RESPONSE_TOPIC;

@Slf4j
@Component
@RequiredArgsConstructor
@KafkaListener(topics = {SENT_SHIPPING_ORDER_REQUEST_TOPIC, SENT_SHIPPING_ORDER_RESPONSE_TOPIC})
public class PaymentListener {
    private final KafkaTemplate<String, ShippingRequestDTO> kafkaTemplatePersist;
    private final KafkaTemplate<String, ShopOrderInformationStatusDTO> kafkaTemplateInformation;

    @KafkaHandler
    public void handleMessage(ShippingRequestDTO requestDTO) {
        log.info("ShippingRequestDTO in Shipping module: {}", requestDTO);

        SendResult<String, ShippingRequestDTO> result = null;
        try {
            result = kafkaTemplatePersist.send(SENT_PERSIST_REQUEST_TOPIC, requestDTO.requestUUId(), requestDTO).get();
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
        log.info("Order UUID => {}", order.requestUUId());
        sentToInformation(order);
        if(order.status().equals(OrderStatus.SHIPPING)){
            sendToComplete(order);
        }

    }

    private void sendToComplete(PersistDTO order) {
        var message = ShopOrderInformationStatusDTO.builder()
                .orderId(order.orderId())
                .requestUUId(order.requestUUId())
                .status(OrderStatus.COMPLETED)
                .moduleName(SHIPPING_MODULE)
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


    private void sentToInformation(PersistDTO order) {
        var message = ShopOrderInformationStatusDTO.builder()
                .orderId(order.orderId())
                .requestUUId(order.requestUUId())
                .status(order.status())
                .moduleName(SHIPPING_MODULE)
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

