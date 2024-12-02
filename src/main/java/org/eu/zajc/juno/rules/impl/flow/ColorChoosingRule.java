// SPDX-License-Identifier: GPL-3.0
/*
 * JUNO, the UNO library for Java 
 * Copyright (C) 2019-2024 Marko Zajc
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
package org.eu.zajc.juno.rules.impl.flow;

import static org.eu.zajc.juno.cards.UnoCardColor.WILD;
import static org.eu.zajc.juno.rules.types.flow.UnoInitializationConclusion.NOTHING;

import org.eu.zajc.juno.cards.*;
import org.eu.zajc.juno.game.UnoGame;
import org.eu.zajc.juno.players.UnoPlayer;
import org.eu.zajc.juno.rules.types.UnoGameFlowRule;
import org.eu.zajc.juno.rules.types.flow.UnoInitializationConclusion;

/**
 * The game flow rule responsible for letting hands change the color of wild
 * {@link UnoCard}s.
 *
 * @author Marko Zajc
 */
public class ColorChoosingRule implements UnoGameFlowRule {

	private static final String INVALID_COLOR = "%s tries to set an invalid color.";
	private static final String COLOR_CHANGED = "%s sets the color to %s.";

	@Override
	public UnoInitializationConclusion initializationPhase(UnoPlayer player, UnoGame game) {
		if (game.getTopCard() != null && game.getTopCard().getColor() == WILD && !game.getTopCard().isOpen()) {
			UnoCardColor color = game.getTopCard().getPlacer().chooseColor(game);

			if (color == WILD) {
				game.onEvent(INVALID_COLOR, game.getTopCard().getPlacer().getName());
				return new UnoInitializationConclusion(true, false);
			}

			game.getTopCard().setColorMask(color);
			game.onEvent(COLOR_CHANGED, game.getTopCard().getPlacer().getName(), color.toString());
		}

		return NOTHING;
	}

}
