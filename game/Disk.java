package game;
public enum Disk {
    BLACK("black", 'B'),
    WHITE("white", 'W');

    private final String name;
    private final char symbol;

    Disk(String name, char symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public Disk getOpposite() {
        return this == WHITE ? BLACK : WHITE;
    }

    public String toString() {
        return this.name;
    }

    public char getSymbol() {
        return this.symbol;
    }
}
