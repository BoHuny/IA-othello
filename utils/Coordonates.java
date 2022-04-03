package utils;
public class Coordonates {
    private final int x;
    private final int y;

    public Coordonates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coordonates(Coordonates coordonates, Direction direction) {
        this.x = coordonates.getX() + direction.getCoordonates().getX();
        this.y = coordonates.getY() + direction.getCoordonates().getY();
    }

    public boolean equals(Coordonates otherCoordonates) {
        return this.x == otherCoordonates.x && this.y == otherCoordonates.y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }
}
