package player;

import game.Board;
import game.Disk;
import game.Move;

import java.util.Collections;
import java.util.List;

public abstract class AIPlayer extends Player {
    protected long totalTime;
    protected boolean touchedLimit;

    public AIPlayer(Disk diskType) {
        super(diskType);
        this.totalTime = 0;
    }

    public long getTotalComputationTime() {
        return this.totalTime;
    }

    @Override
    protected Move chooseLegalMove(List<Move> legalMoves) {
        return null;
    }

    protected Move getBestMoveByDepth(List<Move> moves, int depth) {
        Board copiedBoard = new Board(this.getBoard());
        Move bestMove = null;
        int currentMoveValue;
        int bestMoveValue = Integer.MIN_VALUE;
        for (Move move : moves) {
            copiedBoard.playMove(move);
            currentMoveValue = this.chooseLegalMoveRec(copiedBoard, depth, true, false, bestMoveValue, Integer.MAX_VALUE);
            copiedBoard.backTrackMove();
            if (currentMoveValue > bestMoveValue) {
                bestMoveValue = currentMoveValue;
                bestMove = move;
            }
        }
        return bestMove;
    }

    private int chooseLegalMoveRec(Board board, int depth, boolean isOpponent, boolean previousPassed, int alpha, int beta) {
        List<Move> legalMoves;
        int bestMoveValue;
        if (depth == 0) {
            this.touchedLimit = false;
            return this.evaluatePosition(board);
        }
        else {
            legalMoves = isOpponent ? this.getOpponent().getLegalMoves(board) : this.getLegalMoves(board);
            legalMoves.sort(Collections.reverseOrder()); // Killer heuristic
            if (legalMoves.size() == 0) {
                if (previousPassed) {
                    Disk winner = board.getWinner();
                    if (winner == null) {
                        return 0;
                    }
                    return winner == this.getDiskType() ? Integer.MAX_VALUE : Integer.MIN_VALUE + 1;
                }
                return this.chooseLegalMoveRec(board, depth, !isOpponent, true, alpha, beta);
            }
            if (isOpponent) {
                bestMoveValue = Integer.MAX_VALUE;
                for (Move legalMove : legalMoves) {
                    board.playMove(legalMove);
                    bestMoveValue = Math.min(bestMoveValue, this.chooseLegalMoveRec(board, depth - 1, false, false, alpha, beta));
                    board.backTrackMove();
                    if (alpha >= bestMoveValue) {
                        return bestMoveValue;
                    }
                    beta = Math.min(beta, bestMoveValue);
                }
            }
            else {
                bestMoveValue = Integer.MIN_VALUE;
                for (Move legalMove : legalMoves) {
                    board.playMove(legalMove);
                    bestMoveValue = Math.max(bestMoveValue, this.chooseLegalMoveRec(board, depth - 1, true, false, alpha, beta));
                    board.backTrackMove();
                    if (beta <= bestMoveValue) {
                        return bestMoveValue;
                    }
                    alpha = Math.max(alpha, bestMoveValue);
                }
            }
        }

        return bestMoveValue;
    }

    private int evaluatePosition(Board board) {
        int numberOfLegalMoves = this.getLegalMoves(board).size();
        int diskDifference = board.getDiskDifference() * (this.getDiskType() == Disk.WHITE ? 1 : -1);
        int numberOfUntakableDisksDifference = this.getUntakableDisksDifference();
        return (board.getNumberOfMovesPlayed() > (board.getNumberOfCases() * 25 / 32) ? 1 : 0) * diskDifference + numberOfLegalMoves * 50 + numberOfUntakableDisksDifference * 1000;
    }
}
