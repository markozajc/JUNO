package com.github.markozajc.juno.rules.types;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.rules.UnoRule;

public interface UnoGameFlowRule extends UnoRule {

	public default void turnInitialization(UnoHand hand, UnoGame game) {}

	public default void afterHandDecision(UnoHand hand, UnoGame game, UnoCard decidedCard) {}

}
