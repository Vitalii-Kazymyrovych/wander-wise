package wander.wise.application.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "invalid_jwts")
@Setter
@Getter
@RequiredArgsConstructor
public class InvalidJwt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String jwt;
    @Column(name = "time_stamp", nullable = false)
    private LocalDateTime timeStamp;

    public InvalidJwt(String jwt, LocalDateTime now) {
        this.jwt = jwt;
        this.timeStamp = now;
    }
}
