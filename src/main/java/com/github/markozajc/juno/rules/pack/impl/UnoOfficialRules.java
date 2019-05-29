package com.github.markozajc.juno.rules.pack.impl;

import javax.annotation.Nonnull;

import com.github.markozajc.juno.cards.impl.UnoDrawCard;
import com.github.markozajc.juno.rules.impl.ActionPlacementRules;
import com.github.markozajc.juno.rules.impl.ColorPlacementRules;
import com.github.markozajc.juno.rules.impl.DrawPlacementRules;
import com.github.markozajc.juno.rules.impl.NumericPlacementRules;
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
			DrawPlacementRules.getPack(), NumericPlacementRules.getPack());
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
