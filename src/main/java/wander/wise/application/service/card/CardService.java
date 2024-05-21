package wander.wise.application.service.card;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import wander.wise.application.dto.card.CardDto;
import wander.wise.application.dto.card.CardSearchParameters;
import wander.wise.application.dto.card.CreateCardRequestDto;
import wander.wise.application.dto.card.ReportCardRequestDto;
import wander.wise.application.dto.card.SearchCardsResponseDto;

public interface CardService {
    CardDto createNewCard(String email, CreateCardRequestDto requestDto);

    CardDto findById(Long id);

    CardDto updateById(Long id, String email, CreateCardRequestDto requestDto);

    CardDto addImagesToCardById(Long id, String email, List<MultipartFile> images);

    SearchCardsResponseDto search(Pageable pageable, CardSearchParameters searchParameters);

    boolean postLike(Long id, String email);

    boolean removeLike(Long id, String email);

    boolean hideCard(Long id);

    boolean revealCard(Long id);

    void deleteById(Long id, String email);

    void report(Long id, String email, ReportCardRequestDto requestDto);

    CardDto findByIdAsAdmin(Long id);

    boolean addCardToSaved(Long id, String email);

    boolean removeCardFromSaved(Long id, String email);
}
