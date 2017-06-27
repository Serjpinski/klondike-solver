package klondike;

import java.util.*;

public class Solver {

    public static void main(String[] args) {

        final List<Klondike.Move> pv = new ArrayList<>();
        System.out.println(playOneGame(pv) + " " + pv);
    }

    private static boolean playOneGame(List<Klondike.Move> pv) {

        Klondike klondike = new Klondike(new Random());
        return nextTurn(klondike, pv, new HashSet<>());
    }

    private static boolean nextTurn(Klondike klondike, List<Klondike.Move> pv, Set<Klondike> states) {

        if (klondike.isWon()) return true;
        if (states.contains(klondike)) return false;

        if (pv.size() < 20) System.out.println("(" + new Date() + ") Exploring " + pv);

        List<Klondike.Move> moves = klondike.getLegalMoves();
        if (moves.isEmpty()) return false;

        Klondike state = new Klondike(klondike);
        states.add(state);

        for (Klondike.Move move : moves) {

            klondike.doMove(move);
            pv.add(move);
            if (nextTurn(klondike, pv, states)) return true;
            pv.remove(move);
            klondike.undoMove(move);
        }

        states.remove(state);
        return false;
    }
}