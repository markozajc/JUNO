package com.github.markozajc.juno.rules.pack.impl;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.github.markozajc.juno.cards.impl.UnoDrawCard;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.rules.UnoRuleConflictException;
import com.github.markozajc.juno.rules.impl.flow.*;
import com.github.markozajc.juno.rules.impl.placement.*;
import com.github.markozajc.juno.rules.pack.UnoRulePack;
import com.github.markozajc.juno.rules.pack.impl.house.*;

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
		 */
		SEVENO(UnoSevenORulePack.getPack(), "SevenO"),

		/**
		 * The Progressive UNO house rule; {@link UnoDrawCard} of the same amount can be
		 * stacked to defend and increase penalty. See its rule pack
		 * ({@link UnoProgressiveRulePack}) for more details.
		 */
		PROGRESSIVE(UnoProgressiveRulePack.getPack(), "Progressive UNO"),

		/**
		 * Makes the opponent (the player that didn't request an end by calling
		 * {@link UnoGame#endGame()}, by default this yields a draw) win.
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
		pack = UnoRulePack.ofPacks(ActionPlacementRules.getPack(), ColorPlacementRules.getPack(),
								   DrawPlacementRules.getPack(), NumericPlacementRules.getPack(),
								   new UnoRulePack(new CardDrawingRule(), new CardPlacementRule(),
												   new ColorChoosingRule(), new ActionCardRule()));
	}

	/**
	 * @return {@link UnoRulePack} of the official UNO rules
	 */
	@SuppressWarnings("null")
	@Nonnull
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
	@SuppressWarnings("null")
	@Nonnull
	public static UnoRulePack getPack(@Nonnull UnoHouseRule... houseRules) {
		return getPack(Arrays.asList(houseRules));
	}

	/**
	 * Fetches the official UNO {@link UnoRulePack} with additional house rules.
	 *
	 * @param houseRules
	 *            house rules
	 *
	 * @return {@link UnoRulePack} of the official UNO rules with house rules
	 */
	@SuppressWarnings("null")
	@Nonnull
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
