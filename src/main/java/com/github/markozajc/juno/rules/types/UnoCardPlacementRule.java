package com.github.markozajc.juno.rules.types;

import javax.annotation.Nonnull;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.piles.impl.UnoDiscardPile;
import com.github.markozajc.juno.rules.UnoRule;

/**
 * A rule type that defines what {@link UnoCard} can be placed on what
 * {@link UnoCard}.
 *
 * @author Marko Zajc
 */
public interface UnoCardPlacementRule extends UnoRule<UnoCardPlacementRule> {

	/**
	 * The placement clearance for a card that determines whether a card can be placed
	 * atop of a card or not. The rules for deciding whether a card can be placed in a
	 * multirule environment are as follows:
	 * <ul>
	 * <li>If all are NEUTRAL the card <b>can't be placed</b>
	 * <li>If there are NEUTRAL but at there's least one ALLOWED the card <b>can be
	 * placed</b>
	 * <li>If all are ALLOWED the card <b>can be placed</b>
	 * <li>If there are ALLOWED but there's at least one PROHIBITED the card <b>can't be
	 * placed</b>
	 * <li>If all are PROHIBITED <b>can't be placed</b>
	 * </ul>
	 *
	 * @author Marko Zajc
	 */
	public enum PlacementClearance {

		/**
		 * The cards may be placed on each other (for example Blue 1 on Blue 2)
		 */
		ALLOWED,
		/**
		 * That is out of this rule's field (for example Blue 1 on Green draw two)
		 */
		NEUTRAL,
		/**
		 * The cards must not be placed atop of each other (for example Wild draw four while
		 * its holder has a card that's the color of the top card - the extra hitch to the
		 * Wild draw four that's in the official UNO rules)
		 */
		PROHIBITED;

	}

	/**
	 * Checks whether a certain card can be placed atop of a target card.
	 *
	 * @param target
	 *            the target card (eg. the top {@link UnoCard} of the
	 *            {@link UnoDiscardPile})
	 * @param card
	 *            the of card to check
	 * @param hand
	 *            the {@link UnoHand} placing the card
	 * @return whether {@code card} can be placed atop of {@code target}. This should
	 *         return ALLOWED if the cards may be placed atop of each other (for example
	 *         Blue 1 on Blue 2), NEUTRAL if that is out of this rule's field (for
	 *         example Blue 1 on Green draw two) and PROHOBITED if the cards must not be
	 *         placed atop of each other (for example Wild draw four while its holder has
	 *         a card that's the color of the top card - the extra hitch to the Wild draw
	 *         four that's in the official UNO rules)
	 */
	public abstract PlacementClearance canBePlaced(@Nonnull UnoCard target, @Nonnull UnoCard card, @Nonnull UnoHand hand);

}
