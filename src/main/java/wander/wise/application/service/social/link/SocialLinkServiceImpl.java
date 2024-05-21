package wander.wise.application.service.social.link;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wander.wise.application.dto.social.link.CreateSocialLinkRequestDto;
import wander.wise.application.dto.social.link.SocialLinkDto;
import wander.wise.application.mapper.SocialLinkMapper;
import wander.wise.application.model.SocialLink;
import wander.wise.application.model.User;
import wander.wise.application.repository.social.link.SocialLinkRepository;
import wander.wise.application.service.user.UserService;

@Service
@RequiredArgsConstructor
public class SocialLinkServiceImpl implements SocialLinkService {
    private final SocialLinkRepository socialLinkRepository;
    private final SocialLinkMapper socialLinkMapper;
    private final UserService userService;

    @Override
    @Transactional
    public SocialLinkDto save(String email, CreateSocialLinkRequestDto requestDto) {
        User updatedUser = userService.findUserAndAuthorize(requestDto.userId(), email);
        SocialLink newSocialLink = socialLinkMapper.toModel(requestDto);
        newSocialLink.setUser(updatedUser);
        return socialLinkMapper.toDto(socialLinkRepository.save(newSocialLink));
    }

    @Override
    @Transactional
    public SocialLinkDto updateSocialLinkById(Long id, String email,
                                              CreateSocialLinkRequestDto requestDto) {
        userService.findUserAndAuthorize(requestDto.userId(), email);
        SocialLink updatedSocialLink = socialLinkRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find social link by id: " + id));
        updatedSocialLink = socialLinkMapper.updateSocialLinkFromDto(updatedSocialLink, requestDto);
        return socialLinkMapper.toDto(socialLinkRepository.save(updatedSocialLink));
    }

    @Override
    @Transactional
    public void deleteSocialLinkById(Long id, String email) {
        SocialLink deletedSocialLink = socialLinkRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find social link by id: " + id));
        Long userId = deletedSocialLink.getUser().getId();
        userService.findUserAndAuthorize(userId, email);
        socialLinkRepository.deleteById(id);
    }
}
