package ch.epfl.alpano.dem;

import static ch.epfl.alpano.Math2.bilerp;

import java.util.Objects;

import ch.epfl.alpano.Distance;
import ch.epfl.alpano.GeoPoint;

public final class ContinuousElevationModel {
    private DiscreteElevationModel dem;
    private final static double dNS = Distance
            .toMeters(1. / DiscreteElevationModel.SAMPLES_PER_RADIAN);

    public ContinuousElevationModel(DiscreteElevationModel dem) {
        this.dem = Objects.requireNonNull(dem);
    }

    public double elevationAt(GeoPoint p) {
        double x = Distance.toMeters(p.longitude() / dNS);
        double y = Distance.toMeters(p.latitude() / dNS);
        return elevationAtExtent(x, y);
    }

    public double slopeAt(GeoPoint p) {
        double x = Distance.toMeters(p.longitude() / dNS);
        double y = Distance.toMeters(p.latitude() / dNS);
        return slopeAtExtent(x, y);
    }

    private double elevationAtExtent(double x, double y) {
        int x00 = (int) (Math.floor(x));
        int y00 = (int) (Math.floor(y));

        return bilerp(elevationAtExtent(x00, y00),
                elevationAtExtent(x00 + 1, y00),
                elevationAtExtent(x00, y00 + 1),
                elevationAtExtent(x00 + 1, y00 + 1), x - x00, y - y00);
    }

    private double elevationAtExtent(int x, int y) {
        if (dem.extent().contains(x, y))
            return dem.elevationSample(x, y);
        return 0;
    }

    private double slopeAtExtent(double x, double y) {
        int x00 = (int) (x);
        int y00 = (int) (y);
        int x11 = x00 + 1;
        int y11 = y00 + 1;
        if (!(dem.extent().contains(x00, y00)
                && dem.extent().contains(x11, y11)))
            return 0;
        return bilerp(slopeAtSample(x00, y00), slopeAtSample(x00 + 1, y00),
                slopeAtSample(x00, y00 + 1), slopeAtSample(x00 + 1, y00 + 1),
                x - x00, y - y00);

    }

    private double slopeAtSample(int x, int y) {
        double deltaA = elevationAtExtent(x + 1, y) - elevationAtExtent(x, y);
        double deltaB = elevationAtExtent(x, y + 1) - elevationAtExtent(x, y);
        return Math.acos(dNS
                / (Math.sqrt(deltaA * deltaA + deltaB * deltaB + dNS * dNS)));

    }
}
