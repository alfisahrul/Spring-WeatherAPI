package com.example.WeatherAPI.model;
import lombok.*;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Data
public class Weather implements Serializable
{
    private String address;
    private String timezone;
    private String description;
    private String sunset;
    private String sunrise;
    private String dateTime;
    private double temp;
}
