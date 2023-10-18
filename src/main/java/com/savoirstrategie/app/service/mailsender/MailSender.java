package com.savoirstrategie.app.service.mailsender;

public interface MailSender {


    public void sendEmail(String emailDestination,String message,String objet);
}
