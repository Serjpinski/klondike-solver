package klondike;

import java.time.LocalDateTime;
import java.util.*;

public class Solver {

    public static void main(String[] args) throws InterruptedException {

        final Stack<Klondike.Move> pv = new Stack<>();
        playOneGame(pv);
        System.out.println(pv);
        Thread.sleep(1);
    }

    private static boolean playOneGame(Stack<Klondike.Move> pv) {

        Klondike klondike = new Klondike();
        return nextTurn(klondike, pv, new LinkedHashSet<>());
    }

    private static boolean nextTurn(Klondike klondike, Stack<Klondike.Move> pv, Set<Klondike> states) {

        if (pv.size() > 1) {

            Klondike.Move a = pv.get(pv.size() - 1);
            Klondike.Move b = pv.get(pv.size() - 2);
            if (a.isReverse(b)) {
                System.out.println("LOOP WARNING");
            }
        }

        if (pv.size() < 20) System.out.println(LocalDateTime.now() + " depth " + pv.size() + " " + pv);

        List<Klondike.Move> moves = klondike.getLegalMoves();
        if (moves.isEmpty()) return false;

        Klondike state = new Klondike(klondike);
        states.add(state);

        for (Klondike.Move move : moves) {

            pv.push(move);
            klondike.doMove(move);

            if (klondike.isWon() ||
                    (!states.contains(klondike) && nextTurn(klondike, pv, states)))
                return true;

            klondike.undoMove(move);
            pv.pop();

            if (!klondike.equals(state)) {
                System.out.println("BAD STATE WARNING");
            }
        }

        states.remove(state);
        return false;
    }
}