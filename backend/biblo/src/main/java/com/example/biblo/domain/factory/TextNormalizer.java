package com.example.biblo.domain.factory;

import org.springframework.stereotype.Component;

@Component
public class TextNormalizer implements Normalizer {
    @Override
    public String normalize(String value) {
        return value != null ? value.trim().toLowerCase() : "";
    }
}
