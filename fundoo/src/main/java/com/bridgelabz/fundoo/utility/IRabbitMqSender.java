package com.bridgelabz.fundoo.utility;

import com.bridgelabz.fundoo.user.model.MailModel;

public interface IRabbitMqSender {

	public void sendMessageToQueue(MailModel rabbitMqDto);

	public void send(MailModel email);

	public void receiveMessage(MailModel email);
}
