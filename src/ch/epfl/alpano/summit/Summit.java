package ch.epfl.alpano.summit;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import ch.epfl.alpano.GeoPoint;

public final class Summit {
    private final String name;
    private final GeoPoint position;
    private final int elevation;

    public Summit(String name, GeoPoint position, int elevation) {
        this.name = requireNonNull(name);
        this.position = requireNonNull(position);
        this.elevation = elevation;
    }

    public String name() {
        return name;
    }

    public GeoPoint position() {
        return position;
    }

    public int elevation() {
        return elevation;
    }

    @Override
    public String toString() {
        return name + " " + position + " " + elevation;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Summit
                && ((Summit) obj).elevation == this.elevation
                && ((Summit) obj).position.equals(this.position)
                && ((Summit) obj).name.equals(this.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name.hashCode(), position.latitude(),
                position.latitude(), elevation);
    }

}
