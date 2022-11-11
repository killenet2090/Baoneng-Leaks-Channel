package com.bnmotor.icv.tsp.vehstatus.config;

import com.google.common.collect.Maps;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.Map;

/**
 * @ClassName: KafkaConfig
 * @Description: kafka配置
 * @author: huangyun1
 * @date: 2020/12/09 10:09 上午
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Configuration
public class StatusSyncKafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String kafkaBootstrapServers;

    @Value("${spring.kafka.listener.poll-timeout}")
    private long pollTimeout;

    @Value("${spring.kafka.consumer.status-sync.enable-auto-commit}")
    private boolean enableAutoCommit;

    @Value("${spring.kafka.consumer.status-sync.concurrency}")
    private int concurrency;

    @Value("${spring.kafka.consumer.key-serializer}")
    private String keySerializer;

    @Value("${spring.kafka.consumer.value-serializer}")
    private String valueSerializer;

    @Value("${spring.kafka.consumer.status-sync.auto-offset-reset}")
    private String autoOffsetReset;

    @Bean
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> statusSyncContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(statusSyncConsumerFactory());
        //设置并发量，小于或等于Topic的分区数
        factory.setConcurrency(concurrency);
        //设置为批量监听
        //factory.setBatchListener(true);
        factory.getContainerProperties().setPollTimeout(pollTimeout);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, String> statusSyncConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(statusSyncConsumerConfigs());
    }

    @Bean
    public Map<String, Object> statusSyncConsumerConfigs() {
        Map<String, Object> props = Maps.newHashMap();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keySerializer);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueSerializer);
        return props;
    }
}
