package com.github.markozajc.juno.rules.pack.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.github.markozajc.juno.cards.impl.UnoDrawCard;
import com.github.markozajc.juno.rules.impl.flow.ActionCardRule;
import com.github.markozajc.juno.rules.impl.flow.CardDrawingRule;
import com.github.markozajc.juno.rules.impl.flow.CardPlacementRule;
import com.github.markozajc.juno.rules.impl.flow.ColorChoosingRule;
import com.github.markozajc.juno.rules.impl.placement.ActionPlacementRules;
import com.github.markozajc.juno.rules.impl.placement.ColorPlacementRules;
import com.github.markozajc.juno.rules.impl.placement.DrawPlacementRules;
import com.github.markozajc.juno.rules.impl.placement.NumericPlacementRules;
import com.github.markozajc.juno.rules.pack.UnoRulePack;
import com.github.markozajc.juno.rules.pack.impl.house.UnoSevenORulePack;

/**
 * Placement rules for {@link UnoDrawCard}.
 *
 * @author Marko Zajc
 */
public class UnoOfficialRules {

	/**
	 * An enum of supported "house rules". House rules are official optional changes to
	 * the flow of the game that introduce new strategies and gameplay. They can be
	 * included into a pack using {@link #getPack(List)} or
	 * {@link #getPack(UnoHouseRule...)}.
	 *
	 * @author Marko Zajc
	 */
	public enum UnoHouseRule {

		/**
		 * The SevenO house rules; placing a card with a zero or a seven swaps hands. See its
		 * rule pack ({@link UnoSevenORulePack}) for more details.
		 *
		 * @see UnoSevenORulePack
		 */
		SEVENO(UnoSevenORulePack.getPack(), "SevenO");

		private final UnoRulePack pack;
		private final String name;

		private UnoHouseRule(UnoRulePack pack, String name) {
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
			DrawPlacementRules.getPack(), NumericPlacementRules.getPack(), new UnoRulePack(new CardDrawingRule(),
					new CardPlacementRule(), new ColorChoosingRule(), new ActionCardRule()));
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
	 * @return {@link UnoRulePack} of the official UNO rules with house rules
	 */
	@SuppressWarnings("null")
	@Nonnull
	public static UnoRulePack getPack(@Nonnull List<UnoHouseRule> houseRules) {
		return getPack().addPacks(houseRules.stream().map(UnoHouseRule::getPack).collect(Collectors.toList()));
	}

}
