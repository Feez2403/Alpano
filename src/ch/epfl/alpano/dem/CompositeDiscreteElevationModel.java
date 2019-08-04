package ch.epfl.alpano.dem;

import static java.util.Objects.requireNonNull;

import ch.epfl.alpano.Interval2D;

public final class CompositeDiscreteElevationModel
        implements DiscreteElevationModel {

    private final DiscreteElevationModel dem1, dem2;
    private final Interval2D extent;

    public CompositeDiscreteElevationModel(DiscreteElevationModel dem1,
            DiscreteElevationModel dem2) {
        this.dem1 = requireNonNull(dem1);
        this.dem2 = requireNonNull(dem2);
        this.extent = dem1.extent().union(dem2.extent());
    }

    @Override
    public void close() throws Exception {
        dem1.close();
        dem2.close();
    }

    @Override
    public Interval2D extent() {
        return extent;
    }

    @Override
    public double elevationSample(int x, int y) {
        try {
            return dem1.elevationSample(x, y);
        } catch (IllegalArgumentException e) {
            return dem2.elevationSample(x, y);
        }
    }

}
