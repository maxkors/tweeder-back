package com.maxkors.tweeder.infrastructure;

public interface Profile {
    Long getId();

    String getUsername();

    String getName();

    Long getSubscriptionsCount();

    Long getSubscribersCount();
}
