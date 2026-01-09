package com.ethaum.launch.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "launches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Launch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long startupId;

    private String productName;

    private String tagline;

    @Column(length = 3000)
    private String description;

    private String category;

    private int upvotes;

    private int commentsCount;

    private String ownerEmail;

    private LocalDateTime launchedAt;
}
