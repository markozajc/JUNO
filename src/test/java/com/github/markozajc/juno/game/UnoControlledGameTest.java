package com.github.markozajc.juno.game;

import javax.annotation.*;

import org.junit.jupiter.api.Test;

import com.github.markozajc.juno.decks.UnoDeck;
import com.github.markozajc.juno.decks.impl.UnoStandardDeck;
import com.github.markozajc.juno.players.UnoPlayer;
import com.github.markozajc.juno.players.impl.UnoStrategicPlayer;
import com.github.markozajc.juno.rules.pack.UnoRulePack;
import com.github.markozajc.juno.rules.pack.impl.UnoOfficialRules;
import com.github.markozajc.juno.rules.pack.impl.UnoOfficialRules.UnoHouseRule;

import static org.junit.jupiter.api.Assertions.*;

class UnoControlledGameTest {

	private static final int ROUNDS = 10000;

	private static final class UnoStressTestGame extends UnoControlledGame {

		public UnoStressTestGame(@Nonnull UnoPlayer first, @Nonnull UnoPlayer second, @Nonnull UnoDeck unoDeck,
								 @Nonnegative int cardAmount, @Nonnull UnoRulePack rules) {
			super(first, second, unoDeck, cardAmount, rules);
		}

		@Override
		public void onEvent(String format, Object... arguments) {}
	}

	private static final String DEBUG_FORMAT =
		"\nDebug information: EXT:%s,EXM:%s,RDN:%s,H1C:%s,H2C:%s,DRC:%s,DIC:%s,TCR:%s";

	private static final String gatherDebugInfo(UnoGame game, int i, Exception e) {
		return String.format(DEBUG_FORMAT, e.getClass().getSimpleName(), e.getMessage(), i,
							 game.getFirstPlayer().getHand().getSize(), game.getSecondPlayer().getHand().getSize(),
							 game.getDraw().getSize(), game.getDiscard().getSize(), game.getTopCard());
	}

	@Test
	void testStress() {
		System.out.println("[==== INITIATING THE STRESS TEST ====]");

		UnoGame game =
			new UnoStressTestGame(new UnoStrategicPlayer("P1"), new UnoStrategicPlayer("P2"), UnoStandardDeck.getDeck(),
								  7, UnoOfficialRules.getPack(UnoHouseRule.values()));

		for (int i = 0; i < ROUNDS; i++) {
			try {
				game.play();

				assertEquals(game.getDiscard().getSize() + game.getDraw().getSize()
					+ game.getFirstPlayer().getHand().getSize() + game.getSecondPlayer().getHand().getSize(),
							 UnoStandardDeck.getExpectedSize());
			} catch (Exception e) {
				e.printStackTrace();
				fail("The stress test has failed. " + gatherDebugInfo(game, i, e));
			}
		}

		System.out.println("[==== STRESS TEST PLAYED " + ROUNDS + " ROUNDS ====]");
	}
}
