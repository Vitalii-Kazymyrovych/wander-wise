package wander.wise.application.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import wander.wise.application.model.SocialLink;
import wander.wise.application.model.User;
import wander.wise.application.repository.social.link.SocialLinkRepository;
import wander.wise.application.repository.user.UserRepository;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class SocialLinkRepositoryTest {
    private SocialLink socialLink1;
    private SocialLink socialLink2;
    @Autowired
    private SocialLinkRepository socialLinkRepository;
    @Autowired
    private UserRepository userRepository;
    
    @BeforeEach
    public void setUp() {
        User user = User.builder()
                .pseudonym("user")
                .email("@user")
                .password("uuuuuuuuser")
                .firstName("user")
                .lastName("user")
                .profileImage("image")
                .location("location")
                .bio("bio")
                .build();
        userRepository.save(user);
        socialLink1 = SocialLink.builder()
                .user(user)
                .name("name1")
                .link("link1")
                .build();
        socialLink2 = SocialLink.builder()
                .user(user)
                .name("name2")
                .link("link2")
                .build();
    }

    @Test
    public void save_validData_ReturnSavedCard() {
        //when
        SocialLink saved = socialLinkRepository.save(socialLink1);
        //then
        Assertions.assertThat(saved).isNotNull();
        Assertions.assertThat(saved.getId()).isGreaterThan(0);
    }

    @Test
    public void saveAll_validData_ReturnSavedCard() {
        //when
        List<SocialLink> saved = socialLinkRepository
                .saveAll(List.of(socialLink1, socialLink2));
        //then
        Assertions.assertThat(saved).isNotNull();
        Assertions.assertThat(saved.size()).isGreaterThan(0);
    }

    @Test
    public void findAll_validData_ReturnedSavedCards() {
        //given
        socialLinkRepository.save(socialLink1);
        socialLinkRepository.save(socialLink2);
        //when
        List<SocialLink> foundSocialLinks = socialLinkRepository.findAll();
        //then
        Assertions.assertThat(foundSocialLinks).isNotNull();
        Assertions.assertThat(foundSocialLinks.size()).isEqualTo(2);
    }

    @Test
    public void findById_validData_ReturnedSavedCard() {
        //given
        socialLinkRepository.save(socialLink1);
        //when
        SocialLink found = socialLinkRepository.findById(socialLink1.getId()).get();
        //then
        Assertions.assertThat(found).isNotNull();
    }

    @Test
    public void count_validData_ReturnedAmountOfSavedCards() {
        //given
        socialLinkRepository.save(socialLink1);
        socialLinkRepository.save(socialLink2);
        //when
        Long amount = socialLinkRepository.count();
        //then
        Assertions.assertThat(amount).isEqualTo(2);
    }

    @Test
    public void deleteById_validData_DeletedCard() {
        //given
        socialLinkRepository.save(socialLink1);
        //when
        socialLinkRepository.deleteById(socialLink1.getId());
        Long amount = socialLinkRepository.count();
        //then
        Assertions.assertThat(amount).isEqualTo(0L);
    }
}
