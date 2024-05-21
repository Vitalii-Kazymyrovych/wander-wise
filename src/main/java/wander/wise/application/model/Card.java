package wander.wise.application.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SoftDelete;

@Entity
@Table(name = "cards")
@Setter
@Getter
@RequiredArgsConstructor
@SoftDelete
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "full_name", nullable = false, unique = true)
    private String fullName;
    @Column(nullable = false)
    private String author = "AI";
    @Column(name = "trip_types", nullable = false)
    private String tripTypes;
    @Column(nullable = false)
    private String climate;
    @Column(name = "special_requirements", nullable = false)
    private String specialRequirements;
    @Column(nullable = false)
    private String description;
    @Column(name = "why_this_place", nullable = false)
    private String whyThisPlace;
    @Column(name = "image_links", nullable = false)
    private String imageLinks = "";
    @Column(name = "map_link", nullable = false)
    private String mapLink;
    @Column(nullable = false)
    private double latitude;
    @Column(nullable = false)
    private double longitude;
    private Long likes = 0L;
    private Long reports = 0L;
    @OneToMany(mappedBy = "card", fetch = FetchType.EAGER)
    private Set<Comment> comments = new HashSet<>();
    private boolean shown = true;

    public Card(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Card card)) {
            return false;
        }
        return Objects.equals(id, card.id) && Objects.equals(fullName, card.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName);
    }
}
