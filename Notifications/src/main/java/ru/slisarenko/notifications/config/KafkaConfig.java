package ru.slisarenko.notifications.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;
import ru.slisarenko.entity_library.dto.ShopOrderInformationStatusDTO;
import ru.slisarenko.entity_library.dto.order.OrderRequestDTO;
import ru.slisarenko.notifications.exception.NonRetryableException;
import ru.slisarenko.notifications.exception.RetryableException;

import static ru.slisarenko.entity_library.constants.ServiceTopicNames.SENT_NOTIFICATION_TOPIC;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {
    private final Environment environment;

    @Bean
    public ProducerFactory<String, Object> deadLetterProducerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, environment.getProperty("spring.kafka.consumer.bootstrap-servers"));
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, environment.getProperty("spring.kafka.producer.key-serializer"));
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, environment.getProperty("spring.kafka.producer.value-serializer"));
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, Object> deadLetterKafkaTemplate() {
        return new KafkaTemplate<>(deadLetterProducerFactory());
    }

    @Bean
    public ConsumerFactory<String, ShopOrderInformationStatusDTO> consumerFactoryInformation() {
        return new DefaultKafkaConsumerFactory<>(buildConsumerConfigs());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ShopOrderInformationStatusDTO> kafkaListenerContainerFactory(
            ConsumerFactory<String, ShopOrderInformationStatusDTO> consumerFactory,
            @Qualifier("deadLetterKafkaTemplate") KafkaTemplate<String, Object> deadLetterKafkaTemplate) {

        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(deadLetterKafkaTemplate);
        DefaultErrorHandler errorHandler = new DefaultErrorHandler(recoverer, new FixedBackOff(3000, 3));

        errorHandler.addNotRetryableExceptions(NonRetryableException.class);
        errorHandler.addRetryableExceptions(RetryableException.class);

        ConcurrentKafkaListenerContainerFactory<String, ShopOrderInformationStatusDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(errorHandler);
        return factory;
    }



    private Map<String,Object> buildConsumerConfigs() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, environment.getProperty("spring.kafka.consumer.bootstrap-servers"));
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, environment.getProperty("spring.kafka.consumer.group-id"));
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        configs.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        configs.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JacksonJsonDeserializer.class);
        configs.put(JacksonJsonDeserializer.VALUE_DEFAULT_TYPE, OrderRequestDTO.class);
        configs.put(JacksonJsonDeserializer.TRUSTED_PACKAGES,
                environment.getProperty("spring.kafka.consumer.properties.spring.json.trusted.packages"));
        return configs;
    }

    @Bean
    NewTopic createRequestTopic() {
        return TopicBuilder.name(SENT_NOTIFICATION_TOPIC)
                .partitions(3)
                .replicas(3)
                .configs(Map.of("min.insync.replicas",
                        Objects.requireNonNull(environment.getProperty("spring.kafka.producer.properties.min.insync.replicas"))))
                .build();
    }
}
