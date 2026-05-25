package ru.slisarenko.persist.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.slisarenko.entity_library.dto.order.OrderRequestDTO;

import static ru.slisarenko.entity_library.constants.ServiceTopicNames.NEW_ORDERS_REQUEST_TOPIC;

@Slf4j
@Component
public class KafkaListener {

    @KafkaListener(topics = "my-topic", containerFactory = "kafkaListenerContainerFactory")
    public void listenForTypeA(@Payload TypeA message, @Header("__TypeId__") String typeId) {
        // Будет вызван только когда тип TypeA
        handleTypeA(message);
    }

    @KafkaListener(topics = "my-topic", containerFactory = "kafkaListenerContainerFactory")
    public void listenForTypeB(@Payload TypeB message, @Header("__TypeId__") String typeId) {
        handleTypeB(message);
    }

    @KafkaListener(topics = "my-topic", containerFactory = "kafkaListenerContainerFactory")
    public void listenForTypeC(@Payload TypeC message, @Header("__TypeId__") String typeId) {
        handleTypeC(message);
    }
}
