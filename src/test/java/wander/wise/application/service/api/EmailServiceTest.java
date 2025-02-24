package wander.wise.application.service.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import wander.wise.application.service.api.email.EmailServiceImpl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {
    @InjectMocks
    private EmailServiceImpl emailService;
    @Mock
    private JavaMailSender javaMailSender;

    @Test
    public void sendEmail_ValidData_ReturnsNothing() {
        doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));
        assertAll(() -> emailService.sendEmail("", "", ""));
    }
}
