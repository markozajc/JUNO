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
import com.github.markozajc.juno.players.UnoPlayer;

/**
 * This is not actually a test class. Instead it provides shared utility methods to
 * the tests themselves.
 *
 * @author Marko Zajc
 */
public class TestUtils {

	public static class CheckState {

		private boolean state = false;

		CheckState() {}

		public boolean isState() {
			return this.state;
		}

		public void setState(boolean state) {
			this.state = state;
		}

	}

	private static class DummyUnoPlayer extends UnoPlayer {

		public DummyUnoPlayer(@Nonnull Collection<UnoCard> cards) {
			super("DummyUnoPlayer-" + System.currentTimeMillis());
			this.getHand().getCards().addAll(cards);
		}

		@Override
		public UnoCard playCard(UnoGame game, UnoPlayer next) {
			throw new UnsupportedOperationException("DummyUnoHand can not play cards.");
		}

		@Override
		public UnoCardColor chooseColor(UnoGame game) {
			throw new UnsupportedOperationException("DummyUnoHand can not choose colors.");
		}

		@Override
		public boolean shouldPlayDrawnCard(UnoGame game, UnoCard drawnCard, UnoPlayer next) {
			throw new UnsupportedOperationException(
					"DummyUnoHand can not decide whether it should play drawn cards or not.");
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
	 * Returns a dummy {@link UnoPlayer} containing a preferred {@link Collection} of
	 * {@link UnoCard}s. Calling either {@link UnoPlayer#playCard(UnoGame, UnoPlayer)},
	 * {@link UnoPlayer#chooseColor(UnoGame)} or
	 * {@link UnoPlayer#shouldPlayDrawnCard(UnoGame, UnoCard, UnoPlayer)} will throw an
	 * {@link UnsupportedOperationException}.
	 *
	 * @param cards
	 *            {@link Collection} of {@link UnoCard}s the {@link UnoHand} should
	 *            contain
	 * @return the created {@link UnoPlayer}
	 */
	@Nonnull
	public static DummyUnoPlayer getDummyPlayer(@Nonnull Collection<UnoCard> cards) {
		return new DummyUnoPlayer(cards);
	}

	/**
	 * Returns a dummy {@link UnoDeck} containing a preferred {@link List} of cards.
	 *
	 * @param cards
	 *            {@link List} of {@link UnoCard}s the {@link UnoDeck} should contain
	 * @return the built {@link UnoDeck}
	 * @deprecated no longer of any use as {@link UnoDeck}s can be made using their
	 *             constructor
	 */
	@Deprecated
	@Nonnull
	public static UnoDeck getDummyDeck(@Nonnull List<UnoCard> cards) {
		return new UnoDeck(cards);
	}

	public static CheckState createCheckState() {
		return new CheckState();
	}

}
