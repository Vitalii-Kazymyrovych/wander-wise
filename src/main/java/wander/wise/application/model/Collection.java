package wander.wise.application.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SoftDelete;

@Entity
@Table(name = "collections")
@Setter
@Getter
@RequiredArgsConstructor
@SoftDelete
public class Collection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User user;
    private String name;
    @Column(name = "image_link")
    private String imageLink;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "collection_card",
            joinColumns = @JoinColumn(name = "collection_id"),
            inverseJoinColumns = @JoinColumn(name = "card_id"))
    private Set<Card> cards = new HashSet<>();
    @Column(name = "is_public")
    private boolean isPublic = false;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Collection that)) {
            return false;
        }
        return isPublic == that.isPublic
                && Objects.equals(id, that.id)
                && Objects.equals(user, that.user)
                && Objects.equals(name, that.name)
                && Objects.equals(imageLink, that.imageLink)
                && Objects.equals(cards, that.cards);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                user,
                name,
                imageLink,
                cards,
                isPublic);
    }
}
