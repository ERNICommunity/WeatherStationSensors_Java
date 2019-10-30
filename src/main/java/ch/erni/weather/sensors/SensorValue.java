package ch.erni.weather.sensors;

/**
 * A value read from a sensor.
 */
public class SensorValue {
    private final Double value;
    private final boolean valid;
    private final String unit;

    public SensorValue(Double value, boolean valid, String unit) {
        this.value = value;
        this.valid = valid;
        this.unit = unit;
    }

    public Double getValue() {
        return value;
    }

    public boolean isValid() {
        return valid;
    }

    public String getUnit() {
        return unit;
    }
}
