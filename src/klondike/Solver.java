package klondike;

import java.time.LocalDateTime;
import java.util.*;

// TODO things to try: Breadth-First Search, iterative deepening search
public class Solver {

    public static void main(String[] args) {

        Klondike klondike = new Klondike();
        Stack<Klondike.Move> pv1 = solveGame(klondike);
        System.out.println("PV: " + pv1);
    }

    private static Stack<Klondike.Move> solveGame(Klondike klondike) {

        Stack<Klondike.Move> pv = new Stack<>();
        nextTurn(klondike, pv, new LinkedHashSet<>());
        return pv;
    }

    private static boolean nextTurn(Klondike klondike, Stack<Klondike.Move> pv, Set<Klondike> states) {

        System.out.println(LocalDateTime.now() + " depth " + pv.size() + " " + pv);

        List<Klondike.Move> moves = klondike.getLegalMoves();
        moves.sort(Comparator.comparingInt(Klondike.Move::movePriority).reversed());
        if (moves.isEmpty()) return false;

        Klondike state = new Klondike(klondike);
        states.add(state);

        for (Klondike.Move move : moves) {

            pv.push(move);
            klondike.doMove(move);

            if (klondike.isWon()) return true;

            boolean loop = states.contains(klondike);

            if (!loop && nextTurn(klondike, pv, states)) return true;

            klondike.undoMove(move);
            pv.pop();
        }

        states.remove(state);
        return false;
    }
}