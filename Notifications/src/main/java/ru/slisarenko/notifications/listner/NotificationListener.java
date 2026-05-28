package ru.slisarenko.notifications.listner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.slisarenko.entity_library.dto.ShopOrderInformationStatusDTO;

import static ru.slisarenko.entity_library.constants.ServiceTopicNames.SENT_NOTIFICATION_TOPIC;

@Slf4j
@Component
@RequiredArgsConstructor
@KafkaListener(topics = {SENT_NOTIFICATION_TOPIC}, containerFactory = "kafkaListenerContainerFactory")
public class NotificationListener {
    @KafkaHandler
    public void handleSaga(ShopOrderInformationStatusDTO information) {
        log.info("RequestUUID => {}, Order id => {}, Module => {}, Status => {}",
                information.requestUUId(),
                information.orderId(),
                information.moduleName(),
                information.status());
    }
}
