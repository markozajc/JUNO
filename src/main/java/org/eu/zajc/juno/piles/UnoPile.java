package org.eu.zajc.juno.piles;

import java.util.List;

import org.eu.zajc.juno.cards.UnoCard;
import org.eu.zajc.juno.hands.UnoHand;
import org.eu.zajc.juno.piles.impl.*;

/**
 * A interface representing a pile of cards. Normally a UNO games has two regular
 * piles ({@link UnoDrawPile} and {@link UnoDiscardPile}) and {@link UnoHand}s, which
 * also implement this.
 *
 * @author Marko Zajc
 */
public interface UnoPile {

	/**
	 * @return either a modifiable clone or an unmodifiable list of this pile's cards
	 */
	List<UnoCard> getCards();

	/**
	 * @return this pile's size
	 */
	int getSize();

}
