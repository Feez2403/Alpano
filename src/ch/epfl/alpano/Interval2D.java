package ch.epfl.alpano;

import java.util.Objects;

public final class Interval2D {
    private final Interval1D iX, iY;

    public Interval2D(Interval1D iX, Interval1D iY) {
        if (iX == null || iY == null)
            throw new NullPointerException();
        this.iX = iX;
        this.iY = iY;
    }

    public Interval1D iX() {
        return iX;
    }

    public Interval1D iY() {
        return iY;
    }

    public boolean contains(int x, int y) {
        return iX.contains(x) && iY.contains(y);
    }

    private boolean contains(Interval2D that) {
        return this.contains(that.iX.includedFrom(), that.iY.includedFrom())
                && this.contains(that.iX.includedTo(), that.iY.includedTo());
    }

    public int size() {
        return iX.size() * iY.size();
    }

    public int sizeOfIntersectionWith(Interval2D that) {
        return this.iX.sizeOfIntersectionWith(that.iX)
                * this.iY.sizeOfIntersectionWith(that.iY);
    }

    public Interval2D boundingUnion(Interval2D that) {
        return new Interval2D(this.iX.boundingUnion(that.iX),
                this.iY.boundingUnion(that.iY));
    }

    public boolean isUnionableWith(Interval2D that) {
        return this.contains(that) || that.contains(this)
                || (this.iX.equals(that.iX) && this.iY.isUnionableWith(that.iY))
                || (this.iY.equals(that.iY)
                        && this.iX.isUnionableWith(that.iX));
    }

    public Interval2D union(Interval2D that) {
        Preconditions.checkArgument(this.isUnionableWith(that));
        return new Interval2D(iX.union(that.iX), iY.union(that.iY));
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Interval2D && iX.equals(((Interval2D) obj).iX)
                && iY.equals(((Interval2D) obj).iY);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.iX.hashCode(), this.iY.hashCode());
    }

    @Override
    public String toString() {
        return this.iX.toString() + "x" + this.iY.toString();
    }
}
