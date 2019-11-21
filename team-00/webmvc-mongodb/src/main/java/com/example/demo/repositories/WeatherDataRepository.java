package com.example.demo.repositories;

import com.example.demo.repositories.entities.PersistedWeatherData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeatherDataRepository extends CrudRepository<PersistedWeatherData, String> {

    List<PersistedWeatherData> findByOrderByDeleteAtDesc();
}
