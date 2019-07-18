package com.bridgelabz.fundoo.user.service;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.bridgelabz.fundoo.user.model.EmailId;

@Component
public class RabbitMqProvider {

	@Autowired
	private AmqpTemplate amqTemplate;
	
	@Autowired
	private JavaMailSender sender;
	
	@Autowired
	private Environment environment;
	
	public void sendMessageToQueue(EmailId mailModel) 
	{
	final String exchange="rabbitMq";//environment.getProperty("spring.rabbitmq.exchange"); 
	final String routingKey="RoutingKey";//environment.getProperty("spring.rabbitmq.routingKey");
	amqTemplate.convertAndSend(exchange, routingKey, mailModel);

	}

	@RabbitListener(queues = "${jsa.rabbitmq.queue}")
	public void send(EmailId email) {

	SimpleMailMessage message = new SimpleMailMessage(); 
	   message.setTo(email.getTo()); 
	   message.setSubject(email.getSubject()); 
	   message.setText(email.getBody());
	   
	   System.out.println("Sending Email ");
	 
	   sender.send(message);

	   System.out.println("Email Sent Successfully!!");
	}
	
}
