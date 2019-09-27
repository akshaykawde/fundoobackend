package com.bridgelabz.fundoo.utility;

import com.bridgelabz.fundoo.user.model.MailModel;

public interface RabbitMq {

	void sendMessageToQueue(MailModel Model); //producer
	void receiveMessage(MailModel Model); //listener
	void send(MailModel Model);
}
