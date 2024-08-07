package wander.wise.application;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import wander.wise.application.config.ApisConfigProperties;
import wander.wise.application.dto.ai.AiResponseDto;
import wander.wise.application.dto.card.CardSearchParameters;
import wander.wise.application.service.api.ai.AiApiService;

import java.util.List;

@SpringBootApplication
@EnableConfigurationProperties(ApisConfigProperties.class)
@RequiredArgsConstructor
public class Application implements CommandLineRunner {
    private final AiApiService aiApiService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        CardSearchParameters par = new CardSearchParameters(
                null,
                null,
                null,
                null,
                new String[]{"Kharkiv"},
                null);
        List<AiResponseDto> list = aiApiService.getAiResponses(par, null);
        list.stream().map(AiResponseDto::fullName).forEach(System.out::println);
    }
}
