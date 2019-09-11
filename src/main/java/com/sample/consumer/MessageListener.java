package com.sample.consumer;

import com.sample.payload.Message;
import com.sample.service.HttpRequester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;

@Service
public class MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(MessageListener.class);

    @Autowired
    HttpRequester httpRequester;

    private CountDownLatch latch = new CountDownLatch(0);

    public CountDownLatch getLatch() {
        return latch;
    }

    /**
     * Listener for processing messages
     * @param message
     * @param headers
     */
    @KafkaListener(topics = "${kafka.sample.topic_name}", containerFactory = "kafkaListenerContainerFactory")
    public void receive(@Payload Message message,
                        @Headers MessageHeaders headers) {
        logger.info("received data='{}'", message);

        headers.keySet().forEach(key -> {
            logger.info("{}: {}", key, headers.get(key));
        });

        httpRequester.sendRequest("http://localhost:8088/receive", message);
    }

}