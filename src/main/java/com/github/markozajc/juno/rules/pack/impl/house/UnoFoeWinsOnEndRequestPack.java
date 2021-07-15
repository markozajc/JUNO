package com.github.markozajc.juno.rules.pack.impl.house;

import javax.annotation.Nonnull;

import com.github.markozajc.juno.game.*;
import com.github.markozajc.juno.game.UnoWinner.UnoEndReason;
import com.github.markozajc.juno.players.UnoPlayer;
import com.github.markozajc.juno.rules.pack.UnoRulePack;
import com.github.markozajc.juno.rules.types.UnoGameFlowRule;
import com.github.markozajc.juno.rules.types.flow.UnoFinishConclusion;

/**
 * By default calling {@link UnoGame#endGame()} will result in a draw. Apply this
 * rule pack if you want the other player to win in such case.
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
			if (winner.getEndReason() == UnoEndReason.REQUESTED && lastPlayer != null)
				return new UnoFinishConclusion(game.getNextPlayer());
			else
				return UnoFinishConclusion.NOTHING;
		}

	}

	/**
	 * @return the "Foe Wins on End Request" house {@link UnoRulePack}
	 */
	@SuppressWarnings("null")
	@Nonnull
	public static UnoRulePack getPack() {
		if (pack == null)
			createPack();

		return pack;
	}

}
