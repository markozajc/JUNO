package com.github.markozajc.juno.rules.types;

import java.util.Collection;

import javax.annotation.Nonnull;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.piles.impl.UnoDiscardPile;
import com.github.markozajc.juno.rules.UnoRule;

/**
 * A rule type that defines what cards can be placed on what.
 *
 * @author Marko Zajc
 */
public abstract class UnoCardPlacementRule extends UnoRule {

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

		ALLOWED,
		NEUTRAL,
		PROHIBITED;

	}

	/**
	 * Checks whether a certain card can be placed atop of a target card.
	 *
	 * @param target
	 *            the target card (eg. the top {@link UnoCard} of the
	 *            {@link UnoDiscardPile})
	 * @param card
	 *            the {@link Collection} of cards to analyze (eg. {@link UnoHand}'s
	 *            cards)
	 * @param hand
	 *            the {@link UnoHand} placing the card
	 * @return whether {@code card} can be placed atop of {@code target}
	 */
	public abstract PlacementClearance canBePlaced(@Nonnull UnoCard target, @Nonnull UnoCard card, @Nonnull UnoHand hand);

}
