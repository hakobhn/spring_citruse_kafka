package com.sample.service.impl;


import com.sample.payload.Message;
import com.sample.service.MessagingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service("messagingService")
public class MessagingServiceImpl implements MessagingService {

    private static final Logger logger = LoggerFactory.getLogger(MessagingServiceImpl.class);

    @Autowired
    private KafkaTemplate<String, Object> template;

    @Autowired
    private String sampleTopicName;

    @Override
    public void sendMessage(Message message) {
//        if (template.send(sampleTopicName, message.getId(), message) != null) {
//            logger.info("Message received and passed to kafka. Message: "+message);
//        } else {
//            logger.error("Message passing to kafka failed. Messge: "+message);
//            throw new RuntimeException("Message passing to kafka failed.");
//        }

//        org.springframework.messaging.Message<Message> content = MessageBuilder
//                .withPayload(message)
//                .setHeader(KafkaHeaders.TOPIC, sampleTopicName)
//                .build();
//
//        ListenableFuture<SendResult<String, Object>> future = template.send(content);

        ListenableFuture<SendResult<String, Object>> future = template.send(sampleTopicName, message.getId(), message.toString());

        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {

            @Override
            public void onSuccess(SendResult<String, Object> result) {
                logger.info("Sent message=[" + message +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }
            @Override
            public void onFailure(Throwable ex) {
                logger.info("Unable to send message=["
                        + message + "] due to : " + ex.getMessage());
                throw new RuntimeException("Message passing to kafka failed.", ex);
            }
        });
    }
}
