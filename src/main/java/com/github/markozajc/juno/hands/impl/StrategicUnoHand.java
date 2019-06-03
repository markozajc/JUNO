package com.github.markozajc.juno.hands.impl;

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
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.utils.UnoRuleUtils;
import com.github.markozajc.juno.utils.UnoUtils;

/**
 * An automated hand that uses actual strategic "thinking" to decide cards and colors
 * to return. Is suitable for production so you may use it as a "CPU" opponent in
 * your code.
 *
 * @author Marko Zajc
 */
public class StrategicUnoHand extends UnoHand {

	/**
	 * Creates a new {@link StrategicUnoHand}.
	 *
	 * @param name
	 *            hand's name
	 */
	public StrategicUnoHand(@Nonnull String name) {
		super(name);
	}

	private static final int DRAW_CARD_THRESHOLD = 3;

	@Nullable
	private static UnoDrawCard chooseDrawCard(List<UnoCard> possiblePlacements, List<Entry<Long, UnoCardColor>> colorAnalysis, UnoGame game, UnoCard topCard) {
		if (topCard instanceof UnoDrawCard && !((UnoDrawCard) topCard).isPlayed()) {
			// In case the other hand placed a draw card

			if (!((UnoDrawCard) topCard).getOriginalColor().equals(UnoCardColor.WILD)) {
				// If the card is NOT a draw four card (or any other custom wild draw card)

				UnoDrawCard result = chooseBestCard(possiblePlacements, colorAnalysis, UnoDrawCard.class);
				if (result != null)
					return result;

			} else {
				return UnoUtils.filterKind(UnoDrawCard.class, possiblePlacements).get(0);
			}

		}
		// Defends self if "attacked" with a Draw X card

		// Places an action card if possible (both action cards do the same thing in 2 player
		// games so yeah)

		if (game.playerOneHand.getSize() <= DRAW_CARD_THRESHOLD) {
			List<UnoDrawCard> drawCards = UnoUtils.filterKind(UnoDrawCard.class, possiblePlacements);
			if (!drawCards.isEmpty())
				return drawCards.get(0);
		}
		// Places a Draw X card in case opponent's hand size is less or equal to
		// DRAW_CARD_THRESHOLD

		return null;
	}

	@Nullable
	private static UnoActionCard chooseActionCard(List<UnoCard> possiblePlacements, List<Entry<Long, UnoCardColor>> colorAnalysis) {
		return chooseBestCard(possiblePlacements, colorAnalysis, UnoActionCard.class);
	}

	@Nullable
	private static UnoNumericCard chooseNumericCard(List<UnoCard> possiblePlacements, List<Entry<Long, UnoCardColor>> colorAnalysis) {
		return chooseBestCard(possiblePlacements, colorAnalysis, UnoNumericCard.class);
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
	private static <T extends UnoCard> T chooseBestCard(List<UnoCard> possiblePlacements, List<Entry<Long, UnoCardColor>> colorAnalysis, Class<T> cardType) {
		List<T> filteredPossiblePlacements = UnoUtils.filterKind(cardType, possiblePlacements);
		if (filteredPossiblePlacements.isEmpty())
			return null;
		// In case there's no card of the requested kind

		for (Entry<Long, UnoCardColor> color : colorAnalysis) {
			if (color.getValue().equals(UnoCardColor.WILD))
				continue;
			// Skips the wild cards because it might be a good idea to save them for later

			List<T> matches = UnoUtils.getColorCards(color.getValue(), filteredPossiblePlacements);
			// Gets the cards of

			if (!matches.isEmpty())
				return matches.get(0);
		}

		return filteredPossiblePlacements.get(0);
		// Fallback method
	}

	@SuppressWarnings("null")
	@Override
	public UnoCard playCard(UnoGame game) {
		UnoCard top = game.getDiscard().getTop();
		List<UnoCard> possible = UnoRuleUtils.combinedPlacementAnalysis(top, this.cards, game.getRules(), this);

		if (possible.isEmpty())
			return null;
		// Draws a card if no other option is possible

		List<Entry<Long, UnoCardColor>> colorAnalysis = UnoUtils.analyzeColors(getCards());
		// Analyzes the colors

		UnoDrawCard drawCard = chooseDrawCard(possible, colorAnalysis, game, top);
		if (drawCard != null)
			return drawCard;
		// Places an action card if possible

		UnoActionCard actionCard = chooseActionCard(possible, colorAnalysis);
		if (actionCard != null)
			return actionCard;
		// Places an action card if possible

		UnoNumericCard numericCard = chooseNumericCard(possible, colorAnalysis);
		if (numericCard != null)
			return numericCard;
		// Places a numeric card if possible

		UnoWildCard wild = UnoUtils.filterKind(UnoWildCard.class, possible).stream().findFirst().orElse(null);
		if (wild != null)
			return wild;
		// places a Wild card if available

		return possible.get(0);
		// Places the first possible card if everything else fails
	}

	@SuppressWarnings("null")
	@Override
	public UnoCardColor chooseColor(UnoGame game) {
		return UnoUtils.analyzeColors(this.cards)
				.stream()
				.filter(p -> !p.getValue().equals(UnoCardColor.WILD))
				.findFirst()
				.orElseThrow(() -> new IllegalStateException("Couldn't choose a color (UnoUtils malfuntioned!)"))
				.getValue();
	}

	@Override
	public boolean shouldPlayDrawnCard(UnoGame game, UnoCard drawnCard) {
		return Objects.equals(playCard(game), drawnCard);
	}

}
