package player;
import java.util.*;

import game.Board;
import game.Disk;
import game.Move;
import utils.Coordonates;
import utils.Direction;
import utils.Logger;

public abstract class Player {
    private Disk diskType;
    private Board board;
    private Player opponent;
    private boolean[][] untakableDisks;
    private int numberOfUntakableDisks;

    public Player(Disk diskType) {
        this.diskType = diskType;
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    public Player getOpponent() {
        return this.opponent;
    }

    public Disk getDiskType() {
        return this.diskType;
    }

    public String toString() {
        return this.diskType.toString();
    }

    public boolean play() {
        Logger.log("----- " + this + "'s turn -----");
        List<Move> legalMoves = this.getLegalMoves(this.board);
        if (legalMoves.size() == 0) {
            Logger.log("No legal move");
            return false;
        }
        Collections.shuffle(legalMoves);
        Move move = this.chooseLegalMove(legalMoves);
        this.board.playMove(move);
        Logger.log(this.board.toString());
        return true;
    }

    protected Board getBoard() {
        return this.board;
    }

    public void checkToAddUntakable(Coordonates coordonates) {
        Coordonates coordonatesToCheck;
        if (!this.untakableDisks[coordonates.getY()][coordonates.getX()] && (
            (this.isUntakableCoordonates(new Coordonates(coordonates, Direction.UP)) || this.isUntakableCoordonates(new Coordonates(coordonates, Direction.DOWN))) && 
            (this.isUntakableCoordonates(new Coordonates(coordonates, Direction.UP_RIGHT)) || this.isUntakableCoordonates(new Coordonates(coordonates, Direction.DOWN_LEFT))) && 
            (this.isUntakableCoordonates(new Coordonates(coordonates, Direction.RIGHT)) || this.isUntakableCoordonates(new Coordonates(coordonates, Direction.LEFT))) && 
            (this.isUntakableCoordonates(new Coordonates(coordonates, Direction.DOWN_RIGHT)) || this.isUntakableCoordonates(new Coordonates(coordonates, Direction.UP_LEFT)))
        )) {
            this.setUntakableDisk(coordonates, true);
            for (Direction direction : Direction.values()) {
                coordonatesToCheck = new Coordonates(coordonates, direction);
                if (this.board.getDiskAtCoordonates(coordonatesToCheck) == this.diskType) {
                    this.checkToAddUntakable(coordonatesToCheck);
                }
            }
        }
    }

    public void checkToRemoveUntakable(Coordonates coordonates) {
        Coordonates coordonatesToCheck;
        if (this.untakableDisks[coordonates.getY()][coordonates.getX()]) {
            this.setUntakableDisk(coordonates, false);
            for (Direction direction : Direction.values()) {
                coordonatesToCheck = new Coordonates(coordonates, direction);
                this.checkUntakableFromDirection(coordonatesToCheck, direction);
            }
        }
    }

    public void switchColor() {
        this.diskType = this.diskType.getOpposite();
    }

    private void setBoardForNewGame(Board board) {
        this.board = board;
        this.untakableDisks = new boolean[board.getSize()][board.getSize()];
        this.numberOfUntakableDisks = 0;
    }

    private boolean checkUntakableFromDirection(Coordonates coordonates, Direction direction) {
        if (this.board.isOutOfBounds(coordonates)) {
            return true;
        }
        if (this.board.getDiskAtCoordonates(coordonates) != this.diskType) {
            return false;
        }
        boolean untakable = this.checkUntakableFromDirection(new Coordonates(coordonates, direction), direction);
        if (!untakable && this.untakableDisks[coordonates.getY()][coordonates.getX()]) {
            this.setUntakableDisk(coordonates, false);
        }
        return untakable;
    }

    private void setUntakableDisk(Coordonates coordonates, boolean untakable) {
        this.numberOfUntakableDisks += (untakable ? 1 : -1);
        this.untakableDisks[coordonates.getY()][coordonates.getX()] = untakable;
    }

    private boolean isUntakableCoordonates(Coordonates coordonates) {
        return this.board.isOutOfBounds(coordonates) || this.untakableDisks[coordonates.getY()][coordonates.getX()];
    }

    public void initBeforeGame(Board board) {
        this.setBoardForNewGame(board);
    }

    protected abstract Move chooseLegalMove(List<Move> legalMoves);

    public int getNumberOfUntakableDisks() {
        return this.numberOfUntakableDisks;
    }

    protected int getUntakableDisksDifference() {
        return this.numberOfUntakableDisks - this.opponent.numberOfUntakableDisks;
    }

    protected List<Move> getLegalMoves(Board board) {
        Map<Integer, Move> legalMovesInt = new HashMap<Integer, Move>();
        List<Coordonates> disksOfColor = board.getDisksOfColor(this.diskType);
        Disk currentDisk;
        boolean passedOppositeDisk;
        Coordonates currentCoordonates;
        for (Coordonates currentDiskCoordonates : disksOfColor) {
            for (Direction currentDirection : Direction.values()) {
                passedOppositeDisk = false;
                currentCoordonates = currentDiskCoordonates;
                do {
                    currentCoordonates = new Coordonates(currentCoordonates, currentDirection);
                    currentDisk = board.getDiskAtCoordonates(currentCoordonates);
                    if (currentDisk == this.opponent.diskType && !passedOppositeDisk) {
                        passedOppositeDisk = true;
                    }
                } while (currentDisk == this.opponent.diskType);

                if (passedOppositeDisk && currentDisk == null && !board.isOutOfBounds(currentCoordonates)) {
                    int integerCoordonates = currentCoordonates.getX() + board.getSize() * currentCoordonates.getY();
                    if (!legalMovesInt.containsKey(integerCoordonates)) {
                        legalMovesInt.put(integerCoordonates, new Move(currentCoordonates, this));
                    }
                    legalMovesInt.get(integerCoordonates).addBaseDiskCoordonates(currentDiskCoordonates);
                }
            }
        }

        List<Move> legalMoves = new ArrayList<Move>(legalMovesInt.values());
        return legalMoves;
    }
}
