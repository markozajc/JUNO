package com.github.markozajc.juno.rules.types;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.rules.UnoRule;
import com.github.markozajc.juno.rules.types.flow.UnoFlowPhaseConclusion;
import com.github.markozajc.juno.rules.types.flow.UnoTurnInitializationConclusion;

public interface UnoGameFlowRule extends UnoRule {

	public default UnoTurnInitializationConclusion turnInitialization(@Nonnull UnoHand hand, @Nonnull UnoGame game) {
		return UnoTurnInitializationConclusion.NOTHING;
	}

	public default UnoFlowPhaseConclusion afterHandDecision(@Nonnull UnoHand hand, @Nonnull UnoGame game, @Nullable UnoCard decidedCard) {
		return UnoFlowPhaseConclusion.NOTHING;
	}

}
