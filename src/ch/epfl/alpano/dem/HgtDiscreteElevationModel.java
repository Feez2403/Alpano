/*
package ch.epfl.alpano.dem;

import static ch.epfl.alpano.Preconditions.checkArgument;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel.MapMode;

import ch.epfl.alpano.Interval1D;
import ch.epfl.alpano.Interval2D;

public final class HgtDiscreteElevationModel implements DiscreteElevationModel {
    private final ShortBuffer buffer;
    private final Interval2D extent;

    private static int sign(char c, char pos, char neg) {
        checkArgument(c == pos || c == neg);
        return (c == pos) ? 1 : -1;
    }

    public HgtDiscreteElevationModel(File file) {
        String fileName = file.getName();
        checkArgument(fileName.length() == 11 && fileName.endsWith(".hgt"),
                "invalid HGT file name");

        try (FileInputStream stream = new FileInputStream(file)) {
            char ns = Character.toUpperCase(fileName.charAt(0));
            int minLatDeg = Integer.parseInt(fileName.substring(1, 3))
                    * sign(ns, 'N', 'S');
            char ew = Character.toUpperCase(fileName.charAt(3));
            int minLonDeg = Integer.parseInt(fileName.substring(4, 7))
                    * sign(ew, 'E', 'W');
            long fileLength = file.length();
            checkArgument((SAMPLES_PER_DEGREE + 1) * (SAMPLES_PER_DEGREE + 1)
                    * 2 == fileLength, "invalid HGT file length");

            int minX = minLonDeg * SAMPLES_PER_DEGREE;
            int minY = minLatDeg * SAMPLES_PER_DEGREE;
            Interval1D lonInt = new Interval1D(minX, minX + SAMPLES_PER_DEGREE);
            Interval1D latInt = new Interval1D(minY, minY + SAMPLES_PER_DEGREE);

            this.extent = new Interval2D(lonInt, latInt);
            this.buffer = stream.getChannel()
                    .map(MapMode.READ_ONLY, 0, fileLength).asShortBuffer();
        } catch (NumberFormatException | IndexOutOfBoundsException
                | IOException e) {
            throw new IllegalArgumentException(
                    "invalid HGT file name (or contents)", e);
        }
    }

    @Override
    public Interval2D extent() {
        return extent;
    }

    @Override
    public double elevationSample(int x, int y) {
        checkArgument(extent.contains(x, y));

        int xL = x - extent.iX().includedFrom();
        int yL = -(y - extent.iY().includedTo());
        return buffer.get(yL * (SAMPLES_PER_DEGREE + 1) + xL);
    }

    @Override
    public void close() throws Exception {
    }
}
*/

package ch.epfl.alpano.dem;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel.MapMode;

import ch.epfl.alpano.Interval1D;
import ch.epfl.alpano.Interval2D;
import ch.epfl.alpano.Preconditions;

public final class HgtDiscreteElevationModel implements DiscreteElevationModel {
    private final int lat;
    private final int lon;
    private final static int POINTS_PER_SIDE = 60 * 60 + 1;
    private final static int FILE_LENGTH = POINTS_PER_SIDE * POINTS_PER_SIDE
            * 2;
    private FileInputStream fis;
    private ShortBuffer sbuf;
    private final Interval2D extent;

    public HgtDiscreteElevationModel(File file) {
        String name = file.getName();

        try {
            char ns = name.charAt(0);
            char ew = name.charAt(3);
            int lat = Integer.parseInt(name.substring(1, 3));
            int lon = Integer.parseInt(name.substring(4, 7));
            if (name.length() != 11)
                throw new IllegalArgumentException("Nom invalide");
            if (!name.substring(7, 11).equals(".hgt"))
                throw new IllegalArgumentException(
                        "extension de fichier invalide : "
                                + name.substring(7, 11));
            if (file.length() != FILE_LENGTH)
                throw new IllegalArgumentException("TAILLE INVALIDE");

            if (ns == 'N')
                this.lat = lat;
            else if (ns == 'S')
                this.lat = -lat;
            else
                throw new IllegalArgumentException("NS INVALIDE");
            if (ew == 'E')
                this.lon = lon;
            else if (ew == 'W')
                this.lon = -lon;
            else
                throw new IllegalArgumentException("EW INVALIDE");
            fis = new FileInputStream(file);
            sbuf = fis.getChannel().map(MapMode.READ_ONLY, 0, FILE_LENGTH)
                    .asShortBuffer();
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }

        this.extent = new Interval2D(
                new Interval1D(this.lon * (POINTS_PER_SIDE - 1),
                        (this.lon + 1) * (POINTS_PER_SIDE - 1)),
                new Interval1D(this.lat * (POINTS_PER_SIDE - 1),
                        (this.lat + 1) * (POINTS_PER_SIDE - 1)));
    }

    @Override
    public void close() throws Exception {
        fis.close();

    }

    @Override
    public Interval2D extent() {
        return extent;
    }

    @Override
    public double elevationSample(int x, int y) {
        Preconditions.checkArgument(extent.contains(x, y),
                "Ne contient pas les points ");
        return sbuf.get(index(x, y));

    }

    private int index(int x, int y) {
        return (x - extent.iX().includedFrom())
                + (POINTS_PER_SIDE - 1 - (y - extent.iY().includedFrom()))
                        * POINTS_PER_SIDE;
    }

}
