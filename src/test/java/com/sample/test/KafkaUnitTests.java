package com.sample.test;

import com.sample.consumer.MessageListener;
import com.sample.payload.ApiResponse;
import com.sample.payload.Message;
import com.sample.service.MessagingService;
import com.sample.util.RestURIConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KafkaUnitTests extends BaseTest {

	private static final Logger logger = LoggerFactory.getLogger(KafkaUnitTests.class);

	@Autowired
	private MessageListener receiver;

	private int iterationCount = 10;
	private CountDownLatch latch;

	@Autowired
	MessagingService messagingService;

	@Before
	public void before() {
		super.setUp();
	}

	@After
	public void after() {
	}

	@Test
	public void sendMessagesTest() {
		try {
			latch = new CountDownLatch(iterationCount);
			logger.info("========== Production iteration ==========");
			IntStream.range(0, iterationCount)
					.forEach(i -> messagingService.sendMessage(
							new Message(UUID.randomUUID().toString(), "Msg-"+i, "A Sample message "+i))
					);
			logger.info("========== Iteration completd ==========");
			latch.await(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testSendRest() {
		ApiResponse response = null;
		try {
			logger.info("Call rest ...");
			response = getResponseObject(performRestCallPOST(
					RestURIConstants.MESSAGING + RestURIConstants.MESSAGING_SEND,
					new Message(UUID.randomUUID().toString(), "Msg", "A Sample message")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertTrue(response.getSuccess());
	}

	@Test
	public void testReceive() throws Exception {
		messagingService.sendMessage(new Message(UUID.randomUUID().toString(), "Msg",
				"A Sample message"));

		receiver.getLatch().await(5, TimeUnit.MILLISECONDS);
		assertTrue(receiver.getLatch().getCount() == 0);
	}

}
