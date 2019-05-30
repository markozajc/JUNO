package com.github.markozajc.juno.rules.pack.impl;

import javax.annotation.Nonnull;

import com.github.markozajc.juno.cards.impl.UnoDrawCard;
import com.github.markozajc.juno.rules.impl.flow.CardDrawingRule;
import com.github.markozajc.juno.rules.impl.flow.CardPlacementRule;
import com.github.markozajc.juno.rules.impl.flow.ColorChoosingRule;
import com.github.markozajc.juno.rules.impl.placement.ActionPlacementRules;
import com.github.markozajc.juno.rules.impl.placement.ColorPlacementRules;
import com.github.markozajc.juno.rules.impl.placement.DrawPlacementRules;
import com.github.markozajc.juno.rules.impl.placement.NumericPlacementRules;
import com.github.markozajc.juno.rules.pack.UnoRulePack;

/**
 * Placement rules for {@link UnoDrawCard}.
 *
 * @author Marko Zajc
 */
public class UnoOfficialRules {

	private UnoOfficialRules() {}

	private static UnoRulePack pack;

	private static void createPack() {
		pack = UnoRulePack.ofPacks(ActionPlacementRules.getPack(), ColorPlacementRules.getPack(),
			DrawPlacementRules.getPack(), NumericPlacementRules.getPack(),
			new UnoRulePack(new CardDrawingRule(), new CardPlacementRule(), new ColorChoosingRule()));
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

}
