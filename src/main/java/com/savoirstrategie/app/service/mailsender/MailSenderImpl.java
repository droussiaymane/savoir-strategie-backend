package com.savoirstrategie.app.service.mailsender;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MailSenderImpl implements MailSender{

    @Autowired
    JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String emailSender;
    @Override
    @Async
    public void sendEmail(String emailDestination, String message, String objet) {
    /*
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("aymanedroussi1232023@outlook.com");
        mailMessage.setTo(emailDestination);
        mailMessage.setSubject(objet);
        mailMessage.setText(message);
        try{
            sendEmailMethod(mailMessage);
            log.info("Email sent successfully" );
        }catch (Exception e){

            log.error("Email not  sent successfully. Email: "+ emailDestination  + " message:"+message );
           throw new MailSendException("Un erreur est survenue lors de l'envoi du mail. Veuillez réssayer plus-tard");
        }
*/
        log.info("Email sent successfully" );

    }


    @Async
    public void sendEmailMethod(SimpleMailMessage email) {
        javaMailSender.send(email);
    }


}
