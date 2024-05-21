package wander.wise.application.dto.card;

import java.util.Set;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import wander.wise.application.dto.comment.CommentDto;

@Data
@ToString
@RequiredArgsConstructor
public class CardDto {
    private Long id;
    private String name;
    private String author;
    private Set<String> tripTypes;
    private String climate;
    private Set<String> specialRequirements;
    private String whereIs;
    private String description;
    private Set<String> whyThisPlace;
    private Set<String> imageLinks;
    private String mapLink;
    private int distance;
    private Long likes;
    private Set<CommentDto> comments;
    private boolean shown;
}
