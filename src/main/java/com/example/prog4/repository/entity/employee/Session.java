package com.example.prog4.repository.entity.employee;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "\"session\"")
@EqualsAndHashCode
@ToString
public class Session {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private String id;
    @Column(name = "session_id")
    private String sessionId;
    private LocalDateTime timeout;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
