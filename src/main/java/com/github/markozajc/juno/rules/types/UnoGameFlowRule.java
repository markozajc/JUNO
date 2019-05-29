package com.github.markozajc.juno.rules.types;

import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.rules.UnoRule;

public interface UnoGameFlowRule extends UnoRule {

	public void onTurn(UnoHand hand, UnoGame game);

}
