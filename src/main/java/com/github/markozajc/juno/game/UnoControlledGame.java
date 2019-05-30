package com.github.markozajc.juno.game;

import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.decks.UnoDeck;
import com.github.markozajc.juno.hands.UnoHand;
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
		rules.forEach(r -> r.turnInitialization(hand, this));
		UnoCard card = hand.playCard(this, false);
		rules.forEach(r -> r.afterHandDecision(hand, this, card));
	}

}
