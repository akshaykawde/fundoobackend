package com.bridgelabz.fundoo.utility;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.bridgelabz.fundoo.user.model.MailModel;

@Component
public class RabbitMqSenderImpl {

	@Autowired
	private AmqpTemplate rabbitTemplate;

	@Autowired
	private JavaMailSender javaMailSender;

	// PRODUCER
	public void sendMessageToQueue(Object object) {
		final String exchange = "rabbitExchange";
		final String routingKey = "routingKey";
		rabbitTemplate.convertAndSend(exchange, routingKey, object);

	}

//	@RabbitListener(queues = "${spring.rabbitmq.template.default-receive-queue}")
//	@Override
//	public void send(MailModel email) {
//		SimpleMailMessage message = new SimpleMailMessage();
//		message.setTo(email.getTo());
//		message.setFrom(email.getFrom());
//		message.setSubject(email.getSubject());
//		message.setText(email.getBody());
//		javaMailSender.send(message);
//		System.out.println("Mail Sent Succesfully");
//	}

	@RabbitListener(queues = "${spring.rabbitmq.template.default-receive-queue}")
	public void sendObject(Object object) {
		System.out.println("object send ");
	}

}
