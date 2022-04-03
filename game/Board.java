package game;
import java.util.List;
import java.util.ArrayList;

import player.Player;
import utils.Coordonates;
import utils.Direction;

public class Board {
    private static final String EMPTY_SYMBOL = "-";
    private final Disk[][] board;
    private final int size;
    private final int numberOfCases;
    private final List<Move> movesPlayed;
    private int diskDifference;

    private static int[][] scoredBoard =  {
        {100, -20, 10, 5, 5, 10, -20, 100},
        {-20, -50, -2, -2, -2, -2, -50, -20},
        {10, -2, -1, -1, -1, -1, -2, -10},
        {5, -2, -1, -1, -1, -1, -2, -5},
        {5, -2, -1, -1, -1, -1, -2, -5},
        {10, -2, -1, -1, -1, -1, -2, -10},
        {-20, -50, -2, -2, -2, -2, -50, -20},
        {100, -20, 10, 5, 5, 10, -20, 100}
    };

    public Board(int size) {
        this.size = size;
        this.numberOfCases = this.size * this.size;
        this.movesPlayed = new ArrayList<Move>();
        this.board = new Disk[this.size][this.size];
        this.diskDifference = 0;
        this.initBoard();
    }

    public Board(Board boardToCopy) {
        this.size = boardToCopy.size;
        this.numberOfCases = boardToCopy.numberOfCases;
        this.movesPlayed = new ArrayList<Move>(boardToCopy.movesPlayed);
        this.board = new Disk[this.size][this.size];
        this.diskDifference = boardToCopy.diskDifference;
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                this.board[i][j] = boardToCopy.getDiskAtCoordonates(new Coordonates(j, i));
            }
        }
    }

    public Board(String boardRepresentation) {
        String[] rows = boardRepresentation.split("\n");
        this.size = rows.length;
        this.numberOfCases = this.size * this.size;
        this.board = new Disk[this.size][this.size];
        this.diskDifference = 0;
        this.movesPlayed = new ArrayList<Move>();
        for (int i = 0; i < rows.length; i++) {
            for (int j = 0; j < rows[i].length(); j++) {
                if (rows[i].charAt(j) == Disk.WHITE.getSymbol()) {
                    this.placeDisk(Disk.WHITE, new Coordonates(j, i));
                }
                else if (rows[i].charAt(j) == Disk.BLACK.getSymbol()) {
                    this.placeDisk(Disk.BLACK, new Coordonates(j, i));
                }
            }
        }
    }

    public static int getCaseScore(Coordonates coordonates) {
        return scoredBoard[coordonates.getY()][coordonates.getX()];
    }

    public int getSize() {
        return this.size;
    }

    public int getNumberOfCases() {
        return this.numberOfCases;
    }

    public int getDiskDifference() {
        return this.diskDifference;
    }

    private void initBoard() {
        int middle = this.size / 2;
        // Initial configuration
        this.placeDisk(Disk.WHITE, new Coordonates(middle - 1, middle - 1));
        this.placeDisk(Disk.WHITE, new Coordonates(middle, middle));
        this.placeDisk(Disk.BLACK, new Coordonates(middle - 1, middle));
        this.placeDisk(Disk.BLACK, new Coordonates(middle, middle - 1));
    }

    public List<Coordonates> getDisksOfColor(Disk disk) {
        List<Coordonates> disksOfColor = new ArrayList<Coordonates>();
        Coordonates currentCoordonates;
        Disk currentDisk;
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                currentCoordonates = new Coordonates(i, j);
                currentDisk = this.getDiskAtCoordonates(currentCoordonates);
                if (currentDisk == disk) {
                    disksOfColor.add(currentCoordonates);
                }
            }
        }
        return disksOfColor;
    }

    public Disk getWinner() {
        if (this.getDiskDifference() == 0) {
            return null;
        }
        return this.getDiskDifference() > 0 ? Disk.WHITE : Disk.BLACK;
    }

    public boolean isOutOfBounds(Coordonates coordonates) {
        return coordonates.getX() < 0 || coordonates.getX() >= this.size || coordonates.getY() < 0 || coordonates.getY() >= this.size;
    }

    public Disk getDiskAtCoordonates(Coordonates coordonates) {
        if (this.isOutOfBounds(coordonates)) {
            return null;
        }
        return this.board[coordonates.getY()][coordonates.getX()];
    }

    public void playMove(Move move) {
        Coordonates currentCoordonates;
        Direction currentDirection;
        Player player = move.getPlayer();
        Disk disk = player.getDiskType();
        this.placeDisk(disk, move.getCoordonates());
        player.checkToAddUntakable(move.getCoordonates());
        for (Coordonates baseDiskCoordonates : move.getBaseDisksCoordonates()) {
            currentDirection = Direction.getDirectionFromPairOfCoordonates(baseDiskCoordonates, move.getCoordonates());
            currentCoordonates = new Coordonates(move.getCoordonates(), currentDirection);
            while (!currentCoordonates.equals(baseDiskCoordonates)) {
                this.placeDisk(disk, currentCoordonates);
                player.checkToAddUntakable(currentCoordonates);
                currentCoordonates = new Coordonates(currentCoordonates, currentDirection);
            }
        }
        this.movesPlayed.add(move);
    }

    public void backTrackMove() {
        Move moveToBackTrack = this.movesPlayed.get(this.movesPlayed.size() - 1);
        Coordonates currentCoordonates;
        Direction currentDirection;
        Player player = moveToBackTrack.getPlayer();
        Disk oppositeDisk = player.getDiskType().getOpposite();
        player.checkToRemoveUntakable(moveToBackTrack.getCoordonates());
        this.removeDisk(moveToBackTrack.getCoordonates());
        for (Coordonates baseDiskCoordonates : moveToBackTrack.getBaseDisksCoordonates()) {
            currentDirection = Direction.getDirectionFromPairOfCoordonates(baseDiskCoordonates, moveToBackTrack.getCoordonates());
            currentCoordonates = new Coordonates(moveToBackTrack.getCoordonates(), currentDirection);
            while (!currentCoordonates.equals(baseDiskCoordonates)) {
                player.checkToRemoveUntakable(currentCoordonates);
                this.placeDisk(oppositeDisk, currentCoordonates);
                currentCoordonates = new Coordonates(currentCoordonates, currentDirection);
            }
        }
        this.movesPlayed.remove(this.movesPlayed.size() - 1);
    }

    private void placeDisk(Disk disk, Coordonates coordonates) {
        if (this.getDiskAtCoordonates(coordonates) != null) {
            this.diskDifference += this.getDiskAtCoordonates(coordonates) == Disk.WHITE ? -1 : 1;
        }
        if (disk != null) {
            this.diskDifference += disk == Disk.WHITE ? 1 : -1;
        }
        this.board[coordonates.getY()][coordonates.getX()] = disk;
    }

    private void removeDisk(Coordonates coordonates) {
        this.placeDisk(null, coordonates);
    }

    public String toString() {
        Disk currentDisk;
        String str = "  a b c d e f g h \n";
        for (int i = 0; i < this.size; i++) {
            str += (i + 1) + " ";
            for (int j = 0; j < this.size; j++) {
                currentDisk = this.getDiskAtCoordonates(new Coordonates(j, i));
                if (currentDisk == null) {
                    str += EMPTY_SYMBOL;
                }
                else {
                    str += currentDisk.getSymbol();
                }
                str += " ";
            }
            str += "\n";
        }
        return str;
    }

    public int getNumberOfMovesPlayed() {
        return this.movesPlayed.size();
    }
}
