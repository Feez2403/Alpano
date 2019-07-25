package ch.epfl.alpano;

public interface Distance {
    public final static double EARTH_RADIUS = 6371000;
    
    public static double toRadians(double distanceInMeters) {
        return distanceInMeters/EARTH_RADIUS;
    }
    
    public static double toMeters(double distanceInRadians) {
        return distanceInRadians*EARTH_RADIUS;
    }
}
