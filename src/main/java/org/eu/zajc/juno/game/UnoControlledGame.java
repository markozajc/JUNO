// SPDX-License-Identifier: GPL-3.0
/*
 * JUNO, the UNO library for Java
 * Copyright (C) 2019-2024 Marko Zajc, (Olfi01) Florian Meyer
 *
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this
 * program. If not, see <https://www.gnu.org/licenses/>.
 */
package org.eu.zajc.juno.game;

import static org.eu.zajc.juno.utils.UnoRuleUtils.filterRuleKind;

import java.util.*;

import javax.annotation.*;

import org.eu.zajc.juno.cards.UnoCard;
import org.eu.zajc.juno.decks.UnoDeck;
import org.eu.zajc.juno.players.UnoPlayer;
import org.eu.zajc.juno.rules.UnoRule;
import org.eu.zajc.juno.rules.pack.UnoRulePack;
import org.eu.zajc.juno.rules.types.UnoGameFlowRule;

/**
 * An implementation of {@link UnoGame} that lets you control most of the things with
 * {@link UnoRule}s.
 *
 * @author Marko Zajc
 */
public abstract class UnoControlledGame extends UnoGame {

	/**
	 * Creates a new {@link UnoControlledGame}.
	 *
	 * @param unoDeck
	 *            the {@link UnoDeck} to use
	 * @param cardAmount
	 *            the amount of cards each player starts with
	 * @param rules
	 *            the {@link UnoRulePack} for this {@link UnoGame}
	 * @param players
	 *            the {@link UnoPlayer}s for this {@link UnoGame}. Must have at least 2
	 *            elements
	 */
	protected UnoControlledGame(@Nonnull UnoDeck unoDeck, @Nonnegative int cardAmount, @Nonnull UnoRulePack rules,
								@Nonnull UnoPlayer... players) {
		super(unoDeck, cardAmount, rules, players);
	}

	/**
	 * Creates a new {@link UnoControlledGame}.
	 *
	 * @param unoDeck
	 *            the {@link UnoDeck} to use
	 * @param cardAmount
	 *            the amount of cards each player starts with
	 * @param rules
	 *            the {@link UnoRulePack} for this {@link UnoGame}
	 * @param random
	 *            the random number generator used throughout the game, or {@code null}
	 *            to use the default
	 * @param players
	 *            the {@link UnoPlayer}s for this {@link UnoGame}. Must have at least 2
	 *            elements
	 */
	protected UnoControlledGame(@Nonnull UnoDeck unoDeck, @Nonnegative int cardAmount, @Nonnull UnoRulePack rules,
								@Nullable Random random, @Nonnull UnoPlayer... players) {
		super(unoDeck, cardAmount, rules, random, players);
	}

	@Override
	protected void turn(@Nonnull UnoPlayer player) {
		List<UnoGameFlowRule> rules = filterRuleKind(this.getRules().getRules(), UnoGameFlowRule.class);

		boolean skip = initializationPhase(player, this, rules);

		if (!skip)
			decisionPhase(player, this, rules);

	}

	private static boolean initializationPhase(@Nonnull UnoPlayer player, @Nonnull UnoGame game,
											   @Nonnull List<UnoGameFlowRule> rules) {
		boolean repeat = true;
		boolean loseATurn = false;
		while (repeat) {
			repeat = false;

			for (UnoGameFlowRule rule : rules) {
				var result = rule.initializationPhase(player, game);
				if (result.shouldRepeat())
					repeat = true;

				if (result.shouldLoseATurn())
					loseATurn = true;
			}
		}

		return loseATurn;
	}

	private static void decisionPhase(@Nonnull UnoPlayer player, @Nonnull UnoGame game,
									  @Nonnull List<UnoGameFlowRule> rules) {
		boolean repeatDecision = true;
		while (repeatDecision) {
			repeatDecision = false;

			UnoCard decision = player.playCard(game);

			if (game.isEndRequested())
				return;
			// No need to continue

			for (UnoGameFlowRule rule : rules) {
				var result = rule.decisionPhase(player, game, decision);
				if (result.shouldRepeat())
					repeatDecision = true;
				if (result.shouldReverseDirection())
					game.reverseDirection();
			}

		}
	}

}
