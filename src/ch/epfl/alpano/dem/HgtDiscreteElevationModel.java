package ch.epfl.alpano.dem;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel.MapMode;

import ch.epfl.alpano.Interval1D;
import ch.epfl.alpano.Interval2D;

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
            int lat = Integer.parseInt(name.substring(1, 2));
            int lon = Integer.parseInt(name.substring(4, 6));
            if (name.length() != 11 || !name.substring(7, 10).equals(".hgt")
                    || file.length() != FILE_LENGTH)
                throw new IllegalArgumentException();
            if (ns == 'N')
                this.lat = lat;
            else if (ns == 'S')
                this.lat = -lat;
            else
                throw new IllegalArgumentException();
            if (ew == 'E')
                this.lon = lon;
            else if (ew == 'W')
                this.lon = -lon;
            else
                throw new IllegalArgumentException();
            fis = new FileInputStream(file);
            sbuf = fis.getChannel().map(MapMode.READ_ONLY, 0, FILE_LENGTH)
                    .asShortBuffer();
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }

        this.extent = new Interval2D(
                new Interval1D(this.lon * (POINTS_PER_SIDE - 1),
                        (this.lon + 1) * (POINTS_PER_SIDE - 1) + 1),
                new Interval1D(this.lat * (POINTS_PER_SIDE - 1),
                        (this.lat + 1) * (POINTS_PER_SIDE - 1) + 1));
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
        if (!extent.contains(x, y))
            throw new IllegalArgumentException();
        return sbuf.get((x - extent.iX().includedFrom())
                + (POINTS_PER_SIDE - (y - extent.iY().includedFrom()))
                        * POINTS_PER_SIDE);

    }

}
