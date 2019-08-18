package ch.epfl.alpano;

import java.util.Objects;

public final class PanoramaParameters {
    private final GeoPoint observerPosition;
    private final int observerElevation;
    private final double centerAzimuth;
    private final double horizontalFieldOfView;
    private final int maxDistance;
    private final int width;
    private final int height;
    private final double delta;

    public PanoramaParameters(GeoPoint observerPosition, int observerElevation,
            double centerAzimuth, double horizontalFieldOfView, int maxDistance,
            int width, int height) {
        Preconditions.checkArgument(
                Azimuth.isCanonical(centerAzimuth) && horizontalFieldOfView > 0
                        && horizontalFieldOfView <= Math2.PI2 && maxDistance > 0
                        && width > 0 && height > 0);
        this.observerPosition = Objects.requireNonNull(observerPosition);
        this.observerElevation = observerElevation;
        this.centerAzimuth = centerAzimuth;
        this.horizontalFieldOfView = horizontalFieldOfView;
        this.maxDistance = maxDistance;
        this.width = width;
        this.height = height;
        this.delta = horizontalFieldOfView / width;

    }

    public GeoPoint observerPosition() {
        return observerPosition;
    }

    public int observerElevation() {
        return observerElevation;
    }

    public double centerAzimuth() {
        return centerAzimuth;
    }

    public double horizontalFieldOfView() {
        return horizontalFieldOfView;
    }

    public int maxDistance() {
        return maxDistance;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public double verticalFieldOfView() {
        return horizontalFieldOfView * (height - 1) / (width - 1);
    }

    public double azimuthForX(double x) {
        Preconditions.checkArgument(x >= 0 && x <= width - 1);
        return centerAzimuth - (width / 2 + x) * delta;
    }

    public double xForAzimuth(double a) {
        Preconditions.checkArgument(a / delta);
    }
}
