package com.github.markozajc.juno.rules.types;

import java.util.Collection;
import java.util.List;

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
	 * Finds all possible cards that can be placed on a certain {@link UnoCard} from a
	 * {@link Collection} of cards.
	 *
	 * @param target
	 *            the target card (eg. the top {@link UnoCard} of the
	 *            {@link UnoDiscardPile})
	 * @param cards
	 *            the {@link Collection} of cards to analyze (eg. {@link UnoHand}'s
	 *            cards)
	 * @return a {@link List} of all possible cards from {@code cards} can be placed on
	 *         the {@code targetCard}.
	 */
	@Nonnull
	public abstract <T extends UnoCard> Collection<T> analyzePossiblePlacements(@Nonnull UnoCard target, @Nonnull Collection<T> cards);

}
