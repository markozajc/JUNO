package com.github.markozajc.juno.cards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.github.markozajc.juno.cards.impl.UnoActionCard;
import com.github.markozajc.juno.cards.impl.UnoDrawCard;
import com.github.markozajc.juno.cards.impl.UnoNumericCard;
import com.github.markozajc.juno.cards.impl.UnoWildCard;
import com.github.markozajc.juno.cards.impl.UnoActionCard.UnoAction;
import com.github.markozajc.juno.decks.UnoDeck;

/**
 * An immutable class containing the standard deck of UNO cards.
 *
 * @author Marko Zajc
 * @see <a href="https://en.wikipedia.org/wiki/File:UNO_cards_deck.svg">Reference vector graphic of a UNO deck</a>
 */
public class UnoStandardDeck implements UnoDeck {

	private static final int EXPECTED_SIZE = 108;
	private static final List<UnoCard> DECK;
	static {
		List<UnoCard> tempDeck = new ArrayList<>();

		for (UnoAction action : UnoAction.values()) {
			for (UnoCardColor color : UnoCardColor.values()) {
				if (color.equals(UnoCardColor.WILD))
					continue;

				UnoCard card = new UnoActionCard(action, color);
				tempDeck.addAll(Arrays.asList(card, card));
				// Adds two of each action cards
			}
		}
		// Adds the action cards

		for (UnoCardColor color : UnoCardColor.values()) {
			if (color.equals(UnoCardColor.WILD))
				continue;

			for (int i = 0; i <= 9; i++) {
				UnoCard card = new UnoNumericCard(i, color);

				if (i != 0) {
					tempDeck.addAll(Arrays.asList(card, card));

				} else {
					tempDeck.add(card);
					// Yes, there's just one of each 0 cards in the official rules
				}
			}
			// Adds all standard number cards

			tempDeck.addAll(Arrays.asList(new UnoDrawCard(color), new UnoDrawCard(color)));
			// Adds two of each color's draw two cards
		}
		// Adds colored (non-wild) cards

		tempDeck.addAll(Arrays.asList(
			// 4 wild draw four cards
			new UnoDrawCard(), new UnoDrawCard(), new UnoDrawCard(), new UnoDrawCard(),

			// 4 wild cards
			new UnoWildCard(), new UnoWildCard(), new UnoWildCard(), new UnoWildCard()));
		// Adds generic cards

		DECK = Collections.unmodifiableList(tempDeck);
	}

	@Override
	public int getExpectedSize() {
		return EXPECTED_SIZE; // NOSONAR
	}

	@Override
	public List<UnoCard> getCards() {
		return new ArrayList<>(DECK);
		// Returns a CLONE of the deck
	}

}
