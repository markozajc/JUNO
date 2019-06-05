package com.github.markozajc.juno.rules.pack.impl;

import java.util.Arrays;
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
import com.github.markozajc.juno.rules.pack.impl.house.SevenORulePack;

/**
 * Placement rules for {@link UnoDrawCard}.
 *
 * @author Marko Zajc
 */
public class UnoOfficialRules {

	public enum UnoHouseRules {

		SEVENO(SevenORulePack.getPack());

		private final UnoRulePack pack;

		private UnoHouseRules(UnoRulePack pack) {
			this.pack = pack;
		}

		public UnoRulePack getPack() {
			return this.pack;
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
	 * @return {@link UnoRulePack} of the official draw placement rules
	 */
	@SuppressWarnings("null")
	@Nonnull
	public static UnoRulePack getPack() {
		if (pack == null)
			createPack();

		return pack;
	}

	@SuppressWarnings("null")
	@Nonnull
	public static UnoRulePack getPack(UnoHouseRules... houseRules) {
		return getPack()
				.addPacks(Arrays.asList(houseRules).stream().map(UnoHouseRules::getPack).collect(Collectors.toList()));
	}

}
