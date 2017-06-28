package klondike;

import java.time.LocalDateTime;
import java.util.*;

public class Solver {

    public static void main(String[] args) {

        final List<Klondike.Move> pv = new ArrayList<>();
        playOneGame(pv);
        System.out.println(pv);
    }

    private static boolean playOneGame(List<Klondike.Move> pv) {

        Klondike klondike = new Klondike();
        return nextTurn(klondike, pv, new LinkedHashSet<>());
    }

    private static boolean nextTurn(Klondike klondike, List<Klondike.Move> pv, Set<Klondike> states) {

        if (pv.size() > 1) {

            Klondike.Move a = pv.get(pv.size() - 1);
            Klondike.Move b = pv.get(pv.size() - 2);
            if (a.type == b.type && a.cards == b.cards && !a.reveal && !b.reveal && a.from == b.to && a.to == b.from) {
                System.out.println("LOOP WARNING");
            }
        }

        if (pv.size() < 20) System.out.println("(" + LocalDateTime.now() + ") depth " + pv.size() + " " + pv);

        List<Klondike.Move> moves = klondike.getLegalMoves();
        if (moves.isEmpty()) return false;

        Klondike state = new Klondike(klondike);
        states.add(state);

        for (Klondike.Move move : moves) {

            pv.add(move);
            klondike.doMove(move);

            if (klondike.isWon() ||
                    (!states.contains(klondike) && nextTurn(klondike, pv, states)))
                return true;

            klondike.undoMove(move);
            pv.remove(move);
        }

        states.remove(state);
        return false;
    }
}