package org.eu.zajc.juno.rules.pack.impl.house;

import static org.eu.zajc.juno.game.UnoWinner.UnoEndReason.REQUESTED;
import static org.eu.zajc.juno.rules.types.flow.UnoFinishConclusion.NOTHING;

import javax.annotation.Nonnull;

import org.eu.zajc.juno.game.*;
import org.eu.zajc.juno.players.UnoPlayer;
import org.eu.zajc.juno.rules.pack.UnoRulePack;
import org.eu.zajc.juno.rules.types.UnoGameFlowRule;
import org.eu.zajc.juno.rules.types.flow.UnoFinishConclusion;

/**
 * By default calling {@link UnoGame#endGame()} will result in a draw. Apply this
 * rule pack if you want the other player to win in such case.<br>
 * <b>NOTE: this only works in games with two players. It will do nothing
 * otherwise.</b>
 *
 * @author Marko Zajc
 *
 */
public class UnoFoeWinsOnEndRequestPack {

	private UnoFoeWinsOnEndRequestPack() {}

	private static UnoRulePack pack;

	private static void createPack() {
		pack = new UnoRulePack(new FlowRule());
	}

	static class FlowRule implements UnoGameFlowRule {

		@Override
		public UnoFinishConclusion finishPhase(UnoWinner winner, UnoGame game) {
			UnoPlayer lastPlayer = game.getLastPlayer();
			if (game.getPlayers().size() == 2 && winner.getEndReason() == REQUESTED && lastPlayer != null)
				return new UnoFinishConclusion(game.getNextPlayer());
			else
				return NOTHING;
		}

	}

	/**
	 * @return the "Foe Wins on End Request" house {@link UnoRulePack}
	 */
	@Nonnull
	@SuppressWarnings("null")
	public static UnoRulePack getPack() {
		if (pack == null)
			createPack();

		return pack;
	}

}
