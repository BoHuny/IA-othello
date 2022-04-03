package player;

import game.Board;
import game.Disk;
import game.Move;
import utils.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class AIPlayerTimeControl extends AIPlayer {
    private final long timeByGame;
    private long timeBank;
    private final static int MIN_DEPTH = 0;

    public AIPlayerTimeControl(Disk diskType, long timeByGame) {
        super(diskType);
        this.timeByGame = timeByGame;
    }

    @Override
    public void initBeforeGame(Board board) {
        super.initBeforeGame(board);
        this.timeBank = this.timeByGame;
    }

    @Override
    protected Move chooseLegalMove(List<Move> legalMoves) {
        long computationTimeStart = System.currentTimeMillis();
        long timeSpent = 0;
        long timeLimitForMove = this.timeBank /
                Math.max(1, (long)((((long) this.getBoard().getSize() * this.getBoard().getSize() - this.getBoard().getNumberOfMovesPlayed() - 8) * 0.8)));
        int depth = MIN_DEPTH;
        Move chosenMove = legalMoves.get(0);
        this.touchedLimit = false;
        while (timeSpent < timeLimitForMove && !this.touchedLimit) {
            this.touchedLimit = true;
            chosenMove = this.getBestMoveByDepth(legalMoves, ++depth);
            timeSpent = System.currentTimeMillis() - computationTimeStart;
        }
        Logger.log("The computer plays " + chosenMove);
        this.timeBank -= timeSpent;
        this.totalTime += timeSpent;
        Logger.log("The computer played in " + timeSpent / 1000f + "s (" + this.timeBank / 1000f + "s left, played at depth " + depth + ")");
        return chosenMove;
    }
}
