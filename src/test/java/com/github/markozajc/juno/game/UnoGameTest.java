package com.github.markozajc.juno.game;

import static com.github.markozajc.juno.TestUtils.getDummyPlayer;
import static com.github.markozajc.juno.decks.impl.UnoStandardDeck.getDeck;
import static com.github.markozajc.juno.rules.pack.impl.UnoOfficialRules.UnoHouseRule.PROGRESSIVE;
import static java.util.Collections.emptyList;

import javax.annotation.*;

import org.junit.jupiter.api.Test;

import com.github.markozajc.juno.decks.UnoDeck;
import com.github.markozajc.juno.players.UnoPlayer;
import com.github.markozajc.juno.rules.pack.UnoRulePack;
import com.github.markozajc.juno.rules.pack.impl.UnoOfficialRules;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UnoGameTest {

	private static final class UnoTestGame extends UnoControlledGame {

		public UnoTestGame(@Nonnull UnoDeck unoDeck, @Nonnegative int cardAmount, @Nonnull UnoRulePack rules,
						   @Nonnull UnoPlayer... players) {
			super(unoDeck, cardAmount, rules, players);
		}

		@Override
		public void onEvent(String format, Object... arguments) {}
	}

	@Test
	@SuppressWarnings("null")
	void testGetNextPlayer() {
		var first = getDummyPlayer(emptyList());
		var second = getDummyPlayer(emptyList());
		var third = getDummyPlayer(emptyList());
		var game = createGame(first, second, third);

		assertEquals(second, game.getNextPlayer(first));
		assertEquals(third, game.getNextPlayer(second));
		assertEquals(first, game.getNextPlayer(third));

		game.reverseDirection();
		assertEquals(second, game.getNextPlayer(third));
		assertEquals(third, game.getNextPlayer(first));
		assertEquals(first, game.getNextPlayer(second));
	}

	@Test
	@SuppressWarnings("null")
	void testGetPreviousPlayer() {
		var first = getDummyPlayer(emptyList());
		var second = getDummyPlayer(emptyList());
		var third = getDummyPlayer(emptyList());
		var game = createGame(first, second, third);

		assertEquals(second, game.getPreviousPlayer(third));
		assertEquals(third, game.getPreviousPlayer(first));
		assertEquals(first, game.getPreviousPlayer(second));

		game.reverseDirection();
		assertEquals(second, game.getPreviousPlayer(first));
		assertEquals(third, game.getPreviousPlayer(second));
		assertEquals(first, game.getPreviousPlayer(third));
	}

	private static UnoGame createGame(@Nonnull UnoPlayer... players) {
		return new UnoTestGame(getDeck(), 7, UnoOfficialRules.getPack(PROGRESSIVE), players);
	}

}
