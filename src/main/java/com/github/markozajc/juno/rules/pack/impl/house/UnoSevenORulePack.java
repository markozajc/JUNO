package com.github.markozajc.juno.rules.pack.impl.house;

import static com.github.markozajc.juno.rules.types.flow.UnoInitializationConclusion.NOTHING;

import javax.annotation.Nonnull;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.impl.UnoNumericCard;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.players.UnoPlayer;
import com.github.markozajc.juno.rules.pack.UnoRulePack;
import com.github.markozajc.juno.rules.pack.impl.UnoOfficialRules;
import com.github.markozajc.juno.rules.pack.impl.UnoOfficialRules.UnoHouseRule;
import com.github.markozajc.juno.rules.types.UnoGameFlowRule;
import com.github.markozajc.juno.rules.types.flow.*;

/**
 * A house {@link UnoRulePack} that implements the official SevenO house rule. The
 * rule adds the mechanic of swapping hands whenever a {@link UnoNumericCard} with
 * the number seven (7) or zero (0) is placed. As JUNO only supports two-player
 * games, both of the numbers act the same (instead of seven allowing you to choose
 * the player to swap hands with and zero forcing you to swap cards with the player
 * next to you). This {@link UnoRulePack} is also referenced by
 * {@link UnoHouseRule#SEVENO}, which is makes it easy to install into
 * {@link UnoOfficialRules}.
 *
 * @author Marko Zajc
 */
public class UnoSevenORulePack {

	private UnoSevenORulePack() {}

	private static UnoRulePack pack;

	private static void createPack() {
		pack = new UnoRulePack(new FlowRule());
	}

	static class FlowRule implements UnoGameFlowRule {

		@Override
		public UnoInitializationConclusion initializationPhase(UnoPlayer player, UnoGame game) {
			if (game.getTopCard() instanceof UnoNumericCard && game.getTopCard().isOpen()
				&& (((UnoNumericCard) game.getTopCard()).getNumber() == 0
					|| ((UnoNumericCard) game.getTopCard()).getNumber() == 7)) {
				// If the top card is a numeric card with a seven or a zero

				game.getTopCard().markClosed();

				UnoPlayer foe = game.getNextPlayer(player);
				UnoHand playerHand = player.getHand();
				UnoHand foeHand = foe.getHand();
				player.setHand(foeHand);
				foe.setHand(playerHand);
				// Swap hands

				game.onEvent("Players swap hands.", (Object[]) null);
			}

			return NOTHING;
		}

		@Override
		public UnoPhaseConclusion decisionPhase(UnoPlayer player, UnoGame game, UnoCard decidedCard) {
			if (decidedCard instanceof UnoNumericCard && !decidedCard.isOpen()
				&& (((UnoNumericCard) decidedCard).getNumber() == 0
					|| ((UnoNumericCard) decidedCard).getNumber() == 7)) {
				decidedCard.markOpen();
			}

			return NOTHING;
		}

	}

	/**
	 * @return the SevenO house {@link UnoRulePack}
	 */
	@Nonnull
	@SuppressWarnings("null")
	public static UnoRulePack getPack() {
		if (pack == null)
			createPack();

		return pack;
	}

}
