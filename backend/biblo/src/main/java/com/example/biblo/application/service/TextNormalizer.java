package com.example.biblo.application.service;

import com.example.biblo.domain.service.Normalizer;
import org.springframework.stereotype.Component;

@Component
public class TextNormalizer implements Normalizer {
    @Override
    public String normalize(String value) {
        return value != null ? value.trim().toLowerCase() : "";
    }
}
