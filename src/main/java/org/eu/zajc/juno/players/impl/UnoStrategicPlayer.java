package org.eu.zajc.juno.players.impl;

import static java.lang.Math.max;
import static org.eu.zajc.juno.cards.UnoCardColor.WILD;
import static org.eu.zajc.juno.rules.pack.impl.UnoOfficialRules.UnoHouseRule.*;
import static org.eu.zajc.juno.utils.UnoRuleUtils.combinedPlacementAnalysis;
import static org.eu.zajc.juno.utils.UnoUtils.*;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.*;

import org.eu.zajc.juno.cards.*;
import org.eu.zajc.juno.cards.impl.*;
import org.eu.zajc.juno.game.UnoGame;
import org.eu.zajc.juno.players.UnoPlayer;
import org.eu.zajc.juno.rules.pack.impl.house.UnoSevenORulePack;
import org.eu.zajc.juno.utils.UnoUtils;

/**
 * An automated hand that uses strategic logic to decide cards and colors to return.
 * Is suitable for production so you may use it as a "CPU" opponent in your code.
 *
 * @author Marko Zajc
 */
public class UnoStrategicPlayer extends UnoPlayer {

	private static final int DRAW_CARD_THRESHOLD = 3;

	private static final float DESPERATION_THRESHOLD = 2;
	// desperation = (own hand size - min hand size) / threshold

	private static final float FACTOR_SEVENO_STRATEGY = .8F;
	private static final float FACTOR_PLACE_DRAW = .5F;
	private static final float FACTOR_PLACE_ACTION_SLIDE = .8F;
	private static final float FACTOR_PLACE_SKIP = .5F;
	private static final float FACTOR_PLACE_REVERSE = .6F;
	private static final float FACTOR_PLACE_NUMERIC = .8F;
	private static final float FACTOR_PLACE_WILD = .7F;
	private static final float FACTOR_PLACE_ANY = .9F;

	/**
	 * Creates a new {@link UnoStrategicPlayer}.
	 *
	 * @param name
	 *            this player's name
	 */
	public UnoStrategicPlayer(@Nonnull String name) {
		super(name);
	}

	@Override
	@SuppressWarnings("null")
	public UnoCard playCard(UnoGame game) {
		var top = game.getDiscard().getTop();
		var possible = combinedPlacementAnalysis(top, this.getCards(), game.getRules(), this.getHand());
		var next = game.getNextPlayer();

		if (possible.isEmpty())
			return null;
		// Draws a card if no other option is possible

		var colorAnalysis = analyzeColors(getCards());
		// Analyzes the colors
		if (decideRandomly(game, FACTOR_SEVENO_STRATEGY) && game.getHouseRules().contains(SEVENO)) {
			var sevenoCard = sevenoStrategy(possible, colorAnalysis, next);
			if (sevenoCard != null)
				return sevenoCard;
		}
		// Apply the Seven-O strategy if applicable

		if (decideRandomly(game, FACTOR_PLACE_DRAW)) {
			var drawCard = chooseDrawCard(possible, colorAnalysis, game, next);
			if (drawCard != null)
				return drawCard;
		}
		// Places a draw card if necessary

		if (decideRandomly(game, FACTOR_PLACE_ACTION_SLIDE) && game.getPlayers().size() == 2) {
			var skipCard = chooseCard(possible, colorAnalysis, UnoSkipCard.class);
			if (skipCard != null)
				return skipCard;

			var reverseCard = chooseCard(possible, colorAnalysis, UnoReverseCard.class);
			if (reverseCard != null)
				return reverseCard;
		}
		// Places an action card (skip or reverse) if there are two players

		if (decideRandomly(game, FACTOR_PLACE_SKIP) && next.getHandSize() < game.getNextPlayer(next).getHandSize()) {
			var skipCard = chooseCard(possible, colorAnalysis, UnoSkipCard.class);
			if (skipCard != null)
				return skipCard;
		}
		// Places a skip card if the next player has less cards than the second-next player

		if (decideRandomly(game, FACTOR_PLACE_REVERSE) && next.getHandSize() < game.getPreviousPlayer().getHandSize()) {
			var skipCard = chooseCard(possible, colorAnalysis, UnoReverseCard.class);
			if (skipCard != null)
				return skipCard;
		}
		// Places a skip card if the next player has less cards than the previous player

		List<UnoCard> possibleNumeric;
		if (game.getHouseRules().contains(SEVENO) && this.getHand().getSize() - 1 >= next.getHand().getSize()) {
			possibleNumeric = new ArrayList<>(possible);
			possibleNumeric.removeAll(sevenoFilter(possible));
		} else {
			possibleNumeric = possible;
		}
		// Remove the sevens and zeros from the list of possible cards (numeric card chooser)
		// if we do not want to place them

		if (decideRandomly(game, FACTOR_PLACE_NUMERIC)) {
			var numericCard = chooseCard(possibleNumeric, colorAnalysis, UnoNumericCard.class);
			if (numericCard != null)
				return numericCard;
		}
		// Places a numeric card if possible

		if (decideRandomly(game, FACTOR_PLACE_WILD)) {
			var wildCard = filterKind(UnoWildCard.class, possible).stream().findFirst().orElse(null);
			if (wildCard != null)
				return wildCard;
		}
		// places a Wild card if available

		if (decideRandomly(game, FACTOR_PLACE_ANY))
			return possible.get(0);
		else
			return null;
		// Places the first possible card in case none of the above choosers manage to choose
		// a viable card
	}

	private boolean decideRandomly(UnoGame game, float baseFactor) {
		float roll = ThreadLocalRandom.current().nextFloat();
		float desperation =
			max(1F, (this.getHandSize() - game.getPlayers().stream().mapToInt(UnoPlayer::getHandSize).min().orElse(0))
				/ DESPERATION_THRESHOLD);
		float factor = baseFactor * desperation;
		return factor >= roll;
	}

	@Nullable
	private static List<UnoNumericCard> sevenoFilter(List<UnoCard> cards) {
		var applicable = new ArrayList<>(filterKind(UnoNumericCard.class, cards));
		applicable.removeIf(r -> r.getNumber() == 0 || r.getNumber() == 7);
		return applicable;
	}

	/**
	 * Figures out the best card to play in a Seven-O scenario. This currently only
	 * accounts for a two-player game where both sevens and zeroes swap hands with the
	 * only opponent.
	 *
	 * @param possiblePlacements
	 *            The list of possible placements.
	 * @param colorAnalysis
	 *            The return of {@link UnoUtils#analyzeColors(Collection)}
	 * @param opponent
	 *            The other {@link UnoPlayer}
	 *
	 * @return The best {@link UnoCard} to play or {@null} to pass through.
	 *
	 * @deprecated Read {@link UnoSevenORulePack}'s documentation for the rationale.
	 */
	@Deprecated(since = "2.3", forRemoval = false)
	@Nullable
	private UnoNumericCard sevenoStrategy(List<UnoCard> possiblePlacements,
										  List<Entry<Long, UnoCardColor>> colorAnalysis, UnoPlayer opponent) {
		if (this.getHandSize() - 1 <= opponent.getHandSize())
			return null;

		return chooseBestColorCard(sevenoFilter(possiblePlacements), colorAnalysis);
	}

	@Nullable
	private static UnoDrawCard chooseDrawCard(List<UnoCard> possiblePlacements,
											  List<Entry<Long, UnoCardColor>> colorAnalysis, UnoGame game,
											  UnoPlayer next) {

		boolean shouldPlay = false;
		if (game.getHouseRules().contains(PROGRESSIVE)) {
			// Progressive UNO is enabled
			shouldPlay = game.getTopCard() instanceof UnoDrawCard || next.getHand().getSize() <= DRAW_CARD_THRESHOLD;

			// Only place if "attacked" or if opponent's amount of card is smaller than the
			// threshold

		} else {
			shouldPlay = true;
			// Always play, there's nothing to worry about
		}

		if (!shouldPlay)
			return null;

		return chooseCard(possiblePlacements, colorAnalysis, UnoDrawCard.class);
	}

	@Nullable
	private static <T extends UnoCard> T chooseCard(List<UnoCard> possiblePlacements,
													List<Entry<Long, UnoCardColor>> colorAnalysis, Class<T> type) {
		return chooseBestColorCard(filterKind(type, possiblePlacements), colorAnalysis);
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
	 *            ({@link UnoUtils#analyzeColors(Collection)})
	 *
	 * @return the best possible card or {@code null} if there are no cards of the
	 *         requested kind
	 */
	@Nullable
	private static <T extends UnoCard> T chooseBestColorCard(List<T> possiblePlacements,
															 List<Entry<Long, UnoCardColor>> colorAnalysis) {
		if (possiblePlacements.isEmpty())
			return null;
		// In case there's no card of the requested kind

		for (Entry<Long, UnoCardColor> color : colorAnalysis) {
			if (color.getValue() == WILD)
				continue;
			// Skips the wild cards because it might be a good idea to save them for later

			List<T> matches = getColorCards(color.getValue(), possiblePlacements);
			// Gets the cards of

			if (!matches.isEmpty())
				return matches.get(0);
		}

		return possiblePlacements.get(0);
		// Fallback method
	}

	@Override
	@SuppressWarnings("null")
	public UnoCardColor chooseColor(UnoGame game) {
		return analyzeColors(this.getCards()).stream()
			.filter(p -> p.getValue() != WILD)
			.findFirst()
			.orElseThrow(() -> new IllegalStateException("Couldn't choose a color (UnoUtils malfunctioned!)"))
			.getValue();
	}

	@Override
	public boolean shouldPlayDrawnCard(UnoGame game, UnoCard drawnCard) {
		return Objects.equals(playCard(game), drawnCard);
	}

}
