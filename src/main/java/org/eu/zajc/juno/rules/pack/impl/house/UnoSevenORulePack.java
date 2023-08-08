package org.eu.zajc.juno.rules.pack.impl.house;

import static org.eu.zajc.juno.rules.types.flow.UnoInitializationConclusion.NOTHING;

import javax.annotation.Nonnull;

import org.eu.zajc.juno.cards.UnoCard;
import org.eu.zajc.juno.cards.impl.UnoNumericCard;
import org.eu.zajc.juno.game.UnoGame;
import org.eu.zajc.juno.hands.UnoHand;
import org.eu.zajc.juno.players.UnoPlayer;
import org.eu.zajc.juno.rules.pack.UnoRulePack;
import org.eu.zajc.juno.rules.pack.impl.UnoOfficialRules;
import org.eu.zajc.juno.rules.pack.impl.UnoOfficialRules.UnoHouseRule;
import org.eu.zajc.juno.rules.types.UnoGameFlowRule;
import org.eu.zajc.juno.rules.types.flow.*;

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
 * @deprecated <b>UnoSevenORulePack currently contains several inaccuracies. This may
 *             be fixed in a further release, or it may be removed. Proceed with
 *             caution:</b><br>
 *             <ul>
 *             <li>If a player's last placed card is a zero or a seven, the swapped
 *             player wins. This is inaccurate.
 *             <li>This rule currently only supports games with two players. This is
 *             because having the player make a decision during the rule phases is
 *             currently unsupported (beyond choosing a colour for the wild card,
 *             which is already implemented in a hacky way), but would be required
 *             for the "Seven" part of the Seven-O rule.
 *             </ul>
 *
 * @author Marko Zajc
 */
@Deprecated(since = "2.3", forRemoval = false)
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

				game.onEvent("Players swap hands.");
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
