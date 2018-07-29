package com.aldo.kafka.multithreadKafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOffset;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;


@SpringBootApplication
public class MultithreadKafkaApplication {
    public static Logger logger = LoggerFactory.getLogger(MultithreadKafkaApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(MultithreadKafkaApplication.class, args);
    }

//	private final CountDownLatch latch = new CountDownLatch(3);


//	@KafkaListener(topics = "test-topic", groupId = "app-id")
//	public void listen(ConsumerRecord<?, ?> cr) throws Exception {
//
//        Mono.just(cr)
//                .publishOn(Schedulers.newParallel("p", 20))
//                .subscribe(m -> {
//                    logger.info("***************************************");
//                    logger.info("***************************************");
//                    logger.info("***************************************");
//                    logger.info(cr.toString() + " - " + Thread.currentThread().getName());
//                    logger.info("***************************************");
//                    logger.info("***************************************");
//                    logger.info("***************************************");
//                });
//
//
////		latch.countDown();
//	}

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
            ConsumerFactory<String, String> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setConcurrency(5);
        factory.getContainerProperties().setPollTimeout(10_000);
        factory.getContainerProperties().setConsumerTaskExecutor(execC());
        return factory;
    }

    @Bean
    public AsyncListenableTaskExecutor execC() {
        ThreadPoolTaskExecutor tpte = new ThreadPoolTaskExecutor();
        tpte.setCorePoolSize(10);
        return tpte;
    }

    @Bean
    public AsyncListenableTaskExecutor execL() {
        ThreadPoolTaskExecutor tpte = new ThreadPoolTaskExecutor();
        tpte.setCorePoolSize(10);
        return tpte;
    }

    @KafkaListener(topics = "test-topic", groupId = "app-id", containerFactory = "kafkaListenerContainerFactory")
    public void listen(ConsumerRecord<?, ?> record) {
        Mono.just(0).delayElement(Duration.ofMillis(200)).block();
        logger.info("***************************************");
        logger.info("***************************************");
        logger.info("***************************************");
        logger.info(record.toString() + " - " + Thread.currentThread().getName());
        logger.info("***************************************");
        logger.info("***************************************");
        logger.info("***************************************");
    }


}
