package com.maxkors.tweeder.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.maxkors.tweeder.security.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "chat")
public class Chat {

    @Id
    @SequenceGenerator(name = "chat_seq_gen", sequenceName = "chat_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chat_seq_gen")
    private Long id;

    @JsonIgnore
    @OneToMany(mappedBy = "chat")
    private List<Message> messages;

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "app_user__chat",
            joinColumns = @JoinColumn(name = "chat_id"),
            inverseJoinColumns = @JoinColumn(name = "app_user_id"))
    private Set<User> participants;

    public void addMessage (Message message) {
        this.messages.add(message);
    }
}
