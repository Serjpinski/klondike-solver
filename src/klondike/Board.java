package klondike;

import java.util.*;

public class Board {

    public static final int SUITS = 4;
    public static final int CARDS_PER_SUIT = 13;
    public static final int CARDS = SUITS * CARDS_PER_SUIT;
    public static final int PILES = 7;

    public int[] foundations;
    public Pile[] piles;
    public List<Card> deck;

    public class Pile {

        public List<Card> hidden;
        public List<Card> visible;

        public boolean equals(Object o) {

            if (o == null || !(o instanceof Pile)) return false;
            Pile other = (Pile) o;
            return this.hidden.equals(other.hidden) && this.visible.equals(other.visible);
        }

        public String toString() {

            return "H=" + hidden.toString() + " V=" + visible.toString();
        }

        public Pile() {

            hidden = new ArrayList<>();
            visible = new ArrayList<>();
        }
    }

    public class Card {

        public int suit;
        public int number;

        public boolean equals(Object o) {

            if (o == null || !(o instanceof Card)) return false;
            Card other = (Card) o;
            return this.suit == other.suit && this.number == other.number;
        }

        public String toString() {

            String numberStr = "" + (number + 1);
            if (number == 0) numberStr = "A";
            else if (number == 10) numberStr = "J";
            else if (number == 11) numberStr = "Q";
            else if (number == 12) numberStr = "K";

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

    public boolean equals(Object o) {

        if (o == null || !(o instanceof Board)) return false;
        Board other = (Board) o;
        return Arrays.equals(this.foundations, other.foundations)
                && Arrays.equals(this.piles, other.piles)
                && this.deck.equals(other.deck);
    }

    public Board (Random random) {

        foundations = new int[SUITS];
        initDeck();
        initPilesFromDeck(random);
    }

    private void initDeck() {

        deck = new ArrayList<>(CARDS);
        for (int i = 0; i < SUITS; i++)
            for (int j = 0; j < CARDS_PER_SUIT; j++)
                deck.add(new Card(i, j));
    }

    private void initPilesFromDeck(Random random) {

        piles = new Pile[PILES];

        for (int i = 0; i < piles.length; i++) {

            piles[i] = new Pile();

            for (int j = 0; j < i; j++)
                piles[i].hidden.add(deck.remove(Math.abs(random.nextInt()) % deck.size()));

            piles[i].visible.add(deck.remove(Math.abs(random.nextInt()) % deck.size()));
        }
    }
}
