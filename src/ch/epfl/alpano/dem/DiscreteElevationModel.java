package ch.epfl.alpano.dem;

import static ch.epfl.alpano.Math2.PI2;

import ch.epfl.alpano.Interval2D;

public interface DiscreteElevationModel extends AutoCloseable {
    public static final int SAMPLES_PER_DEGREE = 3600;
    public static final double SAMPLES_PER_RADIAN = SAMPLES_PER_DEGREE * 360
            / PI2;

    public static double sampleIndex(double angle) {
        return angle * SAMPLES_PER_RADIAN;
    }

    public Interval2D extent();

    public double elevationSample(int x, int y);

    default DiscreteElevationModel union(DiscreteElevationModel that) {
        return new CompositeDiscreteElevationModel(this, that);
    }
}
