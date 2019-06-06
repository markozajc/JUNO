package com.github.markozajc.juno.rules.pack.impl.house;

import javax.annotation.Nonnull;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.impl.UnoNumericCard;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.players.UnoPlayer;
import com.github.markozajc.juno.rules.pack.UnoRulePack;
import com.github.markozajc.juno.rules.types.UnoGameFlowRule;
import com.github.markozajc.juno.rules.types.flow.UnoInitializationConclusion;
import com.github.markozajc.juno.rules.types.flow.UnoPhaseConclusion;

public class SevenORulePack {

	private SevenORulePack() {}

	private static UnoRulePack pack;

	private static void createPack() {
		pack = new UnoRulePack(new HandSwappingRule());
	}

	public static class HandSwappingRule implements UnoGameFlowRule {

		@Override
		public UnoInitializationConclusion initializationPhase(UnoPlayer player, UnoGame game) {
			if (game.getTopCard() instanceof UnoNumericCard && game.getTopCard().isOpen()
					&& (((UnoNumericCard) game.getTopCard()).getNumber() == 0
							|| ((UnoNumericCard) game.getTopCard()).getNumber() == 7)) {
				// If the top card is a numeric card with a seven or a zero

				game.getTopCard().markClosed();

				UnoPlayer foe = game.nextPlayer(player);
				UnoHand playerHand = player.getHand();
				UnoHand foeHand = foe.getHand();
				player.setHand(foeHand);
				foe.setHand(playerHand);
				// Swap hands

				game.onEvent("Swapped cards", (Object[]) null);
			}

			return UnoInitializationConclusion.NOTHING;
		}

		@Override
		public UnoPhaseConclusion decisionPhase(UnoPlayer player, UnoGame game, UnoCard decidedCard) {
			if (decidedCard instanceof UnoNumericCard && !decidedCard.isOpen()
					&& (((UnoNumericCard) decidedCard).getNumber() == 0
							|| ((UnoNumericCard) decidedCard).getNumber() == 7)) {
				decidedCard.markOpen();
			}

			return UnoInitializationConclusion.NOTHING;
		}

	}

	@SuppressWarnings("null")
	@Nonnull
	public static UnoRulePack getPack() {
		if (pack == null)
			createPack();

		return pack;
	}

}
