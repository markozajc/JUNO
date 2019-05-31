package com.github.markozajc.juno.game;

import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.decks.UnoDeck;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.rules.impl.flow.exception.UnoGameFlowException;
import com.github.markozajc.juno.rules.pack.UnoRulePack;
import com.github.markozajc.juno.rules.types.UnoGameFlowRule;
import com.github.markozajc.juno.utils.UnoRuleUtils;

public class UnoControlledGame extends UnoGame {

	public UnoControlledGame(@Nonnull UnoHand playerOneHand, @Nonnull UnoHand playerTwoHand, @Nonnull UnoDeck unoDeck,
			@Nonnegative int cardAmount, @Nonnull UnoRulePack rules) {
		super(playerOneHand, playerTwoHand, unoDeck, cardAmount, rules);
	}

	@Override
	protected void playHand(UnoHand hand) {
		List<UnoGameFlowRule> rules = UnoRuleUtils.filterRuleKind(this.getRules().getRules(), UnoGameFlowRule.class);

		boolean repeatInitialization = true;
		while (repeatInitialization) {
			repeatInitialization = false;

			for (UnoGameFlowRule rule : rules) {
				try {
					rule.turnInitialization(hand, this);
				} catch (UnoGameFlowException e) {
					if (e.shouldRepeat())
						repeatInitialization = true;
				}
			}
		}

		boolean repeatDecision = true;
		while (repeatDecision) {
			repeatDecision = false;

			UnoCard decision = hand.playCard(this, false);

			for (UnoGameFlowRule rule : rules) {
				try {
					rule.afterHandDecision(hand, this, decision);
				} catch (UnoGameFlowException e) {
					if (e.shouldRepeat())
						repeatDecision = true;
				}
			}


		}
	}

}
