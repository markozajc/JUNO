package com.github.markozajc.juno.rules.impl.placement;

import static com.github.markozajc.juno.cards.UnoCardColor.WILD;
import static com.github.markozajc.juno.rules.types.UnoCardPlacementRule.PlacementClearance.*;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.impl.UnoDrawCard;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.rules.pack.UnoRulePack;
import com.github.markozajc.juno.rules.types.UnoCardPlacementRule;
import com.github.markozajc.juno.utils.UnoUtils;

/**
 * Placement rules for {@link UnoDrawCard}.
 *
 * @author Marko Zajc
 */
public class DrawPlacementRules {

	private DrawPlacementRules() {}

	private static UnoRulePack pack;

	private static void createPack() {
		pack = new UnoRulePack(new DrawAmountPlacementRule(), new DrawFourHitchPlacementRule(),
							   new OpenDrawCardPlacementRule());
	}

	/**
	 * An amount-based placement rule that allows {@link UnoDrawCard} of the same amount
	 * to be placed on each other.
	 *
	 * @author Marko Zajc
	 */
	public static class DrawAmountPlacementRule implements UnoCardPlacementRule {

		@Override
		public PlacementClearance canBePlaced(UnoCard target, UnoCard card, UnoHand hand) {
			if (target instanceof UnoDrawCard && card instanceof UnoDrawCard
				&& ((UnoDrawCard) target).getAmount() == ((UnoDrawCard) card).getAmount())
				return ALLOWED;
			// Checks whether target's amount matches card's amount

			return NEUTRAL;
		}
	}

	/**
	 * The extra color-based "hitch" to the draw four; prohibits placement of wild draw
	 * four on a card in case its holder has a hand of the same color as that card
	 * (doesn't apply to wild-colored cards).
	 *
	 * @author Marko Zajc
	 */
	public static class DrawFourHitchPlacementRule implements UnoCardPlacementRule {

		@Override
		public PlacementClearance canBePlaced(UnoCard target, UnoCard card, UnoHand hand) {
			if (card instanceof UnoDrawCard && target.getOriginalColor() != WILD
				&& ((UnoDrawCard) card).getAmount() == 4
				&& !UnoUtils.getColorCards(target.getOriginalColor(), hand.getCards()).isEmpty())
				return PROHIBITED;
			// Prohibits the placement of the wild draw four if the hand possesses a card that
			// has the same color as the target (assumed to be the top of the discard pile) card.

			return NEUTRAL;
		}
	}

	/**
	 * A placement rule that prohibits all cards from being placed on an open
	 * {@link UnoDrawCard}.
	 *
	 * @author Marko Zajc
	 */
	public static class OpenDrawCardPlacementRule implements UnoCardPlacementRule {

		@Override
		public PlacementClearance canBePlaced(UnoCard target, UnoCard card, UnoHand hand) {
			if (target instanceof UnoDrawCard && target.isOpen())
				return PROHIBITED;

			return NEUTRAL;
		}

	}

	/**
	 * @return {@link UnoRulePack} of the official draw placement rules
	 */
	public static UnoRulePack getPack() {
		if (pack == null)
			createPack();

		return pack;
	}

}
