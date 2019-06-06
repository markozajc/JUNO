package com.github.markozajc.juno.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import org.junit.jupiter.api.Test;

import com.github.markozajc.juno.cards.UnoStandardDeck;
import com.github.markozajc.juno.decks.UnoDeck;
import com.github.markozajc.juno.players.UnoPlayer;
import com.github.markozajc.juno.players.impl.UnoStrategicPlayer;
import com.github.markozajc.juno.rules.pack.UnoRulePack;
import com.github.markozajc.juno.rules.pack.impl.UnoOfficialRules;

class UnoControlledGameTest {

	private static final class UnoStressTestGame extends UnoControlledGame {

		public UnoStressTestGame(@Nonnull UnoPlayer first, @Nonnull UnoPlayer second, @Nonnull UnoDeck unoDeck,
				@Nonnegative int cardAmount, @Nonnull UnoRulePack rules) {
			super(first, second, unoDeck, cardAmount, rules);
		}

		@Override
		public void onEvent(String format, Object... arguments) {}
	}

	private static final String DEBUG_FORMAT = "\nDebug information: EXT:%s,EXM:%s,RDN:%s,H1C:%s,H2C:%s,DRC:%s,DIC:%s,TCR:%s";

	private static final String gatherDebugInfo(UnoGame game, int i, Exception e) {
		return String.format(DEBUG_FORMAT, e.getClass().getSimpleName(), e.getMessage(), i,
			game.getFirstPlayer().getHand().getSize(), game.getSecondPlayer().getHand().getSize(),
			game.getDraw().getSize(), game.getDiscard().getSize(), game.getTopCard());
	}

	@Test
	void stressTest() {
		System.out.println("[==== INITIATING THE STRESS TEST ====]");

		UnoGame game = new UnoStressTestGame(new UnoStrategicPlayer("P1"), new UnoStrategicPlayer("P2"),
				new UnoStandardDeck(), 7, UnoOfficialRules.getPack());

		for (int i = 0; i < 5000; i++) {
			try {
				game.playGame();

				assertEquals(game.getDiscard().getSize() + game.getDraw().getSize()
						+ game.getFirstPlayer().getHand().getSize() + game.getSecondPlayer().getHand().getSize(),
					game.getDeck().getExpectedSize());
			} catch (Exception e) {
				fail("The stress test has failed. " + gatherDebugInfo(game, i, e));
			}
		}
	}
}
