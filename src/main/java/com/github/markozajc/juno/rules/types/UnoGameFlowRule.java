package com.github.markozajc.juno.rules.types;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.rules.UnoRule;
import com.github.markozajc.juno.rules.impl.flow.exception.UnoGameFlowException;

public interface UnoGameFlowRule extends UnoRule {

	public default void turnInitialization(@Nonnull UnoHand hand, @Nonnull UnoGame game) throws UnoGameFlowException {}

	public default void afterHandDecision(@Nonnull UnoHand hand, @Nonnull UnoGame game, @Nullable UnoCard decidedCard)
			throws UnoGameFlowException {}

}
