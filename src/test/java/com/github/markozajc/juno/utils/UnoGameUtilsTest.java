package com.github.markozajc.juno.utils;

import java.util.*;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.github.markozajc.juno.TestUtils;
import com.github.markozajc.juno.TestUtils.CheckState;
import com.github.markozajc.juno.cards.*;
import com.github.markozajc.juno.cards.impl.UnoNumericCard;
import com.github.markozajc.juno.decks.UnoDeck;
import com.github.markozajc.juno.decks.impl.UnoStandardDeck;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.piles.impl.*;
import com.github.markozajc.juno.players.UnoPlayer;
import com.github.markozajc.juno.rules.pack.impl.UnoOfficialRules;

import static org.junit.jupiter.api.Assertions.*;

class UnoGameUtilsTest {

	private static class FakeUnoGame extends UnoGame {

		private final UnoDrawPile draw;
		private final UnoDiscardPile discard;
		private final CheckState checker;

		@SuppressWarnings("null")
		public FakeUnoGame(UnoDrawPile draw, UnoDiscardPile discard, CheckState checker) {
			super(TestUtils.getDummyPlayer(Collections.emptyList()), TestUtils.getDummyPlayer(Collections.emptyList()),
				  UnoStandardDeck.getDeck(), 0, UnoOfficialRules.getPack());

			this.checker = checker;
			this.draw = draw;
			this.discard = discard;
		}

		@Override
		protected void turn(UnoPlayer player) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void onEvent(String format, Object... arguments) {
			throw new UnsupportedOperationException();
		}

		@SuppressWarnings("null")
		@Override
		public UnoDrawPile getDraw() {
			return this.draw;
		}

		@SuppressWarnings("null")
		@Override
		public UnoDiscardPile getDiscard() {
			return this.discard;
		}

		@Override
		public void discardIntoDraw() {
			super.discardIntoDraw();

			this.checker.setState(true);
		}

	}

	@SuppressWarnings("null")
	@Test
	void testDrawCards() {
		UnoDeck deck = new UnoDeck(Arrays.asList(new UnoNumericCard(UnoCardColor.RED, 0)));
		/*
		 * Cards: Red 0
		 */

		List<UnoCard> toDiscard =
			Arrays.<UnoCard>asList(new UnoNumericCard(UnoCardColor.GREEN, 1), new UnoNumericCard(UnoCardColor.BLUE, 2));
		/*
		 * Cards: Green 1, Blue 2
		 */

		CheckState checker = TestUtils.createCheckState();
		// Creates a CheckState used to check whether #discardIntoDraw has been called or not

		UnoGame game = new FakeUnoGame(new UnoDrawPile(deck), new UnoDiscardPile(), checker);
		// Creates a new FakeUnoGame

		game.getDiscard().addAll(toDiscard);
		// Adds two cards to the discard (one is top and one will be merged into the draw
		// pile)

		assertEquals(deck.getCards().get(0).getColor(), UnoGameUtils.drawCards(game, 1).get(0).getColor());
		// Checks whether the drawn card is the right one (by color)

		assertFalse(checker.getState());
		// #discardIntoDraw should not have been called at this point

		assertTrue(toDiscard.stream()
			.map(UnoCard::getColor)
			.collect(Collectors.toList())
			.contains(UnoGameUtils.drawCards(game, 1).get(0).getColor()));
		// Checks whether the drawn card is the right one (by color)

		assertTrue(checker.getState());
		// #discardIntoDraw should have been called at this point
	}

}
