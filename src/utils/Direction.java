package utils;
public enum Direction {

    UP_LEFT(-1, -1, "UP LEFT"),
    LEFT(-1, 0, "LEFT"),
    DOWN_LEFT(-1, 1, "DOWN LEFT"),
    UP(0, -1, "UP"),
    DOWN(0, 1, "DOWN"),
    UP_RIGHT(1, -1, "UP RIGHT"),
    RIGHT(1, 0, "RIGHT"),
    DOWN_RIGHT(1, 1, "DOWN RIGHT");

    private final Coordonates coordonates;
    private final String name;
    private static final Direction[][] directions;

    private Direction(int x, int y, String name) {
        this.coordonates = new Coordonates(x, y);
        this.name = name;
    }

    static {
        directions = new Direction[3][3];
        for (Direction direction : values()) {
            directions[direction.coordonates.getX() + 1][direction.coordonates.getY() + 1] = direction;
        }
    }

    public Direction getOppositeDirection() {
        return directions[1 - this.coordonates.getX()][1 - this.coordonates.getY()];
    }

    public static Direction getDirectionFromPairOfCoordonates(Coordonates coordonatesA, Coordonates coordonatesB) {
        int differenceX = coordonatesA.getX() - coordonatesB.getX();
        int differenceY = coordonatesA.getY() - coordonatesB.getY();

        return directions[differenceX == 0 ? 1 : differenceX > 0 ? 2 : 0][differenceY == 0 ? 1 : differenceY > 0 ? 2 : 0];
    }

    public Coordonates getCoordonates() {
        return this.coordonates;
    }

    public String toString() {
        return this.name;
    }
}
