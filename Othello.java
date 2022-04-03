import game.Disk;
import game.Game;
import player.*;
import utils.GlobalScanner;
import utils.Logger;

public class Othello {
    private static final char BLACK_TYPE = '1';
    private static final char WHITE_TYPE = '2';

    private static final char FIXED_DEPTH_TYPE = '1';
    private static final char TIME_CONTROL_TYPE = '2';

    private static final int MAX_DIFFICULTY = 9;

    public static void main(String[] args) {
        playVSAI();
    }


    private static void playVSAI() {
        Logger.setVerbose(true);
        Disk playerColor = getColorOfPlayer();
        Player humanPlayer = new HumanPlayer(playerColor);
        AIPlayer AI = getAIType() == FIXED_DEPTH_TYPE ? getFixedDepthAI(playerColor) : getTimeControlAI(playerColor);
        Game game = new Game(humanPlayer, AI);
        game.start();
        GlobalScanner.sc.close();
    }

    private static AIPlayer getFixedDepthAI(Disk playerColor) {
        int computerDifficulty = getComputerDifficulty();
        return new AIPlayerFixedDepth(playerColor.getOpposite(), computerDifficulty);
    }

    private static AIPlayer getTimeControlAI(Disk playerColor) {
        long timeLimit = getTimeLimit();
        return new AIPlayerTimeControl(playerColor.getOpposite(), timeLimit);
    }

    private static void emulateAIGames(int power, int nGames) {
        Logger.setVerbose(false);
        Disk player1Color = Disk.BLACK;
        int player1Win = 0;
        int nDraws = 0;
        AIPlayer player1 = new AIPlayerFixedDepth(player1Color, 5);
        AIPlayer player2 = new AIPlayerTimeControl(player1Color.getOpposite(), 2441);
        for (int i = 0; i < nGames; i++) {
            Game game = new Game(player1, player2);
            System.out.println(i + " / " + nGames);
            Disk winner = game.start();
            if (winner == null) {
                nDraws++;
            }
            if (winner == player1.getDiskType()) {
                player1Win++;
            }
            System.out.println("Player " + (winner == player1.getDiskType() ? 1 : 2) + " wins");
            System.out.println("Player 1 computation time : " + player1.getTotalComputationTime());
            System.out.println("Player 2 computation time : " + player2.getTotalComputationTime());
            player1.switchColor();
            player2.switchColor();
        }
        System.out.println("Player 1 won " + player1Win + " games, drawed " + nDraws + " games, and lost " + (nGames - player1Win - nDraws) + " games out of " + nGames);
    }

    private static Disk getColorOfPlayer() {
        String answer;
        while (true) {
            System.out.println("Select your color:\n" + BLACK_TYPE + ": Black\n" + WHITE_TYPE + ": White\n");
            answer = GlobalScanner.sc.next();
            if (answer.equals(String.valueOf(BLACK_TYPE))) {
                return Disk.BLACK;
            }
            if (answer.equals(String.valueOf(WHITE_TYPE))) {
                return Disk.WHITE;
            }
            System.out.println("Invalid answer");
        }
    }

    private static char getAIType() {
        String answer;
        char charAnswer;
        while (true) {
            System.out.println("Select AI Type:\n" + FIXED_DEPTH_TYPE + ": Fixed depth\n" + TIME_CONTROL_TYPE + ": Time control");
            answer = GlobalScanner.sc.next();
            if (answer.length() == 1) {
                charAnswer = answer.charAt(0);
                if (charAnswer == FIXED_DEPTH_TYPE || charAnswer == TIME_CONTROL_TYPE) {
                    return charAnswer;
                }
            }
            System.out.println("Invalid answer");
        }
    }

    private static int getComputerDifficulty() {
        String answer;
        char charAnswer;
        while (true) {
            System.out.println("Select computer difficulty (1 to " + MAX_DIFFICULTY + "):");
            answer = GlobalScanner.sc.next();
            if (answer.length() == 1) {
                charAnswer = answer.charAt(0);
                if (charAnswer >= '1' && charAnswer <= ('0' + MAX_DIFFICULTY)) {
                    return charAnswer - '1';
                }
            }
            System.out.println("Invalid answer");
        }
    }

    private static long getTimeLimit() {
        System.out.println("Select time limit for the AI (in ms)");
        return GlobalScanner.sc.nextLong();
    }
}