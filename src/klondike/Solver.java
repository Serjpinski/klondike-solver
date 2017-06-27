package klondike;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;

public class Solver {

    public static void main(String[] args) {

        final LinkedHashSet<Klondike.Move> pv = new LinkedHashSet<>();
        System.out.println(playOneGame(pv) + " " + pv);
    }

    private static boolean playOneGame(LinkedHashSet<Klondike.Move> pv) {

        Klondike klondike = new Klondike(new Random());
        return nextTurn(klondike, pv);
    }

    private static boolean nextTurn(Klondike klondike, LinkedHashSet<Klondike.Move> pv) {

        if (pv.size() < 20) System.out.println("(" + new Date() + ") Exploring " + pv);

        if (klondike.isWon()) return true;

        List<Klondike.Move> moves = klondike.getLegalMoves();

        if (moves.isEmpty()) return false;
        else {

            for (Klondike.Move move : moves) {

                if (!pv.contains(move)) {

                    klondike.doMove(move);
                    pv.add(move);
                    if (nextTurn(klondike, pv)) return true;
                    pv.remove(move);
                    klondike.undoMove(move);
                }
            }

            return false;
        }
    }
}