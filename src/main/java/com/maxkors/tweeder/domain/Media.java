package com.maxkors.tweeder.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "media")
public class Media {

    @Id
    @SequenceGenerator(name = "media_seq_gen", sequenceName = "media_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "media_seq_gen")
    private Long id;

    @Column(name = "type")
    private String type;

    @Column(name = "urn")
    private String urn;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "tweet_id")
    private Tweet tweet;
}
