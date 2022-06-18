package com.github.markozajc.juno.decks.impl;

import static com.github.markozajc.juno.cards.UnoCardColor.WILD;
import static java.util.Arrays.asList;

import java.util.*;

import javax.annotation.*;

import com.github.markozajc.juno.cards.*;
import com.github.markozajc.juno.cards.impl.*;
import com.github.markozajc.juno.cards.impl.UnoActionCard.UnoFlowAction;
import com.github.markozajc.juno.decks.UnoDeck;

/**
 * A singleton class containing the official {@link UnoDeck} of UNO cards.
 *
 * @author Marko Zajc
 *
 * @see <a href="https://en.wikipedia.org/wiki/File:UNO_cards_deck.svg">Reference
 *      vector graphic of a UNO deck</a>
 */
public class UnoStandardDeck {

	private UnoStandardDeck() {}

	private static final int EXPECTED_SIZE = 108;
	private static UnoDeck deck;

	@SuppressWarnings("null")
	private static void generateDeck() {
		List<UnoCard> cards = new ArrayList<>();

		for (UnoFlowAction action : UnoFlowAction.values()) {
			for (UnoCardColor color : UnoCardColor.values()) {
				if (color == WILD)
					continue;

				cards.addAll(asList(new UnoActionCard(color, action), new UnoActionCard(color, action)));
				// Adds two of each action cards
			}
		}
		// Adds the action cards

		for (UnoCardColor color : UnoCardColor.values()) {
			if (color == WILD)
				continue;

			for (int i = 0; i <= 9; i++) {
				if (i != 0) {
					cards.addAll(asList(new UnoNumericCard(color, i), new UnoNumericCard(color, i)));

				} else {
					cards.add(new UnoNumericCard(color, i));
					// Yes, there's just one of each 0 cards in the official rules
				}
			}
			// Adds all standard number cards

			cards.addAll(asList(new UnoDrawCard(color), new UnoDrawCard(color)));
			// Adds two of each color's draw two cards
		}
		// Adds colored (non-wild) cards

		cards.addAll(asList(
							// 4 wild draw four cards
							new UnoDrawCard(), new UnoDrawCard(), new UnoDrawCard(), new UnoDrawCard(),

							// 4 wild cards
							new UnoWildCard(), new UnoWildCard(), new UnoWildCard(), new UnoWildCard()));
		// Adds generic cards

		deck = new UnoDeck(cards);
	}

	/**
	 * @return the expected size of this {@link UnoDeck} (108 cards)
	 */
	@Nonnegative
	public static int getExpectedSize() {
		return EXPECTED_SIZE; // NOSONAR
	}

	/**
	 * @return the singleton {@link UnoDeck}
	 */
	@Nonnull
	@SuppressWarnings("null")
	public static UnoDeck getDeck() {
		if (deck == null)
			generateDeck();

		return deck;
	}

}
