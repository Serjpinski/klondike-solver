package klondike;

import java.time.LocalDateTime;
import java.util.*;

public class Solver {

    private static boolean REVERSIBLE_PRUNING = true;

    public static void main(String[] args) throws InterruptedException {

        try {

            Klondike game1 = new Klondike();
            Klondike game2 = new Klondike(game1);

            REVERSIBLE_PRUNING = true;
            Stack<Klondike.Move> pv1 = solveGame(game1);
            System.out.println(pv1);
            System.out.println();
            Thread.sleep(3000);

            REVERSIBLE_PRUNING = false;
            Stack<Klondike.Move> pv2 = solveGame(game2);
            System.out.println(pv2);
            System.out.println();
            Thread.sleep(1);
        }
        catch (Exception e) {

            e.printStackTrace();
        }
    }

    private static Stack<Klondike.Move> solveGame(Klondike klondike) {

        Stack<Klondike.Move> pv = new Stack<>();
        nextTurn(klondike, pv, new LinkedHashSet<>());
        return pv;
    }

    private static boolean nextTurn(Klondike klondike, Stack<Klondike.Move> pv, Set<Klondike> states) {

//        if (pv.size() > 1) {
//
//            Klondike.Move a = pv.get(pv.size() - 1);
//            Klondike.Move b = pv.get(pv.size() - 2);
//            if (a.isReverse(b)) {
//                System.out.println("LOOP WARNING");
//            }
//        }

        if (pv.size() < 20) System.out.println(LocalDateTime.now() + " depth " + pv.size() + " " + pv);

        List<Klondike.Move> moves = klondike.getLegalMoves();
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

            if (REVERSIBLE_PRUNING && loop && move.isReversible()) {

                states.remove(state);
                return false;
            }

//            if (!klondike.equals(state)) {
//                System.out.println("BAD STATE WARNING");
//            }
        }

        states.remove(state);
        return false;
    }
}