package ch.erni.weather.sensors.external;

import java.io.Closeable;
import java.io.IOException;

/**
 * Interface to interact with sensors from VendorB.
 * <p>
 * This is a third-part interface. You cannot change it!
 */
public interface VendorB {
    /**
     * Returns true if the uri belongs to a sensor from VendorB
     */
    boolean acceptsUri(String uri);

    /**
     * Creates a new Connection which is required to read sensor values.
     *
     * @throws IOException if an error occurs
     */
    Connection connect() throws IOException;

    interface Connection extends Closeable {
        /**
         * Reads the current value from the sensor identified with the given URI.
         *
         * @throws IOException if an error occurs
         */
        double readDoubleValue(String uri) throws IOException;
    }
}
