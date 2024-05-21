package wander.wise.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import wander.wise.application.config.MapperConfig;
import wander.wise.application.dto.social.link.CreateSocialLinkRequestDto;
import wander.wise.application.dto.social.link.SocialLinkDto;
import wander.wise.application.model.SocialLink;

@Mapper(config = MapperConfig.class)
public interface SocialLinkMapper {
    SocialLinkDto toDto(SocialLink socialLink);

    SocialLink toModel(CreateSocialLinkRequestDto requestDto);

    SocialLink updateSocialLinkFromDto(@MappingTarget SocialLink socialLink,
                                       CreateSocialLinkRequestDto requestDto);
}
