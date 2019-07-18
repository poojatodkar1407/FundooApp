package com.bridgelabz.fundoo.user.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.bridgelabz.fundoo.user.model.EmailId;
import com.bridgelabz.fundoo.utility.Utility;

@Component
public class RabbitMqReceiver {
 
	@RabbitListener(queues = "${jsa.rabbitmq.queue}")
	public void  receiveMessage(EmailId mailModel)
	{
	Utility.send(mailModel.getTo(), mailModel.getSubject(), mailModel.getBody());
	}
}
