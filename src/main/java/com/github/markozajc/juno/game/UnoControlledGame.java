package com.github.markozajc.juno.game;

import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.decks.UnoDeck;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.rules.UnoRule;
import com.github.markozajc.juno.rules.pack.UnoRulePack;
import com.github.markozajc.juno.rules.types.UnoGameFlowRule;
import com.github.markozajc.juno.rules.types.flow.UnoInitializationConclusion;
import com.github.markozajc.juno.rules.types.flow.UnoPhaseConclusion;
import com.github.markozajc.juno.utils.UnoRuleUtils;

/**
 * An implementation of {@link UnoGame} that lets you control most of the things with
 * {@link UnoRule}s.
 *
 * @author Marko Zajc
 */
public abstract class UnoControlledGame extends UnoGame {

	/**
	 * Creates a new {@link UnoControlledGame}.
	 *
	 * @param playerOneHand
	 *            the first player's hand
	 * @param playerTwoHand
	 *            the second player's hand
	 * @param unoDeck
	 *            the {@link UnoDeck} to use
	 * @param cardAmount
	 *            the amount of card each player gets initially
	 * @param rules
	 *            the {@link UnoRulePack} for this {@link UnoGame}
	 */
	public UnoControlledGame(@Nonnull UnoHand playerOneHand, @Nonnull UnoHand playerTwoHand, @Nonnull UnoDeck unoDeck,
			@Nonnegative int cardAmount, @Nonnull UnoRulePack rules) {
		super(playerOneHand, playerTwoHand, unoDeck, cardAmount, rules);
	}

	@Override
	protected void playHand(UnoHand hand) {
		List<UnoGameFlowRule> rules = UnoRuleUtils.filterRuleKind(this.getRules().getRules(), UnoGameFlowRule.class);

		boolean skip = initializationPhase(hand, this, rules);

		if (!skip)
			decisionPhase(hand, this, rules);

	}

	private static boolean initializationPhase(@Nonnull UnoHand hand, @Nonnull UnoGame game, @Nonnull List<UnoGameFlowRule> rules) {
		boolean repeat = true;
		boolean loseATurn = false;
		while (repeat) {
			repeat = false;

			for (UnoGameFlowRule rule : rules) {
				UnoInitializationConclusion tic = rule.initializationPhase(hand, game);
				if (tic.shouldRepeat())
					repeat = true;

				if (tic.shouldLoseATurn())
					loseATurn = true;
			}
		}

		return loseATurn;
	}

	private static void decisionPhase(@Nonnull UnoHand hand, @Nonnull UnoGame game, @Nonnull List<UnoGameFlowRule> rules) {
		boolean repeatDecision = true;
		while (repeatDecision) {
			repeatDecision = false;

			UnoCard decision = hand.playCard(game, false);

			for (UnoGameFlowRule rule : rules) {
				UnoPhaseConclusion fpc = rule.decisionPhase(hand, game, decision);
				if (fpc.shouldRepeat())
					repeatDecision = true;
			}

		}
	}

}
