package ch.epfl.alpano.dem;

import static ch.epfl.alpano.Azimuth.toMath;
import static ch.epfl.alpano.Distance.toRadians;
import static ch.epfl.alpano.Math2.PI2;
import static ch.epfl.alpano.Math2.lerp;
import static ch.epfl.alpano.Preconditions.checkArgument;
import static java.lang.Math.PI;
import static java.lang.Math.asin;
import static java.lang.Math.ceil;
import static java.lang.Math.cos;
import static java.lang.Math.scalb;
import static java.lang.Math.sin;

import java.util.Objects;

import ch.epfl.alpano.Azimuth;
import ch.epfl.alpano.Distance;
import ch.epfl.alpano.GeoPoint;

public final class ElevationProfile {
    // interpolation step (2^12 = 4096 meters)
    private final static int STEP_BITS = 12;
    private final static double STEP_RAD = Distance
            .toRadians(scalb(1d, STEP_BITS));

    private final ContinuousElevationModel elevationModel;
    private final double length;
    private final double[] lonLats;

    public ElevationProfile(ContinuousElevationModel elevationModel,
            GeoPoint origin, double azimuth, double length) {
        checkArgument(Azimuth.isCanonical(azimuth));
        checkArgument(length > 0);

        this.elevationModel = Objects.requireNonNull(elevationModel);
        this.length = length;
        this.lonLats = computeLonLats(origin.longitude(), origin.latitude(),
                azimuth, length);
    }

    private static double[] computeLonLats(double originLon, double originLat,
            double azimuth, double length) {
        double a = toMath(azimuth);
        double sinLatO = sin(originLat);
        double cosLatOcosA = cos(originLat) * cos(a);
        double sinA = sin(a);

        int pointsCount = (int) ceil(toRadians(length) / STEP_RAD) + 1;
        double[] points = new double[2 * pointsCount];
        for (int i = 0; i < pointsCount; ++i) {
            // from http://williams.best.vwh.net/avform.htm#LL
            double xRad = i * STEP_RAD;
            double sinX = sin(xRad), cosX = cos(xRad);
            double lat = asin(sinLatO * cosX + cosLatOcosA * sinX);
            double lon = (originLon - asin(sinA * sinX / cos(lat)) + PI) % PI2
                    - PI;
            points[2 * i] = lon;
            points[2 * i + 1] = lat;
        }
        return points;
    }

    public double elevationAt(double x) {
        checkArgument(0 <= x && x <= length);
        return elevationModel.elevationAt(positionAt(x));
    }

    public GeoPoint positionAt(double x) {
        checkArgument(0 <= x && x <= length);

        double i = scalb(x, -STEP_BITS);
        int i1 = (int) i, i2 = i1 + 1;
        double p = i - i1;
        double lon = lerp(lonLats[2 * i1], lonLats[2 * i2], p);
        double lat = lerp(lonLats[2 * i1 + 1], lonLats[2 * i2 + 1], p);
        return new GeoPoint(lon, lat);
    }

    public double slopeAt(double x) {
        checkArgument(0 <= x && x <= length);
        return elevationModel.slopeAt(positionAt(x));
    }
}
/*
 * package ch.epfl.alpano.dem;
 * 
 * import static ch.epfl.alpano.Azimuth.toMath; import static
 * ch.epfl.alpano.Math2.PI2; import static java.lang.Math.PI; import static
 * java.lang.Math.asin; import static java.lang.Math.cos; import static
 * java.lang.Math.sin;
 * 
 * import java.util.Objects;
 * 
 * import ch.epfl.alpano.Azimuth; import ch.epfl.alpano.Distance; import
 * ch.epfl.alpano.GeoPoint; import ch.epfl.alpano.Preconditions;
 * 
 * public final class ElevationProfile {
 * 
 * private final ContinuousElevationModel elevationModel; private final double
 * azimuth; private final double length; private final double lambda0, phi0;
 * 
 * public ElevationProfile(ContinuousElevationModel elevationModel, GeoPoint
 * origin, double azimuth, double length) {
 * Preconditions.checkArgument(Azimuth.isCanonical(azimuth),
 * "Azimuth non canonique"); Preconditions.checkArgument(length > 0,
 * "Longueur invalide"); if (origin == null) throw new NullPointerException();
 * 
 * this.elevationModel = Objects.requireNonNull(elevationModel); this.azimuth =
 * azimuth; this.length = length; this.lambda0 = origin.latitude(); this.phi0 =
 * origin.longitude();
 * 
 * }
 * 
 * public double elevationAt(double x) { return
 * elevationModel.elevationAt(positionAt(x)); }
 * 
 * public GeoPoint positionAt(double x0) {
 * Preconditions.checkArgument(isInBounds(x0)); double x =
 * Distance.toRadians(x0); double sinX = sin(x); double alpha =
 * toMath(this.azimuth); double phi = asin(sin(phi0) * cos(x) + cos(phi0) * sinX
 * * cos(alpha)); double lambda = (lambda0 - asin(sin(alpha) * sinX / cos(phi))
 * + PI) % PI2 - PI; return new GeoPoint(phi, lambda); }
 * 
 * public double slopeAt(double x) { return
 * elevationModel.slopeAt(positionAt(x)); }
 * 
 * private boolean isInBounds(double x) { return x >= 0 && x <= length; }
 * 
 * }
 */
