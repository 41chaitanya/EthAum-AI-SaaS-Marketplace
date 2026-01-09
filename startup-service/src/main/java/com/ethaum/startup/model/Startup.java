package com.ethaum.startup.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "startups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Startup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String website;

    private String industry;

    private String fundingStage; // Series A / B / C / D

    private String arrRange; // 1M-5M, 5M-10M etc

    @Column(length = 2000)
    private String description;

    @Column(nullable = false)
    private String ownerEmail; // from JWT

    private LocalDateTime createdAt;
}
