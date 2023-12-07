package com.example.demo.instant;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import java.time.Instant;

public class JacksonConverter implements Converter<String, Instant> {
    @Override
    public Instant convert(String s) {
        Long milliseconds = Long.valueOf(s);
        return Instant.ofEpochMilli(milliseconds);
    }

    @Override
    public JavaType getInputType(TypeFactory typeFactory) {
        return typeFactory.constructType(String.class);
    }

    @Override
    public JavaType getOutputType(TypeFactory typeFactory) {
        return typeFactory.constructType(Instant.class);
    }
}
