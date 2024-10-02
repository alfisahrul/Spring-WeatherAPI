package com.example.WeatherAPI.controller;
import com.example.WeatherAPI.model.Weather;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping ("/api/weather")
public class WeatherController {

    @Value("${API_KEY}")
    private String API_KEY;

    @GetMapping("/{city}")
    @Cacheable(value = "weather_single", key ="#city")
    public Weather getWeatherByCity(@PathVariable String city) throws JsonProcessingException {
        String apiUrl = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/" + city + "/?key=" + API_KEY;
        RestTemplate restTemplate = new RestTemplate();
        String data = restTemplate.getForObject(apiUrl, String.class);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(data);

            var days = rootNode.path("days");
            var today = days.get(0);
            var condition = rootNode.path("currentConditions");

            String address = rootNode.path("address").asText(null);
            String timezone = rootNode.path("timezone").asText(null);
            String dayDescription = rootNode.path("description").asText(null);
            String sunset = condition.path("sunset").asText(null);
            String sunrise = condition.path("sunrise").asText(null);
            String todayDate = today.path("datetime").asText(null);
            double temperature = fahrenheitToCelsius(today.path("temp").asDouble());

            var todayWeather = new Weather(address,timezone,dayDescription,sunset,sunrise,todayDate,temperature);
            return todayWeather;

        }catch (Exception e){
            throw new RuntimeException("Failed to parse weather data", e);
        }

    }

    public static double fahrenheitToCelsius(double fahrenheit) {
        return (fahrenheit - 32) / 1.8;
    }

}
