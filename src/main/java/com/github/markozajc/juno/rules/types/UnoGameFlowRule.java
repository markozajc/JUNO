package com.github.markozajc.juno.rules.types;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.game.UnoControlledGame;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.players.UnoPlayer;
import com.github.markozajc.juno.rules.UnoRule;
import com.github.markozajc.juno.rules.types.flow.UnoInitializationConclusion;
import com.github.markozajc.juno.rules.types.flow.UnoPhaseConclusion;

/**
 * A rule type that controls the flow of the game in {@link UnoControlledGame}. Flow
 * control comes in two parts (phases):
 * <ul>
 * <li><b>initialization</b> is the phase called at the beginning of a turn. Rules
 * should activate and close their respective {@link UnoCard} in this phase. This
 * phase returns {@link UnoInitializationConclusion}, which allows repeating the
 * phase and skipping the next (decision) phase - also skipping the player's turn.
 * <li><b>decision</b> is the phase called right after the {@link UnoPlayer} decides
 * a card to place. The rules that implement this phase should be careful not to
 * conflict with each other. The goal of this phase is getting the decided
 * {@link UnoCard} onto the discard pile and opening the cards that will be activated
 * in the next turn by the initialization phase. This phase returns a
 * {@link UnoPhaseConclusion}, which allows repeating the phase.
 * </ul>
 *
 * @author Marko Zajc
 */
public interface UnoGameFlowRule extends UnoRule {

	/**
	 * The phase that is called at the beginning of a turn. This is the phase where you
	 * activate and close {@link UnoCard}s.
	 *
	 * @param player
	 *            {@link UnoPlayer} of this turn
	 * @param game
	 *            the ongoing {@link UnoGame}
	 * @return a {@link UnoInitializationConclusion}
	 */
	@SuppressWarnings("unused")
	public default UnoInitializationConclusion initializationPhase(@Nonnull UnoPlayer player, @Nonnull UnoGame game) {
		return UnoInitializationConclusion.NOTHING;
	}

	/**
	 * The phase that is called after the {@link UnoHand} decides a card. This phase is
	 * meant for marking {@link UnoCard}s as open so that they can be activated and
	 * closed in the {@link #initializationPhase(UnoPlayer, UnoGame)} of the next round.
	 * It is generally not a good idea to activate the placed card in this phase as that
	 * might conflict with other rules.
	 *
	 * @param player
	 *            {@link UnoPlayer} of this turn
	 * @param game
	 *            the ongoing {@link UnoGame}
	 * @param decidedCard
	 *            the {@link UnoCard} the hand has decided to place (or {@code null} -
	 *            request to draw a card)
	 * @return a {@link UnoPhaseConclusion}
	 */
	@SuppressWarnings("unused")
	public default UnoPhaseConclusion decisionPhase(@Nonnull UnoPlayer player, @Nonnull UnoGame game, @Nullable UnoCard decidedCard) {
		return UnoPhaseConclusion.NOTHING;
	}

}
