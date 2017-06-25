package klondike;

import java.util.*;

public class Klondike {

    public static final int SUITS = 4;
    public static final int CARDS_PER_SUIT = 13;
    public static final int CARDS = SUITS * CARDS_PER_SUIT;
    public static final int PILES = 7;

    public int[] foundations;
    public Pile[] piles;
    public List<Card> deck;

    public static class Pile {

        public List<Card> cards;
        public int visible;

        public boolean equals(Object o) {

            if (o == null || !(o instanceof Pile)) return false;
            Pile other = (Pile) o;
            return this.cards.equals(other.cards) && this.visible == other.visible;
        }

        public String toString() {

            return "v=" + visible + " " + cards.toString();
        }

        public Pile() {

            cards = new ArrayList<>();
            visible = 0;
        }
    }

    public static class Card {

        public int suit;
        public int number;

        public boolean equals(Object o) {

            if (o == null || !(o instanceof Card)) return false;
            Card other = (Card) o;
            return this.suit == other.suit && this.number == other.number;
        }

        public String toString() {

            String numberStr = "" + number;
            if (number == 1) numberStr = "A";
            else if (number == 11) numberStr = "J";
            else if (number == 12) numberStr = "Q";
            else if (number == 13) numberStr = "K";

            String suitStr = "";
            if (suit == 0) suitStr = "♣";
            else if (suit == 1) suitStr = "♦";
            else if (suit == 2) suitStr = "♠";
            else if (suit == 3) suitStr = "♥";

            return numberStr + suitStr;
        }

        public Card(int suit, int number) {

            this.suit = suit;
            this.number = number;
        }
    }

    public static class Move {

        public enum Type {

            DECK_TO_FOUND,
            PILE_TO_FOUND,
            DECK_TO_PILE,
            PILE_TO_PILE,
            FOUND_TO_PILE
        }

        public Type type;
        public int from;
        public int to;
        public int cards;
        public boolean reveal;

        public Move(Type type, int from, int to, int cards, boolean reveal) {

            this.type = type;
            this.from = from;
            this.to = to;
            this.cards = cards;
            this.reveal = reveal;
        }
    }

    public boolean equals(Object o) {

        if (o == null || !(o instanceof Klondike)) return false;
        Klondike other = (Klondike) o;
        return Arrays.equals(this.foundations, other.foundations)
                && Arrays.equals(this.piles, other.piles)
                && this.deck.equals(other.deck);
    }

    public Klondike(Random random) {

        foundations = new int[SUITS];
        initDeck();
        initPilesFromDeck(random);
    }

    private void initDeck() {

        deck = new ArrayList<>(CARDS);
        for (int i = 0; i < SUITS; i++)
            for (int j = 0; j < CARDS_PER_SUIT; j++)
                deck.add(new Card(i, j + 1));
    }

    private void initPilesFromDeck(Random random) {

        piles = new Pile[PILES];

        for (int i = 0; i < piles.length; i++) {

            piles[i] = new Pile();

            for (int j = 0; j <= i; j++)
                piles[i].cards.add(deck.remove(Math.abs(random.nextInt()) % deck.size()));

            piles[i].visible = 1;
        }
    }

    // Assumes move is legal
    public void doMove(Move move) {

        Move.Type type = move.type;

        if (Move.Type.DECK_TO_FOUND.equals(type)) {

            deck.remove(move.from);
            foundations[move.to]++;
        }
        else if (Move.Type.PILE_TO_FOUND.equals(type)) {

            Pile from = piles[move.from];
            from.cards.remove(0);
            if (move.reveal) from.visible = 1;
            else from.visible--;
            foundations[move.to]++;
        }
        else if (Move.Type.DECK_TO_PILE.equals(type)) {

            piles[move.to].cards.add(0, deck.remove(move.from));
            piles[move.to].visible++;
        }
        else if (Move.Type.PILE_TO_PILE.equals(type)) {

            Pile from = piles[move.from];
            Pile to = piles[move.to];
            int cards = move.cards;
            for (int i = 0; i < cards; i++) to.cards.add(0, from.cards.remove(cards - i - 1));
            if (move.reveal) from.visible = 1;
            else from.visible -= cards;
            to.visible += cards;
        }
        else if (Move.Type.FOUND_TO_PILE.equals(type)) {

            piles[move.to].cards.add(0, new Card(move.to, foundations[move.from]--));
            piles[move.to].visible++;
        }
    }

    // Assumes move is legal and it was the last move
    public void undoMove(Move move) {

        Move.Type type = move.type;

        if (Move.Type.DECK_TO_FOUND.equals(type)) {

            deck.add(move.from, new Card(move.to, foundations[move.to]--));
        }
        else if (Move.Type.PILE_TO_FOUND.equals(type)) {

            piles[move.from].cards.add(0, new Card(move.to, foundations[move.to]--));
            if (!move.reveal) piles[move.from].visible++;
        }
        else if (Move.Type.DECK_TO_PILE.equals(type)) {

            deck.add(move.from, piles[move.to].cards.get(0));
            piles[move.to].visible--;
        }
        else if (Move.Type.PILE_TO_PILE.equals(type)) {

            Pile from = piles[move.from];
            Pile to = piles[move.to];
            int cards = move.cards;
            for (int i = 0; i < cards; i++) from.cards.add(0, to.cards.remove(cards - i - 1));
            to.visible -= cards;
            if (from.visible == 0 && !from.cards.isEmpty()) from.visible = 1;
            from.visible += cards;
            if (move.reveal) from.visible--;
        }
        else if (Move.Type.FOUND_TO_PILE.equals(type)) {

            piles[move.to].cards.remove(0);
            piles[move.to].visible--;
            foundations[move.from]++;
        }
    }
}