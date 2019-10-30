package ch.erni.weather.sensors;

class Sensor {
    private final String id;
    private final SensorType type;
    private final String uri;

    public Sensor(String id, SensorType type, String uri) {
        this.id = id;
        this.type = type;
        this.uri = uri;
    }

    public String getId() {
        return id;
    }

    public SensorType getType() {
        return type;
    }

    public String getUri() {
        return uri;
    }

}
