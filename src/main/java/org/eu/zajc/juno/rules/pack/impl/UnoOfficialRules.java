package org.eu.zajc.juno.rules.pack.impl;

import static java.util.Arrays.asList;
import static org.eu.zajc.juno.rules.pack.UnoRulePack.ofPacks;

import java.util.Collection;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.eu.zajc.juno.cards.impl.UnoDrawCard;
import org.eu.zajc.juno.game.UnoGame;
import org.eu.zajc.juno.rules.UnoRuleConflictException;
import org.eu.zajc.juno.rules.impl.flow.*;
import org.eu.zajc.juno.rules.impl.placement.*;
import org.eu.zajc.juno.rules.pack.UnoRulePack;
import org.eu.zajc.juno.rules.pack.impl.house.*;

/**
 * Placement rules for {@link UnoDrawCard}.
 *
 * @author Marko Zajc
 */
public class UnoOfficialRules {

	/**
	 * An enum of supported "house rules". House rules are official optional changes to
	 * the flow of the game that introduce new strategies and gameplay. They can be
	 * included into a pack using {@link #getPack(Collection)} or
	 * {@link #getPack(UnoHouseRule...)}.
	 *
	 * @author Marko Zajc
	 */
	public enum UnoHouseRule {

		/**
		 * The SevenO house rule; placing a card with a zero or a seven swaps hands. See its
		 * rule pack ({@link UnoSevenORulePack}) for more details.
		 *
		 * @see UnoSevenORulePack
		 *
		 * @deprecated see {@link UnoSevenORulePack}'s deprecation message
		 */
		@Deprecated(since = "2.3", forRemoval = false)
		SEVENO(UnoSevenORulePack.getPack(), "SevenO"),

		/**
		 * The Progressive UNO house rule; {@link UnoDrawCard} of the same amount can be
		 * stacked to defend and increase penalty. See its rule pack
		 * ({@link UnoProgressiveRulePack}) for more details.
		 */
		PROGRESSIVE(UnoProgressiveRulePack.getPack(), "Progressive UNO"),

		/**
		 * Makes the opponent (the player that didn't request an end by calling
		 * {@link UnoGame#endGame()}, by default this yields a draw) win.<br>
		 * <b>NOTE: this only works in games with two players. It will do nothing
		 * otherwise.</b>
		 */
		FOE_WINS_ON_QUIT(UnoFoeWinsOnEndRequestPack.getPack(), "\"Foe wins on quit\"");

		private final UnoRulePack pack;
		private final String name;

		UnoHouseRule(UnoRulePack pack, String name) {
			this.pack = pack;
			this.name = name;
		}

		/**
		 * @return the {@link UnoRulePack} for this house rule
		 */
		public UnoRulePack getPack() {
			return this.pack;
		}

		/**
		 * @return this house rule's name
		 */
		public String getName() {
			return this.name;
		}

	}

	private UnoOfficialRules() {}

	private static UnoRulePack pack;

	private static void createPack() {
		pack = ofPacks(ActionPlacementRules.getPack(), ColorPlacementRules.getPack(), DrawPlacementRules.getPack(),
					   NumericPlacementRules.getPack(),
					   new UnoRulePack(new CardDrawingRule(), new CardPlacementRule(), new ColorChoosingRule(),
									   new SkipCardRule(), new ReverseCardRule()));
	}

	/**
	 * @return {@link UnoRulePack} of the official UNO rules
	 */
	@Nonnull
	@SuppressWarnings("null")
	public static UnoRulePack getPack() {
		if (pack == null)
			createPack();

		return pack;
	}

	/**
	 * Fetches the official UNO {@link UnoRulePack} with additional house rules.
	 *
	 * @param houseRules
	 *            house rules
	 *
	 * @return {@link UnoRulePack} of the official UNO rules with house rules
	 */
	@Nonnull
	@SuppressWarnings("null")
	public static UnoRulePack getPack(@Nonnull UnoHouseRule... houseRules) {
		return getPack(asList(houseRules));
	}

	/**
	 * Fetches the official UNO {@link UnoRulePack} with additional house rules.
	 *
	 * @param houseRules
	 *            house rules
	 *
	 * @return {@link UnoRulePack} of the official UNO rules with house rules
	 */
	@Nonnull
	@SuppressWarnings("null")
	public static UnoRulePack getPack(@Nonnull Collection<UnoHouseRule> houseRules) {
		try {
			return getPack().addPacks(houseRules.stream().map(UnoHouseRule::getPack).collect(Collectors.toList()))
				.resolveConflicts();
		} catch (UnoRuleConflictException e) {
			// Shouldn't happen, all house rule packs mustn't have a failing conflict resolution
			return getPack();
		}
	}

}
