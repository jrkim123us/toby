package com.okstudio.user.service;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class DummyMailSender implements MailSender {

	@Override
	public void send(SimpleMailMessage arg0) throws MailException {
	}

	@Override
	public void send(SimpleMailMessage... arg0) throws MailException {
	}

}
