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
	 * Checks whether a certain card can be placed atop of a target card.
	 *
	 * @param target
	 *            the target card (eg. the top {@link UnoCard} of the
	 *            {@link UnoDiscardPile})
	 * @param card
	 *            the {@link Collection} of cards to analyze (eg. {@link UnoHand}'s
	 *            cards)
	 * @return whether {@code card} can be placed atop of {@code target}
	 */
	public abstract boolean canBePlaced(@Nonnull UnoCard target, @Nonnull UnoCard card);

}
