package ch.epfl.alpano;

import static ch.epfl.alpano.Math2.PI2;
import static java.lang.Math.PI;

public interface Azimuth {
    static boolean isCanonical(double azimuth) {
        return azimuth >= 0 && azimuth < PI2;
    }

    static double canonicalize(double azimuth) {
        return Math2.floorMod(azimuth, PI2);
    }

    static double toMath(double azimuth) {
        Preconditions.checkArgument(isCanonical(azimuth));
        if (azimuth == 0d)
            return 0d;
        return PI2 - azimuth;
    }

    static double fromMath(double angle) {
        return toMath(angle);
    }

    static String toOctantString(double azimuth, String n, String e, String s,
            String w) {
        Preconditions.checkArgument(isCanonical(azimuth));
        StringBuilder sb = new StringBuilder();
        double caz = canonicalize(azimuth);
        if (caz <= 3. * PI / 8. || caz >= 13. * PI / 8.)
            sb.append(n);
        if (caz >= 5. * PI / 8. && caz <= 11. * PI / 8.)
            sb.append(s);
        if (caz >= 1. * PI / 8. && caz <= 7. * PI / 8.)
            sb.append(e);
        if (caz >= 9. * PI / 8. && caz <= 15. * PI / 8.)
            sb.append(w);
        return sb.toString();
    }
}
