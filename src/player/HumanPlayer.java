package player;
import java.util.List;

import game.Disk;
import game.Move;
import utils.GlobalScanner;

public class HumanPlayer extends Player {

    public HumanPlayer(Disk diskType) {
        super(diskType);
    }

    @Override
    protected Move chooseLegalMove(List<Move> legalMoves) {
        while (true) {
            System.out.print("Enter your move: ");
            String strMove = GlobalScanner.sc.next();
            for (Move move : legalMoves) {
                if (strMove.equals(move.toString())) {
                    return move;
                }
            }
            System.out.println("This is an illegal move. Please enter a valid move (ex: " + legalMoves.get(0) + ")");
        }
    }
    
}
