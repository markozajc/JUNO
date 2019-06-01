package com.github.markozajc.juno.rules.types;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.game.UnoControlledGame;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.rules.UnoRule;
import com.github.markozajc.juno.rules.types.flow.UnoFlowPhaseConclusion;
import com.github.markozajc.juno.rules.types.flow.UnoTurnInitializationConclusion;

/**
 * A rule type that controls the flow of the game in {@link UnoControlledGame}.
 *
 * @author Marko Zajc
 */
public interface UnoGameFlowRule extends UnoRule {

	/**
	 * The method that is called at the beginning of a turn.
	 *
	 * @param hand
	 *            {@link UnoHand} of this turn
	 * @param game
	 *            the ongoing {@link UnoGame}
	 * @return a {@link UnoTurnInitializationConclusion}
	 */
	@SuppressWarnings("unused")
	public default UnoTurnInitializationConclusion turnInitialization(@Nonnull UnoHand hand, @Nonnull UnoGame game) {
		return UnoTurnInitializationConclusion.NOTHING;
	}

	/**
	 * The method that is called after the {@link UnoHand} decides a card.
	 *
	 * @param hand
	 *            {@link UnoHand} of this turn
	 * @param game
	 *            the ongoing {@link UnoGame}
	 * @param decidedCard
	 *            the {@link UnoCard} the hand has decided to play (or {@code null} -
	 *            request to draw a card)
	 * @return a {@link UnoFlowPhaseConclusion}
	 */
	@SuppressWarnings("unused")
	public default UnoFlowPhaseConclusion afterHandDecision(@Nonnull UnoHand hand, @Nonnull UnoGame game, @Nullable UnoCard decidedCard) {
		return UnoFlowPhaseConclusion.NOTHING;
	}

}
