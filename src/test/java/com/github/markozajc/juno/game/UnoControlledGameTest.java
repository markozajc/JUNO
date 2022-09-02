package com.github.markozajc.juno.game;

import static java.lang.String.format;
import static java.lang.System.out;

import javax.annotation.*;

import org.junit.jupiter.api.Test;

import com.github.markozajc.juno.decks.UnoDeck;
import com.github.markozajc.juno.decks.impl.UnoStandardDeck;
import com.github.markozajc.juno.players.UnoPlayer;
import com.github.markozajc.juno.players.impl.UnoStrategicPlayer;
import com.github.markozajc.juno.rules.pack.UnoRulePack;
import com.github.markozajc.juno.rules.pack.impl.UnoOfficialRules;
import com.github.markozajc.juno.rules.pack.impl.UnoOfficialRules.UnoHouseRule;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class UnoControlledGameTest {

	private static final int ROUNDS = 10000;

	private static final class UnoStressTestGame extends UnoControlledGame {

		public UnoStressTestGame(@Nonnull UnoPlayer first, @Nonnull UnoPlayer second, @Nonnull UnoDeck unoDeck,
								 @Nonnegative int cardAmount, @Nonnull UnoRulePack rules) {
			super(unoDeck, cardAmount, rules, first, second);
		}

		@Override
		public void onEvent(String format, Object... arguments) {}
	}

	private static final String DEBUG_FORMAT =
		"\nDebug information: EXT:%s,EXM:%s,RDN:%s,DRC:%s,DIC:%s,TCR:%s";
	private static final String HAND_COUNT_FORMAT = ",H%sC:%s";

	private static String gatherDebugInfo(UnoGame game, int i, Exception e) {
		StringBuilder builder = new StringBuilder();
		builder.append(format(DEBUG_FORMAT, e.getClass().getSimpleName(), e.getMessage(), i,
				game.getDraw().getSize(), game.getDiscard().getSize(), game.getTopCard()));
		for (int p = 0; p < game.getPlayers().size(); p++) {
			UnoPlayer player = game.getPlayers().get(p);
			builder.append(format(HAND_COUNT_FORMAT, p, player.getCards().size()));
		}
		return builder.toString();
	}

	@Test
	void testStress() {
		out.println("[==== INITIATING THE STRESS TEST ====]");

		UnoGame game =
			new UnoStressTestGame(new UnoStrategicPlayer("P1"), new UnoStrategicPlayer("P2"), UnoStandardDeck.getDeck(),
								  7, UnoOfficialRules.getPack(UnoHouseRule.values()));

		for (int i = 0; i < ROUNDS; i++) {
			try {
				game.play();

				assertEquals(game.getDiscard().getSize() + game.getDraw().getSize()
					+ game.getPlayers().stream().mapToInt(player -> player.getCards().size()).sum(),
							 UnoStandardDeck.getExpectedSize());
			} catch (Exception e) {
				e.printStackTrace();
				fail("The stress test has failed. " + gatherDebugInfo(game, i, e));
			}
		}

		out.println("[==== STRESS TEST PLAYED " + ROUNDS + " ROUNDS ====]");
	}
}
