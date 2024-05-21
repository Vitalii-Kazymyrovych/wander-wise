package wander.wise.application.service.api.email;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import wander.wise.application.exception.custom.EmailServiceException;

@Service
public class EmailServiceImpl implements EmailService {
    @Value("${from.mail.address}")
    private String fromEmail;
    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);
        try {
            javaMailSender.send(message);
        } catch (MailException e) {
            throw new EmailServiceException("Can't send message: " + message, e);
        }
    }
}
