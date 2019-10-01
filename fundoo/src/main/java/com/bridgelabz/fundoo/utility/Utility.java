package com.bridgelabz.fundoo.utility;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bridgelabz.fundoo.user.model.MailModel;

@Component
public class Utility {

	@Autowired
	TokenGenerator tokenUtil;

//	@Value("${spring.mail.EmailId}")
//	private String fromEmail;
//
//	@Value("${spring.mail.EmailPassword}")
//	private String password;

	public void send(String toEmail, String subject, String link) {
		System.out.println("fsdfdsfadsfasfd");
		final String fromEmail = "akshay.skawde@gmail.com";
		final String password = "akshay.skawde@070196";
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
//		System.out.println(fromEmail+"           "+password);
		// to check email sender credentials are valid or not
		Authenticator auth = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password);
			}
		};

		javax.mail.Session session = Session.getInstance(props, auth);

		try {
			MimeMessage msg = new MimeMessage(session);
			System.out.println("DEBUGGINGGGGGG1");
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");

			msg.setFrom(new InternetAddress("no_reply@gmail.com", "NoReply"));

			msg.setReplyTo(InternetAddress.parse(fromEmail, false));

			msg.setSubject(subject, "UTF-8");

			msg.setText(link, "UTF-8");

			msg.setSentDate(new Date());
			System.out.println("DEBUGGINGGGGGG2");
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
			System.out.println("DEBUGGINGGGGGG3");
			Transport.send(msg);

			System.out.println("Email Sent Successfully.........");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static String getUrl(Long id) {

		TokenGenerator tokenUtil = new TokenGenerator();

		return "http://localhost:8080/user/" + tokenUtil.createToken(id);
	}

	public static void send1(String toEmail, String subject, String body) {
		final String fromEmail = "akshay.skawde@gmail.com"; // requires valid gmail id
		final String password = "akshay.skawde@070196"; // correct password for gmail id

		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP Host
		props.put("mail.smtp.port", "587"); // TLS Port
		props.put("mail.smtp.auth", "true"); // enable authentication
		props.put("mail.smtp.starttls.enable", "true"); // enable STARTTLS

		// to check email sender credentials are valid or not
		Authenticator auth = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password);
			}
		};

		javax.mail.Session session = Session.getInstance(props, auth);

		try {
			MimeMessage msg = new MimeMessage(session);

			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");

			msg.setFrom(new InternetAddress("no_reply@gmail.com", "NoReply"));

			msg.setReplyTo(InternetAddress.parse("akshay.skawde@gmail.com", false));

			msg.setSubject(subject, "UTF-8");

			msg.setText(body, "UTF-8");

			msg.setSentDate(new Date());

			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
			Transport.send(msg);

			System.out.println("Email Sent Successfully.........");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String sendEmail(MailModel emailId) {

		final String fromEmail = "akshay.skawde@gmail.com"; // requires valid gmail id
		final String password = "akshay.skawde@070196"; // correct password for gmail id
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP Host
		props.put("mail.smtp.port", "587"); // TLS Port
		props.put("mail.smtp.auth", "true"); // enable authentication
		props.put("mail.smtp.starttls.enable", "true"); // enable STARTTLS

		// to check email sender credentials are valid or not
		Authenticator auth = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password);
			}
		};

		javax.mail.Session session = Session.getInstance(props, auth);

		try {
			MimeMessage msg = new MimeMessage(session);

			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");

			msg.setFrom(new InternetAddress("no_reply@gmail.com", "NoReply"));

			msg.setReplyTo(InternetAddress.parse("poojasparkle124@gmail.com", false));

			msg.setSubject(emailId.getSubject(), "UTF-8");

			msg.setText(emailId.getBody(), "UTF-8");

			msg.setSentDate(new Date());

			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailId.getTo(), false));
			Transport.send(msg);

			System.out.println("Email Sent Successfully.........");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

}