package ch.erni.weather.sensors;

import ch.erni.weather.sensors.external.VendorA;
import ch.erni.weather.sensors.external.VendorB;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

public class WeatherStationSensorsTest {

    private WeatherStationSensors weatherStationSensors;
    private VendorADummy dummyA;
    private VendorBDummy dummyB;

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() {
        dummyA = new VendorADummy();
        dummyB = new VendorBDummy();
        weatherStationSensors = new WeatherStationSensors(dummyA, dummyB);
    }

    @Test
    public void validatesUriWhenAddingSensor() {
        weatherStationSensors.addSensor("id1", SensorType.TEMPERATURE, "a:test");
        weatherStationSensors.addSensor("id2", SensorType.TEMPERATURE, "b:test");

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Cannot handle URI: c:invalid");
        weatherStationSensors.addSensor("id3", SensorType.TEMPERATURE, "c:invalid");
    }

    @Test
    public void temperatureSensorVendorA() {
        checkTemperatureSensor("a:test", dummyA.getValues());
    }

    @Test
    public void temperatureSensorVendorB() {
        checkTemperatureSensor("b:test", dummyB.getValues());
    }

    private void checkTemperatureSensor(String uri, Map<String, Double> values) {
        weatherStationSensors.addSensor("id", SensorType.TEMPERATURE, uri);

        checkSingleValue(weatherStationSensors.readSensorValues(), "id", null, false, "°C");

        values.put(uri, 27.3);
        checkSingleValue(weatherStationSensors.readSensorValues(), "id", 27.3, true, "°C");

        values.put(uri, -274.0);
        checkSingleValue(weatherStationSensors.readSensorValues(), "id", -274.0, false, "°C");

        values.put(uri, 200.2);
        checkSingleValue(weatherStationSensors.readSensorValues(), "id", 200.2, false, "°C");
    }

    @Test
    public void windSpeedSensorVendorA() {
        checkWindSpeedSensor("a:test", dummyA.getValues());
    }

    @Test
    public void windSpeedSensorVendorB() {
        checkWindSpeedSensor("b:test", dummyB.getValues());
    }

    private void checkWindSpeedSensor(String uri, Map<String, Double> values) {
        weatherStationSensors.addSensor("id", SensorType.WIND_SPEED, uri);

        values.put(uri, 27.3);
        checkSingleValue(weatherStationSensors.readSensorValues(), "id", 27.3, true, "km/h");

        values.put(uri, -Double.MIN_VALUE);
        checkSingleValue(weatherStationSensors.readSensorValues(), "id", -Double.MIN_VALUE, false, "km/h");
    }

    @Test
    public void windDirectionSensorVendorA() {
        checkWindDirectionSensor("a:test", dummyA.getValues());
    }

    @Test
    public void windDirectionSensorVendorB() {
        checkWindDirectionSensor("b:test", dummyB.getValues());
    }

    private void checkWindDirectionSensor(String uri, Map<String, Double> values) {
        weatherStationSensors.addSensor("id", SensorType.WIND_DIRECTION, uri);

        values.put(uri, -Math.PI);
        checkSingleValue(weatherStationSensors.readSensorValues(), "id", -Math.PI, true, "");

        values.put(uri, Math.PI);
        checkSingleValue(weatherStationSensors.readSensorValues(), "id", Math.PI, true, "");

        double justAbovePi = Math.nextAfter(Math.PI, 4.0);
        values.put(uri, justAbovePi);
        checkSingleValue(weatherStationSensors.readSensorValues(), "id", justAbovePi, false, "");
        values.put(uri, -justAbovePi);
        checkSingleValue(weatherStationSensors.readSensorValues(), "id", -justAbovePi, false, "");
    }

    @Test
    public void humiditySensorVendorA() {
        checkHumiditySensor("a:test", dummyA.getValues());
    }

    @Test
    public void humiditySensorVendorB() {
        checkHumiditySensor("b:test", dummyB.getValues());
    }

    private void checkHumiditySensor(String uri, Map<String, Double> values) {
        weatherStationSensors.addSensor("id", SensorType.HUMIDITY, uri);

        values.put(uri, 45.0);
        checkSingleValue(weatherStationSensors.readSensorValues(), "id", 45.0, true, "%");

        values.put(uri, 100.1);
        checkSingleValue(weatherStationSensors.readSensorValues(), "id", 100.1, false, "%");

        values.put(uri, -0.1);
        checkSingleValue(weatherStationSensors.readSensorValues(), "id", -0.1, false, "%");
    }

    @Test
    public void multipleSensors() {
        weatherStationSensors.addSensor("A", SensorType.TEMPERATURE, "a:test");
        weatherStationSensors.addSensor("B", SensorType.HUMIDITY, "b:test");

        dummyA.getValues().put("a:test", 27.3);
        dummyB.getValues().put("b:test", 56.0);
        Map<String, SensorValue> values = weatherStationSensors.readSensorValues();
        assertEquals(2, values.size());
        checkValue(values, "A", 27.3, true, "°C");
        checkValue(values, "B", 56.0, true, "%");
    }

    @Test
    public void errorHandling() {
        weatherStationSensors.addSensor("A", SensorType.TEMPERATURE, "a:test");
        weatherStationSensors.addSensor("B", SensorType.HUMIDITY, "b:test");

        dummyA.getValues().put("a:test", 27.3);
        dummyB.getValues().put("b:test", 56.0);
        dummyB.setThrowConnectError(true);

        Map<String, SensorValue> values = weatherStationSensors.readSensorValues();
        assertEquals(2, values.size());
        checkValue(values, "A", 27.3, true, "°C");
        checkValue(values, "B", null, false, "%");
    }

    private void checkSingleValue(Map<String, SensorValue> values, String id, Double number, boolean valid, String unit) {
        assertEquals(1, values.size());
        checkValue(values, id, number, valid, unit);
    }

    private void checkValue(Map<String, SensorValue> values, String id, Double number, boolean valid, String unit) {
        SensorValue value = values.get(id);
        assertThat(value.getValue(), equalTo(number));
        assertThat(value.isValid(), equalTo(valid));
        assertThat(value.getUnit(), equalTo(unit));
    }

    static class VendorADummy implements VendorA {

        private final Map<String, Double> values = new HashMap<>();

        public Map<String, Double> getValues() {
            return values;
        }

        @Override
        public boolean canHandleUri(String uri) {
            return uri.startsWith("a:");
        }

        @Override
        public double readDoubleValue(String uri) throws IOException {
            Double value = values.get(uri);
            if (value == null) {
                throw new IOException("error");
            }
            return value;
        }
    }

    static class VendorBDummy implements VendorB {

        private final Map<String, Double> values = new HashMap<>();
        private boolean throwConnectError;

        public Map<String, Double> getValues() {
            return values;
        }

        public void setThrowConnectError(boolean throwConnectError) {
            this.throwConnectError = throwConnectError;
        }

        @Override
        public boolean acceptsUri(String uri) {
            return uri.startsWith("b:");
        }

        @Override
        public Connection connect() throws IOException {
            if (throwConnectError) {
                throw new IOException("Failed to connect");
            }
            return new Connection() {
                @Override
                public double readDoubleValue(String url) throws IOException {
                    Double value = values.get(url);
                    if (value == null) {
                        throw new IOException("error");
                    }
                    return value;
                }

                @Override
                public void close() throws IOException {
                }
            };
        }
    }

}
