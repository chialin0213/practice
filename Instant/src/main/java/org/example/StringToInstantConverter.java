package org.example;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import java.time.Instant;

@Component
public class StringToInstantConverter implements Converter<String, Instant> {
    @Override
    public Instant convert(String source) {
        Long milliseconds = Long.valueOf(source);
        return Instant.ofEpochMilli(milliseconds);
    }
}
