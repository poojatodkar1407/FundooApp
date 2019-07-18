package com.bridgelabz.fundoo.utility;



import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;



public class RabbitMqUtility {
	@Component
	public class QueueProducer {
	 protected Logger logger = LoggerFactory.getLogger(getClass());
	 @Value("${fanout.exchange}")
	 private String fanoutExchange;
	 private final RabbitTemplate rabbitTemplate;
	 @Autowired
	 public QueueProducer(RabbitTemplate rabbitTemplate) {
	  super();
	  this.rabbitTemplate = rabbitTemplate;
	 }
//	 public void produce(NotificationRequestDTO notificationDTO) throws Exception {
//	  logger.info("Storing notification...");
//	  rabbitTemplate.setExchange(fanoutExchange);
//	  rabbitTemplate.convertAndSend(new ObjectMapper().writeValueAsString(notificationDTO));
//	  logger.info("Notification stored in queue sucessfully");
//	 }
	}
}
