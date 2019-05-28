package com.github.markozajc.juno;

import java.util.Collection;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.UnoCardColor;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.hands.UnoHand;

/**
 * This is not actually a test class. Instead it provides shared utility methods to
 * the tests themselves.
 *
 * @author Marko Zajc
 */
public class TestUtils {

	private static class UnoDummyHand extends UnoHand {

		public UnoDummyHand(Collection<UnoCard> cards) {
			super("Dummy Hand");
			this.cards.addAll(cards);
		}

		@Override
		public UnoCard playCard(UnoGame game, boolean drawn) {
			return this.getCards().get(0);
		}

		@Override
		public UnoCardColor chooseColor(UnoGame game) {
			return UnoCardColor.RED;
		}

	}

	/**
	 * Checks the equality of two {@link Collection}s in an unordered manner.
	 *
	 * @param <T>
	 *            collection item type
	 * @param collection1
	 *            collection number one
	 * @param collection2
	 *            collection number two
	 * @return whether the collection contain equal items or not
	 */
	public static <T> boolean listEqualsUnordered(Collection<? extends T> collection1, Collection<? extends T> collection2) {
		System.out.println("[= COMPARING COLLECTIONS /unordered =]");
		System.out.println(
			"Collection 1: " + collection1.stream().map(Object::toString).collect(Collectors.joining(",")));
		System.out.println(
			"Collection 2: " + collection2.stream().map(Object::toString).collect(Collectors.joining(",")));
		return collection1.size() == collection2.size() && collection1.containsAll(collection2);
	}

	@Nonnull
	public static UnoHand getDummyHand(Collection<UnoCard> cards) {
		return new UnoDummyHand(cards);
	}

}
