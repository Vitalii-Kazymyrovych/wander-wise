package wander.wise.application.service.card;

import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import wander.wise.application.dto.ai.AiResponseDto;
import wander.wise.application.dto.card.CardDto;
import wander.wise.application.dto.card.CardSearchParameters;
import wander.wise.application.dto.card.CreateCardRequestDto;
import wander.wise.application.dto.card.ReportCardRequestDto;
import wander.wise.application.dto.card.SearchCardsResponseDto;
import wander.wise.application.dto.maps.LocationDto;
import wander.wise.application.exception.custom.AuthorizationException;
import wander.wise.application.exception.custom.CardSearchException;
import wander.wise.application.mapper.CardMapper;
import wander.wise.application.model.Card;
import wander.wise.application.model.Collection;
import wander.wise.application.model.User;
import wander.wise.application.repository.card.CardRepository;
import wander.wise.application.repository.card.CardSpecificationBuilder;
import wander.wise.application.repository.collection.CollectionRepository;
import wander.wise.application.service.api.ai.AiApiService;
import wander.wise.application.service.api.email.EmailService;
import wander.wise.application.service.api.images.ImageSearchApiService;
import wander.wise.application.service.api.maps.MapsApiService;
import wander.wise.application.service.api.storage.StorageService;
import wander.wise.application.service.user.UserService;

import static wander.wise.application.constants.GlobalConstants.RANDOM;
import static wander.wise.application.constants.GlobalConstants.RM_DIVIDER;
import static wander.wise.application.constants.GlobalConstants.SEPARATOR;
import static wander.wise.application.constants.GlobalConstants.SET_DIVIDER;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private static final double EARTH_RADIUS_KM = 6371;
    private static final int INITIAL_ATTEMPTS = 0;
    private static final int MAX_ATTEMPTS = 1;
    private static final int MIN_ROLES_AMOUNT = 1;
    private final AiApiService aiApiService;
    private final ImageSearchApiService imageSearchApiService;
    private final MapsApiService mapsApiService;
    private final CardRepository cardRepository;
    private final CardMapper cardMapper;
    private final CardSpecificationBuilder cardSpecificationBuilder;
    private final EmailService emailService;
    private final CollectionRepository collectionRepository;
    private final StorageService storageService;
    private final UserService userService;
    @Value("${support.mail.address}")
    private String supportEmail;

    @Override
    @Transactional
    public CardDto createNewCard(String email, CreateCardRequestDto requestDto) {
        User author = userService.findUserEntityByEmail(email);
        if (!author.isBanned()) {
            Card savedCard = cardRepository.save(initializeUsersCard(requestDto, author));
            addToCreatedCardsCollection(email, savedCard);
            return cardMapper.toDto(savedCard);
        } else {
            throw new AuthorizationException("Access denied. User is banned.");
        }
    }

    @Override
    @Transactional
    public CardDto updateById(Long id, String email, CreateCardRequestDto requestDto) {
        Card updatedCard = findCardEntityById(id);
        User updatingUser = userService.findUserEntityByEmail(email);
        if (userHasAuthority(updatingUser, updatedCard)) {
            updatedCard = cardMapper.updateCardFromRequestDto(updatedCard, requestDto);
            updateImageLinks(requestDto, updatedCard);
            return cardMapper.toDto(cardRepository.save(updatedCard));
        } else {
            throw new AuthorizationException("Access denied. User doesn't have "
                    + "authority to update this cards or is banned");
        }
    }

    @Override
    @Transactional
    public CardDto addImagesToCardById(Long id, String email, List<MultipartFile> images) {
        Card updatedCard = findCardEntityById(id);
        User updatingUser = userService.findUserEntityByEmail(email);
        if (userHasAuthority(updatingUser, updatedCard)) {
            String existingLinks = createNewImageLinksString(images, updatedCard);
            updatedCard.setImageLinks(existingLinks);
            return cardMapper.toDto(cardRepository.save(updatedCard));
        } else {
            throw new AuthorizationException("Access denied.");
        }
    }

    @Override
    @Transactional
    public CardDto findById(Long id) {
        return cardMapper.toDto(cardRepository.findById(id)
                .filter(Card::isShown).orElseThrow(() -> new EntityNotFoundException(
                        "Can't find card by id: " + id)));
    }

    @Override
    @Transactional
    public CardDto findByIdAsAdmin(Long id) {
        return cardMapper.toDto(cardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find card by id: " + id)));
    }

    @Override
    @Transactional
    public boolean addCardToSaved(Long id, String email) {
        Card addedCard = findCardEntityById(id);
        Collection updatedSavedCards = getUsersSavedCards(email);
        List<Long> savedCardsIds = getSavedCardsList(updatedSavedCards);
        if (!savedCardsIds.contains(id)) {
            updatedSavedCards.getCards().add(addedCard);
            collectionRepository.save(updatedSavedCards);
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean removeCardFromSaved(Long id, String email) {
        Card removedCard = findCardEntityById(id);
        Collection updatedSavedCards = getUsersSavedCards(email);
        List<Long> savedCardsIds = getSavedCardsList(updatedSavedCards);
        if (savedCardsIds.contains(id)) {
            updatedSavedCards.getCards().remove(removedCard);
            collectionRepository.save(updatedSavedCards);
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional
    public List<CardDto> getRandomCards(Long number) {
        long max = cardRepository.count();
        Set<Card> randomCards = new HashSet<>();
        for (int i = 0; i < number; i++) {
            getRandomCard(max, randomCards);
        }
        return randomCards.stream()
                .map(cardMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public boolean postLike(Long id, String email) {
        Card likedCard = findCardEntityById(id);
        Collection updatedLikedCards = getUsersLikedCards(email);
        if (!updatedLikedCards.getCards().contains(likedCard)) {
            likedCard.setLikes(likedCard.getLikes() + 1);
            Card savedCard = cardRepository.save(likedCard);
            updatedLikedCards.getCards().add(likedCard);
            collectionRepository.save(updatedLikedCards);
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean removeLike(Long id, String email) {
        Card likedCard = findCardEntityById(id);
        Collection updatedLikedCards = getUsersLikedCards(email);
        if (updatedLikedCards.getCards().contains(likedCard)) {
            updatedLikedCards.getCards().remove(likedCard);
            collectionRepository.save(updatedLikedCards);
            likedCard.setLikes(likedCard.getLikes() - 1);
            cardRepository.save(likedCard);
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean hideCard(Long id) {
        Card hiddenCard = findCardEntityById(id);
        if (hiddenCard.isShown()) {
            hiddenCard.setShown(false);
            cardRepository.save(hiddenCard);
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean revealCard(Long id) {
        Card revealedCard = findCardEntityById(id);
        if (!revealedCard.isShown()) {
            revealedCard.setShown(true);
            cardRepository.save(revealedCard);
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id, String email) {
        Card updatedCard = findCardEntityById(id);
        User updatingUser = userService.findUserEntityByEmail(email);
        if (userHasAuthority(updatingUser, updatedCard)) {
            cardRepository.deleteById(id);
        } else {
            throw new AuthorizationException("Access denied.");
        }
    }

    @Override
    @Transactional
    public void report(Long id, String email, ReportCardRequestDto requestDto) {
        Card reportedCard = findCardEntityById(id);
        reportedCard.setReports(reportedCard.getReports() + 1);
        String message = new StringBuilder()
                .append("User email: ").append(email)
                .append(SEPARATOR)
                .append("Card id: ").append(id)
                .append(SEPARATOR)
                .append("Report text: ").append(requestDto.text())
                .append(SEPARATOR)
                .append("Card was reported: ")
                .append(reportedCard.getReports()).append(" times")
                .toString();
        emailService.sendEmail(
                supportEmail,
                "Report for card " + reportedCard.getFullName(),
                message);
        cardRepository.save(reportedCard);
    }

    @Override
    @Transactional
    public SearchCardsResponseDto search(
            Pageable pageable,
            CardSearchParameters searchParams) {
        searchParams = resetTravelDistance(searchParams);
        Specification<Card> cardSpec = cardSpecificationBuilder.build(searchParams);
        List<Card> foundCards = findOrGenerateCards(searchParams, cardSpec,
                pageable, INITIAL_ATTEMPTS);
        LocationDto startLocationCoordinates = mapsApiService
                .getLocationDtoByName(searchParams.startLocation());
        return new SearchCardsResponseDto(pageable.getPageNumber(),
                initializeCardDtos(foundCards, startLocationCoordinates));
    }

    private void getRandomCard(long max, Set<Card> randomCards) {
        Optional<Card> card = cardRepository.findById(RANDOM.nextLong(0, max));
        if (card.isPresent() && card.get().isShown()) {
            randomCards.add(card.get());
        } else {
            getRandomCard(max, randomCards);
        }
    }

    private Collection getUsersLikedCards(String email) {
        return collectionRepository.findAllByUserEmail(email)
                .stream()
                .filter(collection -> collection.getName().equals("Liked cards"))
                .findFirst()
                .get();
    }

    private static List<Long> getSavedCardsList(Collection updatedSavedCards) {
        return updatedSavedCards.getCards()
                .stream()
                .map(Card::getId)
                .toList();
    }

    private Collection getUsersSavedCards(String email) {
        return collectionRepository.findAllByUserEmail(email)
                .stream()
                .filter(collection -> collection.getName().equals("Saved cards"))
                .findFirst()
                .get();
    }

    private String createNewImageLinksString(List<MultipartFile> images, Card updatedCard) {
        String imageLinks = String.join(SET_DIVIDER, images.stream()
                .map(storageService::uploadFile)
                .toList());
        String existingLinks = updatedCard.getImageLinks();
        if (existingLinks.isEmpty()) {
            existingLinks = imageLinks;
        } else {
            existingLinks = existingLinks + SET_DIVIDER + imageLinks;
        }
        return existingLinks;
    }

    private void addToCreatedCardsCollection(String email, Card savedCard) {
        Collection updatedSavedCards = collectionRepository.findAllByUserEmail(email)
                .stream()
                .filter(collection -> collection.getName().equals("Created cards"))
                .findFirst()
                .get();
        updatedSavedCards.getCards().add(savedCard);
        collectionRepository.save(updatedSavedCards);
    }

    private void updateImageLinks(CreateCardRequestDto requestDto, Card updatedCard) {
        if (!updatedCard.getImageLinks().isEmpty()) {
            Arrays.stream(updatedCard.getImageLinks().split(RM_DIVIDER))
                    .filter(link -> !Arrays.stream(requestDto
                            .imageLinks()).toList().contains(link))
                    .forEach(link -> storageService.deleteFile(link
                            .substring(link.lastIndexOf("/") + 1)));
        }
        updatedCard.setImageLinks(String.join(SET_DIVIDER, requestDto.imageLinks()));
    }

    private Card findCardEntityById(Long id) {
        return cardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find card by id: " + id));
    }

    private CardSearchParameters resetTravelDistance(CardSearchParameters searchParameters) {
        switch (searchParameters.travelDistance()[0]) {
            case "Populated locality" ->
                    searchParameters = searchParameters
                            .setTravelDistance(searchParameters.startLocation()
                                    .split(",")[0]);
            case "Country" ->
                    searchParameters = searchParameters
                            .setTravelDistance(searchParameters.startLocation()
                                    .split(",")[1]);
            case "Region" ->
                    searchParameters = aiApiService.defineRegion(searchParameters);
            case "Continent" ->
                    searchParameters = aiApiService.defineContinent(searchParameters);
            default ->
                    searchParameters = searchParameters.setTravelDistance("");
        }
        return searchParameters;
    }

    private List<Card> findOrGenerateCards(
            CardSearchParameters searchParams,
            Specification<Card> cardSpec,
            Pageable pageable,
            int attempts) {
        List<Card> foundCards = findCards(cardSpec).stream()
                .filter(Card::isShown)
                .toList();
        if (foundCards.size() < getRequiredCardsAmount(pageable)
                && isAiCardsRequired(searchParams)
                && attempts < MAX_ATTEMPTS) {
            attempts++;
            generateAndSaveCards(
                    searchParams,
                    getLocationsToExcludeAndTypeMap(
                            searchParams,
                            foundCards));
            return findOrGenerateCards(
                    searchParams,
                    cardSpec,
                    pageable,
                    attempts);
        }
        if (foundCards.isEmpty()) {
            throw new CardSearchException("Couldn't find and generate enough cards, "
                    + "that match provided requirements");
        }
        return getPage(pageable, foundCards);
    }

    private List<Card> findCards(Specification<Card> cardSpec) {
        return cardRepository.findAll(cardSpec)
                .stream()
                .filter(Card::isShown)
                .toList();
    }

    private void generateAndSaveCards(
            CardSearchParameters searchParams,
            Map<String, List<String>> locationsToExcludeAndTypeMap) {
        List<AiResponseDto> responseDtos = aiApiService.getAiResponses(
                searchParams,
                locationsToExcludeAndTypeMap);
        List<Card> generatedCards = aiResponsesToCards(responseDtos);
        if (!generatedCards.isEmpty()) {
            for (Card card : generatedCards) {
                try {
                    cardRepository.save(card);
                } catch (Exception e) {
                    System.out.println("Duplicate entity");
                }
            }
        }
    }

    private List<Card> aiResponsesToCards(List<AiResponseDto> responseDtos) {
        List<Card> generatedCards = responseDtos.stream()
                .map(this::initialiseCard)
                .filter(Objects::nonNull)
                .toList();
        return generatedCards;
    }

    private Card initialiseCard(AiResponseDto aiResponseDto) {
        String fullName = aiResponseDto.fullName();
        if (cardRepository.existsByFullName(fullName)) {
            return null;
        }
        String searchKey = getSearchKey(fullName);
        LocationDto locationDto = mapsApiService
                .getLocationDtoByName(searchKey);
        if (isValidLocation(locationDto)) {
            return fillNewCard(
                    aiResponseDto,
                    searchKey,
                    locationDto);
        }
        return null;
    }

    private Card fillNewCard(
            AiResponseDto aiResponseDto,
            String searchKey,
            LocationDto locationDto) {
        String imageLinks = imageSearchApiService.getImageLinks(searchKey);
        Card newCard = cardMapper.aiResponseToCard(aiResponseDto);
        newCard.setImageLinks(imageLinks);
        newCard.setMapLink(locationDto.mapLink());
        newCard.setLatitude(locationDto.latitude());
        newCard.setLongitude(locationDto.longitude());
        return newCard;
    }

    private List<CardDto> initializeCardDtos(
            List<Card> foundCards,
            LocationDto startLocationCoordinates) {
        return foundCards.stream()
                .map(card -> {
                    CardDto cardDto = cardMapper.toDto(card);
                    cardDto.setDistance(findDistance(card, startLocationCoordinates));
                    return cardDto;
                })
                .toList();
    }

    private int findDistance(
            Card card,
            LocationDto startLocationCoordinates) {
        // Parse coordinates
        double startLatitude = startLocationCoordinates.latitude();
        double startLongitude = startLocationCoordinates.longitude();
        double endLatitude = card.getLatitude();
        double endLongitude = card.getLongitude();
        // Calculate difference
        double latitudeDifference = Math.toRadians(endLatitude - startLatitude);
        double longitudeDifference = Math.toRadians(endLongitude - startLongitude);
        // Find distance
        double a = Math.sin(latitudeDifference / 2) * Math.sin(latitudeDifference / 2)
                + Math.cos(Math.toRadians(startLatitude)) * Math.cos(Math.toRadians(endLatitude))
                * Math.sin(longitudeDifference / 2) * Math.sin(longitudeDifference / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return (int) (EARTH_RADIUS_KM * c);
    }

    private Card initializeUsersCard(CreateCardRequestDto requestDto, User author) {
        Card newCard = cardMapper.toModel(requestDto);
        newCard.setAuthor(author.getPseudonym());
        LocationDto locationDto = mapsApiService.getMapsResponseByUsersUrl(newCard.getMapLink());
        newCard.setMapLink(locationDto.mapLink());
        newCard.setLatitude(locationDto.latitude());
        newCard.setLongitude(locationDto.longitude());
        return newCard;
    }

    private static boolean userHasAuthority(User updatingUser, Card updatedCard) {
        return updatingUser.getAuthorities().size() > MIN_ROLES_AMOUNT
                || (updatedCard.getAuthor().equals(updatingUser.getPseudonym())
                && !updatingUser.isBanned());
    }

    private static Map<String, List<String>> getLocationsToExcludeAndTypeMap(
            CardSearchParameters searchParams,
            List<Card> foundCards) {
        Map<String, List<String>> locationsToExcludeAndTypeMap = new HashMap<>();
        Arrays.stream(searchParams.tripTypes()).forEach(type -> {
            locationsToExcludeAndTypeMap.put(type, new ArrayList<>());
            foundCards.forEach(card -> {
                if (card.getTripTypes().contains(type)) {
                    locationsToExcludeAndTypeMap.get(type).add(getExcludeLocationName(card));
                }
            });
        });
        return locationsToExcludeAndTypeMap;
    }

    private static List<Card> getPage(
            Pageable pageable,
            List<Card> foundCards) {
        int pageStart = (int) pageable.getOffset();
        int pageEnd = Math.min(pageStart + pageable.getPageSize(), foundCards.size());
        return new PageImpl<>(
                foundCards.subList(pageStart, pageEnd),
                pageable,
                foundCards.size()).toList();
    }

    private static int getRequiredCardsAmount(Pageable pageable) {
        return pageable.getPageSize() * (pageable.getPageNumber() + 1);
    }

    private static boolean isAiCardsRequired(CardSearchParameters searchParams) {
        return Arrays.toString(searchParams.author()).contains("AI");
    }

    private static String getExcludeLocationName(Card card) {
        String[] fullNameArray = card.getFullName().split(RM_DIVIDER);
        return new StringBuilder()
                .append(fullNameArray[0])
                .append(" (")
                .append(fullNameArray[1])
                .append(")")
                .toString();
    }

    private static String getSearchKey(String fullName) {
        String[] searchKeyArray = fullName.split(RM_DIVIDER);
        String searchKey = new StringBuilder()
                .append(searchKeyArray[0])
                .append(" ")
                .append(searchKeyArray[1])
                .toString();
        return searchKey;
    }

    private static boolean isValidLocation(LocationDto locationDto) {
        return locationDto.latitude() != 0
                || locationDto.longitude() != 0;
    }
}
