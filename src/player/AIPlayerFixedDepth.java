package player;
import java.util.Collections;
import java.util.List;

import game.Board;
import game.Disk;
import game.Move;
import utils.Logger;

public class AIPlayerFixedDepth extends AIPlayer {
    private final static int MIN_DEPTH = 3;

    private final int power;

    public AIPlayerFixedDepth(Disk diskType, int power) {
        super(diskType);
        this.power = power;
    }

    @Override
    protected Move chooseLegalMove(List<Move> legalMoves) {
        long computationTimeStart = System.currentTimeMillis();

        Collections.shuffle(legalMoves);
        Move chosenMove = this.getBestMoveByDepth(legalMoves, power);
        Logger.log("The computer plays " + chosenMove);
        long computationTime = System.currentTimeMillis() - computationTimeStart;
        this.totalTime += computationTime;
        Logger.log("The computer played in " + computationTime + "ms");
        return chosenMove;
    }
}
