package com.sample.controller;

import com.sample.payload.ApiResponse;
import com.sample.payload.Message;
import com.sample.service.MessagingService;
import com.sample.util.RestURIConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping(RestURIConstants.MESSAGING)
public class MessagingController {

    private static final Logger logger = LoggerFactory.getLogger(MessagingController.class);

    @Autowired
    MessagingService messagingService;

    @PostMapping(RestURIConstants.MESSAGING_SEND)
    public ResponseEntity<?> insert(@RequestBody Message message) {

        try {
            logger.info("Receiving messages.");
            messagingService.sendMessage(message);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "Messages produced successfully"));
        } catch (Exception e) {
            logger.error("Failed to process. "+e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, "Messages production failed.", e.getMessage()));
        }
    }
}
