package game;

import player.Player;
import utils.Logger;

public class Game {
    private static final int SIZE = 8;

    private Board board;
    private final Player player1;
    private final Player player2;

    public Game(Player player1, Player player2) {
        if (player1.getDiskType() == Disk.BLACK) {
            this.player1 = player1;
            this.player2 = player2;
        }
        else {
            this.player1 = player2;
            this.player2 = player1;   
        }
        this.player1.setOpponent(this.player2);
        this.player2.setOpponent(this.player1);
    }

    private void setNewBoard() {
        this.board = new Board(SIZE);
        this.player1.initBeforeGame(this.board);
        this.player2.initBeforeGame(this.board);
    }

    public Disk start() {
        this.setNewBoard();
        Logger.log(this.board.toString());
        boolean previousDidPlay = true;
        while (true) {
            if (player1.play()) {
                previousDidPlay = true;
            }
            else {
                if (!previousDidPlay) {
                    break;
                }
                previousDidPlay = false;
            }
            if (player2.play()) {
                previousDidPlay = true;
            }
            else {
                if (!previousDidPlay) {
                    break;
                }
                previousDidPlay = false;
            }
        }
        System.out.println("Disk difference: " + this.board.getDiskDifference());
        return this.board.getWinner();
    }
}
