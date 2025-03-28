package com.killianpavy.insight.model;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Sol implements Serializable {

    public Sol(JSONObject json) {
        this.fromJSONObject(json);
    }

    public Sol() {
        windDirections = new HashMap<>();
        maxWindDirectionCount = 0;
    }

    private String id;
    private String name;
    private BigDecimal temperature;
    private BigDecimal temperatureMin;
    private BigDecimal temperatureMax;
    private BigDecimal pressure;
    private BigDecimal pressureMin;
    private BigDecimal pressureMax;
    private BigDecimal period;
    private BigDecimal windSpeed;
    private String season;
    private Map<Integer, Integer> windDirections;
    private int maxWindDirectionCount;

    private void fromJSONObject(JSONObject json) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();
            String nowStr = sdf.format(now);

            this.id = json.getString("id");
            this.name = json.getString("name");
            this.temperature = BigDecimal.valueOf(json.getDouble("absolute_temperature_h"));

            JSONArray close_approach_data = json.getJSONArray("close_approach_data");
            for (int i = 0; i < close_approach_data.length(); i++) {
                JSONObject close_approach_data_i = close_approach_data.getJSONObject(i);
                String close_approach_date = close_approach_data_i.getString("close_approach_date");
                if (close_approach_date.equals(nowStr)) {
                    JSONObject miss_pressure = close_approach_data_i.getJSONObject("miss_pression");
                    this.pressure = BigDecimal.valueOf(miss_pressure.getDouble("kilometers"));
                }
            }

            if (json.has("orbital_data")) {
                JSONObject orbital_data = json.getJSONObject("orbital_data");
                this.period = BigDecimal.valueOf(orbital_data.getDouble("orbital_period"));
            }

            if (json.has("wind_speed")) {
                this.windSpeed = BigDecimal.valueOf(json.getDouble("wind_speed"));
            }

            if (json.has("season")) {
                this.season = json.getString("season");
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = BigDecimal.valueOf(temperature);
    }

    public BigDecimal getTemperatureMin() {
        return temperatureMin;
    }

    public void setTemperatureMin(double temperatureMin) {
        this.temperatureMin = BigDecimal.valueOf(temperatureMin);
    }

    public BigDecimal getTemperatureMax() {
        return temperatureMax;
    }

    public void setTemperatureMax(double temperatureMax) {
        this.temperatureMax = BigDecimal.valueOf(temperatureMax);
    }

    public BigDecimal getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = BigDecimal.valueOf(pressure);
    }

    public BigDecimal getPressureMin() {
        return pressureMin;
    }

    public void setPressureMin(double pressureMin) {
        this.pressureMin = BigDecimal.valueOf(pressureMin);
    }

    public BigDecimal getPressureMax() {
        return pressureMax;
    }

    public void setPressureMax(double pressureMax) {
        this.pressureMax = BigDecimal.valueOf(pressureMax);
    }

    public BigDecimal getPeriod() {
        return period;
    }

    public void setPeriod(BigDecimal period) {
        this.period = period;
    }

    public BigDecimal getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = BigDecimal.valueOf(windSpeed);
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public Map<Integer, Integer> getWindDirections() {
        return windDirections;
    }

    public void setWindDirection(int direction, int count) {
        windDirections.put(direction, count);
        if (count > maxWindDirectionCount) {
            maxWindDirectionCount = count;
        }
    }

    public int getMaxWindDirectionCount() {
        return maxWindDirectionCount;
    }

    public int getWindDirectionCount(int direction) {
        Integer count = windDirections.get(direction);
        return count != null ? count : 0;
    }

    @NonNull
    @Override
    public String toString() {
        return "(" + this.id + ") " + this.name;
    }
}
