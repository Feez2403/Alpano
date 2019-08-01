package ch.epfl.alpano;

import static ch.epfl.alpano.Math2.max;
import static ch.epfl.alpano.Math2.min;

import java.util.Objects;

public final class Interval1D {

    private final int includedFrom;
    private final int includedTo;

    public Interval1D(int includedFrom, int includedTo) {
        Preconditions.checkArgument(includedTo >= includedFrom);
        this.includedFrom = includedFrom;
        this.includedTo = includedTo;
    }

    public int includedFrom() {
        return includedFrom;
    }

    public int includedTo() {
        return includedTo;
    }

    public boolean contains(int v) {
        return v >= includedFrom && v <= includedTo;
    }

    public int size() {
        return includedTo - includedFrom + 1;
    }

    public int sizeOfIntersectionWith(Interval1D that) {
        if (this.includedFrom < that.includedFrom) {
            if (this.includedTo < that.includedFrom) {
                return 0;
            } else if (this.includedTo < that.includedTo) {
                return this.includedTo - that.includedFrom + 1;
            } else {
                return that.size();
            }
        } else {
            if (that.includedTo < this.includedFrom) {
                return 0;
            } else if (that.includedTo < this.includedTo) {
                return that.includedTo - this.includedFrom + 1;
            } else {
                return this.size();
            }
        }
    }

    public Interval1D boundingUnion(Interval1D that) {
        return new Interval1D(min(this.includedFrom, that.includedFrom),
                max(this.includedTo, that.includedTo));
    }

    public boolean isUnionableWith(Interval1D that) {
        return this.includedFrom < that.includedFrom
                ? (this.includedTo >= that.includedFrom - 1)
                : (that.includedTo >= this.includedFrom - 1);
    }

    public Interval1D union(Interval1D that) {
        Preconditions.checkArgument(this.isUnionableWith(that));
        return boundingUnion(that);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Interval1D
                && ((Interval1D) obj).includedFrom == this.includedFrom
                && ((Interval1D) obj).includedTo == this.includedTo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(includedFrom(), includedTo());
    }

    @Override
    public String toString() {
        return "[" + includedFrom + ".." + includedTo + "]";
    }

}
