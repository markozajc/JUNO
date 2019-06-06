package com.github.markozajc.juno.players.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.UnoCardColor;
import com.github.markozajc.juno.cards.impl.UnoActionCard;
import com.github.markozajc.juno.cards.impl.UnoDrawCard;
import com.github.markozajc.juno.cards.impl.UnoNumericCard;
import com.github.markozajc.juno.cards.impl.UnoWildCard;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.players.UnoPlayer;
import com.github.markozajc.juno.rules.pack.impl.UnoOfficialRules.UnoHouseRule;
import com.github.markozajc.juno.utils.UnoRuleUtils;
import com.github.markozajc.juno.utils.UnoUtils;

/**
 * An automated hand that uses actual strategic "thinking" to decide cards and colors
 * to return. Is suitable for production so you may use it as a "CPU" opponent in
 * your code.
 *
 * @author Marko Zajc
 */
public class UnoStrategicPlayer extends UnoPlayer {

	/**
	 * Creates a new {@link UnoStrategicPlayer}.
	 *
	 * @param name
	 *            this player's name
	 */
	public UnoStrategicPlayer(@Nonnull String name) {
		super(name);
	}

	private static final int DRAW_CARD_THRESHOLD = 3;

	@Nullable
	private static List<UnoNumericCard> sevenoFilter(List<UnoCard> cards) {
		List<UnoNumericCard> applicable = new ArrayList<>(UnoUtils.filterKind(UnoNumericCard.class, cards));
		applicable.removeIf(r -> r.getNumber() == 0 || r.getNumber() == 7);
		return applicable;
	}

	@Nullable
	private static UnoNumericCard sevenoStrategy(List<UnoCard> possiblePlacements, List<Entry<Long, UnoCardColor>> colorAnalysis, UnoPlayer us, UnoPlayer foe) {
		if (us.getHand().getSize() - 1 >= foe.getHand().getSize())
			return null;

		return chooseBestColorCard(sevenoFilter(possiblePlacements), colorAnalysis);
	}

	@Nullable
	private static UnoDrawCard chooseDrawCard(List<UnoCard> possiblePlacements, List<Entry<Long, UnoCardColor>> colorAnalysis, UnoCard topCard, UnoPlayer next) {

		/*
		 * if (topCard instanceof UnoDrawCard && !((UnoDrawCard) topCard).isPlayed()) { // In
		 * case the other hand placed a draw card
		 *
		 * if (!((UnoDrawCard) topCard).getOriginalColor().equals(UnoCardColor.WILD)) { // If
		 * the card is NOT a draw four card (or any other custom wild draw card)
		 *
		 * UnoDrawCard result = chooseBestCard(possiblePlacements, colorAnalysis,
		 * UnoDrawCard.class); if (result != null) return result;
		 *
		 * } else { return UnoUtils.filterKind(UnoDrawCard.class, possiblePlacements).get(0);
		 * }
		 *
		 * } // Defends self if "attacked" with a Draw X card
		 */
		// TODO reimplement when progressive uno rule is added

		// Places an action card if possible (both action cards do the same thing in 2 player
		// games so yeah)

		if (next.getHand().getSize() <= DRAW_CARD_THRESHOLD) {
			List<UnoDrawCard> drawCards = UnoUtils.filterKind(UnoDrawCard.class, possiblePlacements);
			if (!drawCards.isEmpty())
				return drawCards.get(0);
		}
		// Places a Draw X card in case opponent's hand size is less or equal to
		// DRAW_CARD_THRESHOLD

		return null;
	}

	@Nullable
	private static <T extends UnoCard> T simpleChooseCard(List<UnoCard> possiblePlacements, List<Entry<Long, UnoCardColor>> colorAnalysis, Class<T> type) {
		return chooseBestColorCard(UnoUtils.filterKind(type, possiblePlacements), colorAnalysis);
	}

	/**
	 * Does some strategic thinking; uses the analysis of this hand's cards and tries to
	 * place the card that has the color of most cards. If, for example, the hand has two
	 * blue and one red card, this will prefer the blue cards.
	 *
	 * @param <T>
	 *            type of the {@link UnoCard} to return
	 * @param possiblePlacements
	 *            all possible cards
	 * @param colorAnalysis
	 *            color analysis of the entire hand
	 *            ({@link UnoUtils#analyzeColors(List)})
	 * @param cardType
	 * @return the best possible card or {@code null} if there are no cards of the
	 *         requested kind
	 */
	@Nullable
	private static <T extends UnoCard> T chooseBestColorCard(List<T> possiblePlacements, List<Entry<Long, UnoCardColor>> colorAnalysis) {
		if (possiblePlacements.isEmpty())
			return null;
		// In case there's no card of the requested kind

		for (Entry<Long, UnoCardColor> color : colorAnalysis) {
			if (color.getValue().equals(UnoCardColor.WILD))
				continue;
			// Skips the wild cards because it might be a good idea to save them for later

			List<T> matches = UnoUtils.getColorCards(color.getValue(), possiblePlacements);
			// Gets the cards of

			if (!matches.isEmpty())
				return matches.get(0);
		}

		return possiblePlacements.get(0);
		// Fallback method
	}

	@SuppressWarnings("null")
	@Override
	public UnoCard playCard(UnoGame game, UnoPlayer next) {
		UnoCard top = game.getDiscard().getTop();
		List<UnoCard> possible = UnoRuleUtils.combinedPlacementAnalysis(top, this.getCards(), game.getRules(),
			this.getHand());

		if (possible.isEmpty())
			return null;
		// Draws a card if no other option is possible

		List<Entry<Long, UnoCardColor>> colorAnalysis = UnoUtils.analyzeColors(getCards());
		// Analyzes the colors
		if (game.getHouseRules().contains(UnoHouseRule.SEVENO)) {
			UnoNumericCard sevenoCard = sevenoStrategy(possible, colorAnalysis, this, next);
			if (sevenoCard != null)
				return sevenoCard;
		}

		UnoDrawCard drawCard = chooseDrawCard(possible, colorAnalysis, top, next);
		if (drawCard != null)
			return drawCard;
		// Places an action card if possible

		UnoActionCard actionCard = simpleChooseCard(possible, colorAnalysis, UnoActionCard.class);
		if (actionCard != null)
			return actionCard;
		// Places an action card if possible

		List<UnoCard> possibleNumeric;
		if (game.getHouseRules().contains(UnoHouseRule.SEVENO)
				&& this.getHand().getSize() - 1 >= next.getHand().getSize()) {
			possibleNumeric = new ArrayList<>(possible);
			possibleNumeric.removeAll(sevenoFilter(possible));
		} else {
			possibleNumeric = possible;
		}
		// Remove the sevens and zeros from the list of possible cards (numeric card chooser)
		// if we do not want to place them

		UnoNumericCard numericCard = simpleChooseCard(possibleNumeric, colorAnalysis, UnoNumericCard.class);
		if (numericCard != null)
			return numericCard;
		// Places a numeric card if possible

		UnoWildCard wild = UnoUtils.filterKind(UnoWildCard.class, possible).stream().findFirst().orElse(null);
		if (wild != null)
			return wild;
		// places a Wild card if available

		return possible.get(0);
		// Places the first possible card in case none of the above choosers manage to choose
		// a viable card
	}

	@SuppressWarnings("null")
	@Override
	public UnoCardColor chooseColor(UnoGame game) {
		return UnoUtils.analyzeColors(this.getCards())
				.stream()
				.filter(p -> !p.getValue().equals(UnoCardColor.WILD))
				.findFirst()
				.orElseThrow(() -> new IllegalStateException("Couldn't choose a color (UnoUtils malfunctioned!)"))
				.getValue();
	}

	@Override
	public boolean shouldPlayDrawnCard(UnoGame game, UnoCard drawnCard, UnoPlayer next) {
		return Objects.equals(playCard(game, next), drawnCard);
	}

}
