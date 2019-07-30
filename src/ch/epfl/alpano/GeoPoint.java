package ch.epfl.alpano;

import static ch.epfl.alpano.Distance.EARTH_RADIUS;
import static ch.epfl.alpano.Math2.PI2;
import static ch.epfl.alpano.Math2.haversin;
import static java.lang.Math.PI;
import static java.lang.Math.asin;
import static java.lang.Math.atan;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

public final class GeoPoint {
    final double longitude, latitude;

    public GeoPoint(double longitude, double latitude) {
        Preconditions.checkArgument(longitude > (-PI) && longitude < PI);
        Preconditions
                .checkArgument(latitude > (-PI / 2.) && latitude < PI / 2.);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double longitude() {
        return longitude;
    }

    public double latitude() {
        return latitude;
    }

    public double distanceTo(GeoPoint that) {
        return EARTH_RADIUS * 2
                * asin(sqrt(haversin(this.latitude - that.latitude)
                        + cos(this.latitude) * cos(that.latitude)
                                * haversin(this.longitude - that.longitude)));
    }

    double azimuthTo(GeoPoint that) {
        return atan((sin(this.longitude - that.longitude) * cos(that.latitude))
                / (cos(this.latitude) * sin(that.latitude)
                        - sin(this.latitude) * cos(that.latitude)
                                * cos(this.longitude - that.longitude)));
    }

    @Override
    public String toString() {
        return String.format("(%.4f, %.4f)", longitude / PI2 * 360,
                latitude / PI2 * 360);
    }
}
