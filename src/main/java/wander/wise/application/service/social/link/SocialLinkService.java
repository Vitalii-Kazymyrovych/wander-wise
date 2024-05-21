package wander.wise.application.service.social.link;

import wander.wise.application.dto.social.link.CreateSocialLinkRequestDto;
import wander.wise.application.dto.social.link.SocialLinkDto;

public interface SocialLinkService {
    SocialLinkDto save(String email, CreateSocialLinkRequestDto requestDto);

    SocialLinkDto updateSocialLinkById(Long id, String email,
                                       CreateSocialLinkRequestDto requestDto);

    void deleteSocialLinkById(Long id, String email);
}
