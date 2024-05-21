package wander.wise.application.service.api.email;

public interface EmailService {
    void sendEmail(String toEmail, String subject, String body);
}
