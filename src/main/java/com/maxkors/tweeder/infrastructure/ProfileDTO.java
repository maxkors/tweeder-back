package com.maxkors.tweeder.infrastructure;

public interface ProfileDTO {
    Long getId();

    String getUsername();

    String getName();

    Long getSubscriptionsCount();

    Long getSubscribersCount();

    Boolean getIsFollowed();
}
