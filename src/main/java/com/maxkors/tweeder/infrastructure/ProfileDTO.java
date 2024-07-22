package com.maxkors.tweeder.infrastructure;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.maxkors.tweeder.api.AvatarUrlSerializer;

public interface ProfileDTO {
    Long getId();

    String getUsername();

    String getName();

    @JsonSerialize(using = AvatarUrlSerializer.class, as = String.class)
    String getAvatarUrl();

    Long getSubscriptionsCount();

    Long getSubscribersCount();

    Boolean getIsFollowed();
}
