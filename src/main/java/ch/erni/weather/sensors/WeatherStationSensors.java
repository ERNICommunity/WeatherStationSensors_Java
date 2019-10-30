package ch.erni.weather.sensors;

import ch.erni.weather.sensors.external.VendorA;
import ch.erni.weather.sensors.external.VendorB;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeatherStationSensors {

    private final VendorA vendorA;
    private final List<Sensor> sensors = new ArrayList<>();

    public WeatherStationSensors(VendorA vendorA) {
        this.vendorA = vendorA;
    }

    public WeatherStationSensors(VendorA vendorA, VendorB vendorB) {
        this.vendorA = vendorA;
    }

    /**
     * Registers a sensor
     *
     * @param id   a unique ID to identify the sensor
     * @param type the type of the sensor
     * @param uri  a URI specifying how to this sensor can be accessed
     */
    public void addSensor(String id, SensorType type, String uri) {
        if (!vendorA.canHandleUri(uri)) {
            throw new IllegalArgumentException("Cannot handle URI: " + uri);
        }
        sensors.add(new Sensor(id, type, uri));
    }

    /**
     * Reads the current values for each sensor.
     *
     * @return a mapping from sensor IDs to sensor values
     */
    public Map<String, SensorValue> readSensorValues() {
        Map<String, SensorValue> values = new HashMap<>();
        for (Sensor sensor : sensors) {
            Double value;
            boolean valid;
            try {
                value = vendorA.readDoubleValue(sensor.getUri());
                switch (sensor.getType()) {
                    case TEMPERATURE:
                        if (value < -50.00) {
                            // underflow
                            valid = false;
                        } else if (value > 150.0) {
                            // overflow
                            valid = false;
                        } else {
                            valid = true;
                        }
                        break;
                    case WIND_SPEED:
                        valid = value >= 0.0;
                        break;
                    case HUMIDITY:
                        valid = value >= 0.0 && value <= 100.0;
                        break;
                    case WIND_DIRECTION:
                        // value is in range of -PI to +PI
                        valid = value >= -Math.PI && value <= Math.PI;
                        break;
                    default:
                        valid = false;
                        break;
                }
            } catch (IOException e) {
                value = null;
                valid = false;
            }
            String unit;
            switch (sensor.getType()) {
                case TEMPERATURE:
                    unit = "°C";
                    break;
                case HUMIDITY:
                    unit = "%";
                    break;
                case WIND_SPEED:
                    unit = "km/h";
                    break;
                case WIND_DIRECTION:
                    unit = "";
                    break;
                default:
                    throw new IllegalArgumentException("Unknown SensorType: " + sensor.getType());
            }

            values.put(sensor.getId(), new SensorValue(value, valid, unit));
        }
        return values;
    }

}
