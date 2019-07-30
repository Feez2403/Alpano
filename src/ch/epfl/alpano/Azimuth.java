package ch.epfl.alpano;

public interface Azimuth {
    static boolean isCanonical(double azimuth) {
        return azimuth >= 0 && azimuth < Math2.PI2;
    }

    static double canonicalize(double azimuth) {
        return Math2.floorMod(azimuth, Math2.PI2);
    }

    static double toMath(double azimuth) {
        System.out.println("Given : " + azimuth);
        double angle = Math.toRadians(azimuth);
        Preconditions.checkArgument(isCanonical(angle));
        System.out.println("Returning : " + angle);
        return angle;
    }

    static double fromMath(double angle) {
        Preconditions.checkArgument(isCanonical(angle));
        return angle / Math2.PI2 * 360;
    }

    static String toOctantString(double azimuth, String n, String e, String s,
            String w) {
        Preconditions.checkArgument(isCanonical(azimuth));
        StringBuilder sb = new StringBuilder();
        double caz = canonicalize(azimuth);
        if (caz <= 67.5 || caz >= 292.5)
            sb.append(n);
        if (caz >= 112.5 && caz <= 247.5)
            sb.append(s);
        if (caz >= 22.5 && caz <= 157.5)
            sb.append(e);
        if (caz >= 202.5 && caz <= 337.5)
            sb.append(w);
        return sb.toString();
    }
}
