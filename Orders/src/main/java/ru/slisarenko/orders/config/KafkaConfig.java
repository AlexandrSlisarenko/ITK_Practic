package ru.slisarenko.orders.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
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
import ru.slisarenko.entity_library.dto.payment.PaymentRequestDTO;
import ru.slisarenko.entity_library.dto.persist.PersistDTO;
import ru.slisarenko.orders.exception.NonRetryableException;
import ru.slisarenko.orders.exception.RetryableException;

import static ru.slisarenko.entity_library.constants.ServiceTopicNames.NEW_ORDERS_REQUEST_TOPIC;
import static ru.slisarenko.entity_library.constants.ServiceTopicNames.NEW_ORDERS_RESPONSE_TOPIC;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

    private final Environment environment;

    @Bean
    public ProducerFactory<String, OrderRequestDTO> producerFactory() {
        return new DefaultKafkaProducerFactory<>(buildProducerConfigs());
    }

    @Bean
    public ProducerFactory<String, ShopOrderInformationStatusDTO> producerFactoryInformation() {
        return new DefaultKafkaProducerFactory<>(buildProducerConfigs());
    }

    @Bean
    public ProducerFactory<String, PersistDTO> producerFactoryPersist() {
        return new DefaultKafkaProducerFactory<>(buildProducerConfigs());
    }
    @Bean
    public ProducerFactory<String, PaymentRequestDTO> producerFactoryPayment() {
        return new DefaultKafkaProducerFactory<>(buildProducerConfigs());
    }

    @Bean
    KafkaTemplate<String, OrderRequestDTO> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    KafkaTemplate<String, PersistDTO> kafkaTemplatePersist() {
        return new KafkaTemplate<>(producerFactoryPersist());
    }

    @Bean
    KafkaTemplate<String, ShopOrderInformationStatusDTO> kafkaTemplateInformation() {
        return new KafkaTemplate<>(producerFactoryInformation());
    }
    @Bean
    KafkaTemplate<String, PaymentRequestDTO> kafkaTemplatePayment() {
        return new KafkaTemplate<>(producerFactoryPayment());
    }

    private Map<String,Object> buildProducerConfigs() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, environment.getProperty("spring.kafka.producer.bootstrap-servers"));
        configs.put(ProducerConfig.ACKS_CONFIG, environment.getProperty("spring.kafka.producer.acks"));
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, environment.getProperty("spring.kafka.producer.key-serializer"));
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, environment.getProperty("spring.kafka.producer.value-serializer"));
        configs.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, environment.getProperty("spring.kafka.producer.properties.delivery.timeout.ms"));
        configs.put(ProducerConfig.LINGER_MS_CONFIG, environment.getProperty("spring.kafka.producer.properties.linger.ms"));
        configs.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, environment.getProperty("spring.kafka.producer.properties.request.timeout.ms"));
        configs.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, environment.getProperty("spring.kafka.producer.properties.enable.idempotence"));
        configs.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);
        return configs;
    }



    @Bean
    public ConsumerFactory<String, ShopOrderInformationStatusDTO> consumerFactoryShopOrderInformation() {
        return new DefaultKafkaConsumerFactory<>(buildConsumerConfigs());
    }

    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(buildConsumerConfigs());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ShopOrderInformationStatusDTO> kafkaListenerContainerFactory(
            ConsumerFactory<String, ShopOrderInformationStatusDTO> consumerFactory, KafkaTemplate kafkaTemplate) {
        DefaultErrorHandler errorHandler = new DefaultErrorHandler(new DeadLetterPublishingRecoverer(kafkaTemplate),
                new FixedBackOff(3000,3)); // ретрай ошибочного сообщения
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
        configs.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        // пропускает сообщение, которое не смог десерилизовать
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        configs.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JacksonJsonDeserializer.class);
        configs.put(JacksonJsonDeserializer.TRUSTED_PACKAGES,
                environment.getProperty("spring.kafka.consumer.properties.spring.json.trusted.packages"));
        return configs;
    }

    @Bean
    NewTopic createResponseTopic() {
        return TopicBuilder.name(NEW_ORDERS_RESPONSE_TOPIC)
                .partitions(3)
                .replicas(3)
                .configs(Map.of("min.insync.replicas",
                        Objects.requireNonNull(environment.getProperty("spring.kafka.producer.properties.min.insync.replicas"))))
                .build();
    }
}
