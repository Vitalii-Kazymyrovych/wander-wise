package wander.wise.application.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SoftDelete;

@Entity
@Table(name = "comments")
@Setter
@Getter
@RequiredArgsConstructor
@SoftDelete
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User user;
    @ManyToOne
    private Card card;
    @Column(name = "time_stamp", nullable = false)
    private LocalDateTime timeStamp;
    @Column(nullable = false)
    private String text;
    private Integer stars = 5;
    private Long reports = 0L;
    private boolean shown = true;
}
