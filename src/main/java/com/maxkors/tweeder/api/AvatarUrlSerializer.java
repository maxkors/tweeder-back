package com.maxkors.tweeder.api;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

public class AvatarUrlSerializer extends JsonSerializer<String> {

    @Value("${aws.base-url}")
    String baseUrl;

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeObject(baseUrl + value);
    }
}
