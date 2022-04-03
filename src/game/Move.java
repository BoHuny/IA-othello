package game;
import java.util.ArrayList;
import java.util.List;

import player.Player;
import utils.Coordonates;

public class Move implements Comparable<Move> {
    private final Coordonates coordonates;
    private final Player player;
    private final List<Coordonates> baseDisksCoordonates;

    public Move(Coordonates coordonates, Player player) {
        this.coordonates = coordonates;
        this.player = player;
        this.baseDisksCoordonates = new ArrayList<Coordonates>();
    }

    public Coordonates getCoordonates() {
        return this.coordonates;
    }

    public Player getPlayer() {
        return this.player;
    }

    public List<Coordonates> getBaseDisksCoordonates() {
        return this.baseDisksCoordonates;
    }

    public void addBaseDiskCoordonates(Coordonates baseDiskCoordonates) {
        this.baseDisksCoordonates.add(baseDiskCoordonates);
    }

    public String toString() {
        String str = "";
        str += (char)('a' + this.coordonates.getX());
        str += (1 + this.coordonates.getY());
        return str;
    }

    @Override
    public int compareTo(Move o) {
        return Board.getCaseScore(this.coordonates) - Board.getCaseScore(o.coordonates);
    }
}
