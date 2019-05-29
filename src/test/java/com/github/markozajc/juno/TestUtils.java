package com.github.markozajc.juno;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.UnoCardColor;
import com.github.markozajc.juno.decks.UnoDeck;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.hands.UnoHand;

/**
 * This is not actually a test class. Instead it provides shared utility methods to
 * the tests themselves.
 *
 * @author Marko Zajc
 */
public class TestUtils {

	private static class DummyUnoHand extends UnoHand {

		public DummyUnoHand(@Nonnull Collection<UnoCard> cards) {
			super("Dummy Hand");
			this.cards.addAll(cards);
		}

		@Override
		public UnoCard playCard(UnoGame game, boolean drawn) {
			throw new UnsupportedOperationException("DummyUnoHand can not play cards.");
		}

		@Override
		public UnoCardColor chooseColor(UnoGame game) {
			throw new UnsupportedOperationException("DummyUnoHand can not choose colors.");
		}

	}

	private static class DummyUnoDeck implements UnoDeck {

		@Nonnull
		private final List<UnoCard> cards;

		public DummyUnoDeck(@Nonnull List<UnoCard> cards) {
			this.cards = cards;
		}

		@Override
		public int getExpectedSize() {
			return this.cards.size();
		}

		@Override
		public List<UnoCard> getCards() {
			return this.cards;
			// Doesn't actually make a clone. A production deck should always make a clone here.
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

	/**
	 * Returns a dummy {@link UnoDeck} containing a preferred {@link Collection} of
	 * cards. Calling either {@link UnoHand#playCard(UnoGame, boolean)} or
	 * {@link UnoHand#chooseColor(UnoGame)} will throw an
	 * {@link UnsupportedOperationException}.
	 *
	 * @param cards
	 *            {@link Collection} of {@link UnoCard}s the {@link UnoHand} should
	 *            contain
	 * @return the created {@link UnoHand}
	 */
	@Nonnull
	public static UnoHand getDummyHand(@Nonnull Collection<UnoCard> cards) {
		return new DummyUnoHand(cards);
	}

	/**
	 * Returns a dummy {@link UnoDeck} containing a preferred {@link List} of cards.
	 * {@link UnoDeck#getExpectedSize()} will contain the size of the provided
	 * {@link List}.
	 *
	 * @param cards
	 *            {@link List} of {@link UnoCard}s the {@link UnoDeck} should contain
	 * @return the built {@link UnoDeck}
	 */
	@Nonnull
	public static UnoDeck getDummyDeck(@Nonnull List<UnoCard> cards) {
		return new DummyUnoDeck(cards);
	}

}
