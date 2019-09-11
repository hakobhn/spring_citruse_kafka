package com.sample.service;

import com.sample.payload.Message;

public interface MessagingService {

    /**
     * Sending A Message To The Kafka Topic
     * @param message
     */
    void sendMessage(Message message);

}
