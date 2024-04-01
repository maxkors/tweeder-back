package com.maxkors.tweeder.api;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessagePayload {
    private String content;
    private Long chatId;
    private String invitedUsername;
}
