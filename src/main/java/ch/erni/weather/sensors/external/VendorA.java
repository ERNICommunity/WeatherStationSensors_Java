package ch.erni.weather.sensors.external;

import java.io.IOException;

/**
 * Interface to interact with sensors from VendorA.
 * <p>
 * This is a third-part interface. You cannot change it!
 */
public interface VendorA {
    /**
     * Returns true if the uri belongs to a sensor from VendorA
     */
    boolean canHandleUri(String uri);

    /**
     * Reads the current value from the sensor identified with the given URI.
     *
     * @throws IOException if an error occurs
     */
    double readDoubleValue(String uri) throws IOException;
}
