package com.bridgelabz.fundoo.utility;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.bridgelabz.fundoo.user.model.MailModel;

public class RabbitMqImpli implements RabbitMq{

	@Autowired
	private AmqpTemplate rabbitTemplate;

	@Autowired
	private JavaMailSender javaMailSender;
	
	//PRODUCER
	@Override
	public void sendMessageToQueue(MailModel Model) {
		final String exchange = "QueueExchangeConn";
		final String routingKey = "RoutingKey";
		rabbitTemplate.convertAndSend(exchange, routingKey, Model);
		
	}

	//listener
	@Override
	@RabbitListener(queues = "${spring.rabbitmq.template.default-receive-queue}")
	public void receiveMessage(MailModel email) {
		send(email);
	}

	@Override
	@RabbitListener(queues = "${spring.rabbitmq.template.default-receive-queue}")
	public void send(MailModel email) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email.getTo());
		message.setFrom(email.getFrom());
		message.setSubject(email.getSubject());
		message.setText(email.getBody());
		javaMailSender.send(message);
		System.out.println("Mail Sent Succesfully");
	}

}
