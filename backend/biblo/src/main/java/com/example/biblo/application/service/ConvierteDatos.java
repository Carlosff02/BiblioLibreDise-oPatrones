package com.example.biblo.application.service;

import com.example.biblo.domain.models.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class ConvierteDatos implements IConvierteDatos{
    private ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public <T> T obtenerDatos(String json, Class<T> clase) {
        try {
            ApiResponse apiResponse = objectMapper.readValue(json, ApiResponse.class);

            if (apiResponse != null && apiResponse.getResults() != null && !apiResponse.getResults().isEmpty()) {

                if (clase.equals(List.class)) {
                    return (T) apiResponse.getResults();
                }

                return (T) apiResponse.getResults().get(0);
            }

            return null;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al deserializar JSON: " + e.getMessage(), e);
        }
    }


}
