package ch.epfl.alpano.summit;

import static java.lang.Integer.parseInt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ch.epfl.alpano.Azimuth;
import ch.epfl.alpano.GeoPoint;

public class GazetteerParser {
    private GazetteerParser() {
    }

    public static List<Summit> readSummitsFrom(File file) throws IOException {
        List<Summit> summits = new ArrayList<>();
        BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file)));
        String s = br.readLine();
        while (s != null) {

            summits.add(summitFromLine(s));
            s = br.readLine();
        }
        br.close();
        return summits;
    }

    private static double angleFromString(String angle) {
        String[] hms = angle.split(":");
        return Azimuth.toMath(parseInt(hms[0]) + parseInt(hms[1]) / 60
                + parseInt(hms[2]) / 3600);

    }

    private static Summit summitFromLine(String s) {
        String[] ss = s.split(" ");
        GeoPoint p = new GeoPoint(angleFromString(ss[0]),
                angleFromString(ss[1]));
        int elevation = Integer.parseInt(ss[2]);
        StringBuilder sb = new StringBuilder();
        for (int i = 6; i < ss.length; i++) {
            sb.append(ss[i]);
        }
        return new Summit(sb.toString(), p, elevation);
    }
}
