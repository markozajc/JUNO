package com.github.markozajc.juno.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import com.github.markozajc.juno.cards.UnoStandardDeck;
import com.github.markozajc.juno.hands.impl.StrategicUnoHand;
import com.github.markozajc.juno.rules.pack.impl.UnoOfficialRules;

class UnoControlledGameTest {

	private static final String DEBUG_FORMAT = "\nDebug information: EXT:%s,EXM:%s,RDN:%s,H1C:%s,H2C:%s,DRC:%s,DIC:%s,TCR:%s";

	private static final String gatherDebugInfo(UnoGame game, int i, Exception e) {
		return String.format(DEBUG_FORMAT, e.getClass().getSimpleName(), e.getMessage(), i,
			game.playerOneHand.getSize(), game.playerTwoHand.getSize(), game.getDraw().getSize(),
			game.getDiscard().getSize(), game.getTopCard());
	}

	@Test
	void stressTest() {
		System.out.println("[==== INITIATING THE STRESS TEST ====]");

		UnoGame game = new UnoControlledGame(new StrategicUnoHand("P1"), new StrategicUnoHand("P1"),
				new UnoStandardDeck(), 7, UnoOfficialRules.getPack()) {

			@Override
			public void onEvent(String format, Object... arguments) {
				System.out.printf(format + " ", arguments);
			}
		};

		for (int i = 0; i < 5000; i++) {
			try {
				System.out.println("\n[R" + i + "======== " + game.playGame().toString() + " won ===========]");
				assertEquals(game.getDiscard().getSize() + game.getDraw().getSize() + game.playerOneHand.getSize()
						+ game.playerTwoHand.getSize(),
					game.getDeck().getExpectedSize());
			} catch (Exception e) {
				fail("The stress test has failed. " + gatherDebugInfo(game, i, e));
			}
		}
	}
}
