package ch.epfl.alpano;

import static java.lang.Math.PI;
import static java.lang.Math.scalb;

import java.util.function.DoubleUnaryOperator;

public interface Math2 {

    ////////////// rajoutées/////////////////
    public static int min(int v1, int v2) {
        return (v1 < v2 ? v1 : v2);
    }

    public static int max(int v1, int v2) {
        return (v1 > v2 ? v1 : v2);
    }

    public static int roof(double x) {
        int x0 = (int) x;
        if (x - x0 == 0)
            return x0;
        return x0 + 1;
    }

    ////////////////////////////////////////

    public final static double PI2 = scalb(PI, 1);

    public static double sq(double x) {
        return x * x;
    }

    public static double floorMod(double x, double y) {
        return x - y * (Math.floor(x / y));
    }

    public static double haversin(double x) {
        return sq(Math.sin(x / 2));
    }

    public static double angularDistance(double a1, double a2) {
        return floorMod(a2 - a1 + Math.PI, PI2) - Math.PI;
    }

    public static double lerp(double y0, double y1, double x) {
        return (y1 - y0) * x + y0;
    }

    public static double bilerp(double z00, double z10, double z01, double z11,
            double x, double y) {
        double z1 = lerp(z00, z10, x);
        double z2 = lerp(z01, z11, x);

        return lerp(z1, z2, y);
    }

    public static double firstIntervalContainingRoot(DoubleUnaryOperator f,
            double minX, double maxX, double dX) {
        double x = minX;
        while (x + dX <= maxX) {
            if (!areSameSign(f.applyAsDouble(x), f.applyAsDouble(x + dX))) {

                return x;
            }

            x += dX;
        }
        if (!areSameSign(f.applyAsDouble(maxX - dX), f.applyAsDouble(maxX)))
            ;
        return Double.POSITIVE_INFINITY;
    }

    public static double improveRoot(DoubleUnaryOperator f, double x1,
            double x2, double epsilon) {
        double y1 = f.applyAsDouble(x1);
        double y2 = f.applyAsDouble(x2);
        Preconditions.checkArgument(!areSameSign(y1, y2),
                "f(x1) et f(x2) sont de même signe !");
        double delta = x2 - x1;
        double x0 = x1;
        while (delta > epsilon) {
            delta /= 2;
            double y0 = f.applyAsDouble(x0);
            double ydelta = f.applyAsDouble(x0 + delta);
            if (!areSameSign(y0, ydelta)) {
                continue;
            } else {
                x0 = x0 + delta;
                continue;
            }
        }
        return x0;
    }

    static boolean areSameSign(double y1, double y2) {
        return (y1 * y2 > 0);
    }
}
