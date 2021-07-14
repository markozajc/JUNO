package com.github.markozajc.juno.utils;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.github.markozajc.juno.cards.*;
import com.github.markozajc.juno.cards.impl.*;
import com.github.markozajc.juno.cards.impl.UnoActionCard.UnoAction;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.piles.impl.UnoDiscardPile;

/**
 * A class containing various utilities, mostly used to process lists of
 * {@link UnoCard}s.
 *
 * @author Marko Zajc
 */
public class UnoUtils {

	private UnoUtils() {}

	/**
	 * Finds all possible cards that can be placed on a certain {@link UnoCard} from a
	 * {@link List} of cards. This implements all standard UNO rules (same color for all
	 * {@link UnoCard}s, same number for all {@link UnoNumericCard}s or same action for
	 * {@link UnoActionCard}) as well as the progressive UNO rule (same quantity for
	 * {@link UnoDrawCard}s). This is intended to be utilized by {@link UnoHand}s to be
	 * able to decide between their available cards better and more easily and by
	 * {@link UnoGame} implementations to prevent cheating - accidental or not.
	 *
	 * @param targetCard
	 *            the target card (eg. the top {@link UnoCard} of the
	 *            {@link UnoDiscardPile})
	 * @param cards
	 *            the {@link Collection} of cards to analyze (eg. {@link UnoHand}'s
	 *            cards)
	 *
	 * @return a {@link List} of all possible cards from {@code cards} can be placed on
	 *         the {@code targetCard}.
	 *
	 * @deprecated This method only supports a hard-coded set of rules. Use
	 *             {@link UnoRuleUtils#combinedPlacementAnalysis(UnoCard, Collection, com.github.markozajc.juno.rules.pack.UnoRulePack, UnoHand)}
	 *             instead.
	 */
	@Deprecated
	public static List<UnoCard> analyzePossibleCards(UnoCard targetCard, Collection<UnoCard> cards) {
		List<UnoCard> result = new ArrayList<>();

		if (targetCard instanceof UnoDrawCard && ((UnoDrawCard) targetCard).isOpen()) {
			UnoDrawCard castTop = (UnoDrawCard) targetCard;

			result.addAll(UnoUtils.filterKind(UnoDrawCard.class, cards)
				.stream()
				.filter(c -> c.getAmount() == castTop.getAmount())
				.collect(Collectors.toList()));

			return result;
		}
		// Adds all allowed draw cards. This is an exception as it only returns draw cards in
		// case the other player placed a draw card (a progressive UNO thing, similar to the
		// "check" state in Chess)

		if (targetCard instanceof UnoWildCard && targetCard.getColor().equals(UnoCardColor.WILD))
			return Collections.emptyList();
		// No card can be placed on an unset wild card

		if (targetCard instanceof UnoNumericCard) {
			UnoNumericCard castTop = (UnoNumericCard) targetCard;

			result.addAll(UnoUtils.filterKind(UnoNumericCard.class, cards)
				.stream()
				.filter(c -> c.getNumber() == castTop.getNumber())
				.collect(Collectors.toList()));
		}
		// Adds all allowed numerical cards.

		else if (targetCard instanceof UnoActionCard) {
			UnoActionCard castTop = (UnoActionCard) targetCard;

			result.addAll(UnoUtils.filterKind(UnoActionCard.class, cards)
				.stream()
				.filter(c -> c.getAction() == castTop.getAction())
				.collect(Collectors.toList()));
		}
		// Adds all allowed action cards

		else if (targetCard instanceof UnoDrawCard && !((UnoDrawCard) targetCard).isOpen()) {
			UnoDrawCard castTop = (UnoDrawCard) targetCard;

			result.addAll(UnoUtils.filterKind(UnoDrawCard.class, cards)
				.stream()
				.filter(c -> c.getAmount() == castTop.getAmount())
				.collect(Collectors.toList()));
		}
		// Adds all allowed draw cards.

		result.addAll(UnoUtils.getColorCards(UnoCardColor.WILD, cards));
		// Adds all wild-colored cards. Wild cards can be placed on anything.

		if (!targetCard.getColor().equals(UnoCardColor.WILD)) {
			for (UnoCard card : UnoUtils.getColorCards(targetCard.getColor(), cards)) {
				if (!result.contains(card))
					result.add(card);
			}
		}
		// Adds all non-wild-colored cards of the same color (that haven't been added
		// already)

		return result;
	}

	/**
	 * Analyzes cards and returns them in an {@link Entry} of {@link UnoCardColor}
	 * (right) and quantity of cards of that color (left).
	 *
	 * @param cards
	 *            cards to analyze
	 *
	 * @return {@link Entry} of quantity and {@link UnoCardColor}
	 */
	public static List<Entry<Long, UnoCardColor>> analyzeColors(Collection<UnoCard> cards) {
		List<Entry<Long, UnoCardColor>> result = new ArrayList<>();

		for (UnoCardColor color : UnoCardColor.values())
			result.add(new SimpleEntry<>(cards.stream().filter(c -> c.getColor().equals(color)).count(), color));

		Collections.sort(result, (v1, v2) -> v2.getKey().compareTo(v1.getKey()));
		return result;
	}

	/**
	 * Gets all cards of a certain color from a list of {@link UnoCard}s.
	 *
	 * @param <T>
	 *            card type
	 * @param color
	 *            {@link UnoCardColor} to search for
	 * @param cards
	 *            {@link Collection} of {@link UnoCard}s to search through
	 *
	 * @return {@link List} containing only cards of a certain color
	 */
	public static <T extends UnoCard> List<T> getColorCards(UnoCardColor color, Collection<T> cards) {
		return cards.stream().filter(c -> c.getColor().equals(color)).collect(Collectors.toList());
	}

	/**
	 * Gets all {@link UnoActionCard} with a certain {@link UnoActionCard} from a list of
	 * {@link UnoCard}s.
	 *
	 * @param action
	 *            {@link UnoCardColor} to search for
	 * @param cards
	 *            {@link Collection} of {@link UnoCard}s to search through
	 *
	 * @return {@link List} containing only cards of a certain color
	 */
	public static List<UnoActionCard> getActionCards(UnoAction action, Collection<UnoCard> cards) {
		return filterKind(UnoActionCard.class, cards).stream()
			.filter(c -> c.getAction().equals(action))
			.collect(Collectors.toList());
	}

	/**
	 * Gets all {@link UnoNumericCard} with a certain number from a list of
	 * {@link UnoCard}s.
	 *
	 * @param number
	 *            number to search for
	 * @param cards
	 *            {@link List} of {@link UnoCard}s to search through
	 *
	 * @return {@link List} containing only cards of a certain color
	 */
	public static List<UnoNumericCard> getNumberCards(int number, Collection<UnoCard> cards) {
		return filterKind(UnoNumericCard.class, cards).stream()
			.filter(c -> c.getNumber() == number)
			.collect(Collectors.toList());
	}

	/**
	 * Gets all {@link UnoCard}s of a certain kind.
	 *
	 * @param <T>
	 *            the superclass of {@link UnoCard} to search for
	 * @param targetKind
	 *            the {@link Class} of {@link UnoCard} to search for
	 * @param cards
	 *            {@link Collection} of {@link UnoCard}s to search through
	 *
	 * @return {@link List} of cards of a certain kind
	 */
	public static <T extends UnoCard> List<T> filterKind(Class<T> targetKind, Collection<UnoCard> cards) {
		return cards.stream().filter(targetKind::isInstance).map(targetKind::cast).collect(Collectors.toList());
	}

}
