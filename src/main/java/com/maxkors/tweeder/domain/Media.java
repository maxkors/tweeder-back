package com.maxkors.tweeder.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.maxkors.tweeder.api.MediaUrlSerializer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "media")
public class Media {

    @Id
    @SequenceGenerator(name = "media_seq_gen", sequenceName = "media_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "media_seq_gen")
    private Long id;

    @Column(name = "type")
    private String type;

//    @JsonIgnore
    @Column(name = "urn")
    @JsonSerialize(using = MediaUrlSerializer.class, as = String.class)
    private String urn;

//    @Transient
//    @JsonIgnore
//    private static String url = "https://tweederstorage.s3.eu-north-1.amazonaws.com/";

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "tweet_id")
    private Tweet tweet;

//    @JsonProperty("uri")
//    public String getUri() {
//        return url + this.urn;
//    }
}
