package com.github.markozajc.juno.rules.impl.flow;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.UnoCardColor;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.players.UnoPlayer;
import com.github.markozajc.juno.rules.types.UnoGameFlowRule;
import com.github.markozajc.juno.rules.types.flow.UnoInitializationConclusion;

/**
 * The game flow rule responsible for letting hands change the color of wild
 * {@link UnoCard}s.
 *
 * @author Marko Zajc
 */
public class ColorChoosingRule implements UnoGameFlowRule {

	private static final String INVALID_COLOR = "%s tried to set an invalid color.";
	private static final String COLOR_CHANGED = "%s set the color to %s.";

	@Override
	public UnoInitializationConclusion initializationPhase(UnoPlayer player, UnoGame game) {
		if (game.getTopCard() != null && game.getTopCard().getColor().equals(UnoCardColor.WILD) && game.getTopCard().isPlayed()) {
			UnoCardColor color = game.getTopCard().getPlacer().chooseColor(game);

			if (color.equals(UnoCardColor.WILD)) {
				game.onEvent(INVALID_COLOR, game.getTopCard().getPlacer().getName());
				return new UnoInitializationConclusion(true, false);
			}

			game.getTopCard().setColorMask(color);
			game.onEvent(COLOR_CHANGED, game.getTopCard().getPlacer().getName(), color.toString());
		}

		return UnoInitializationConclusion.NOTHING;
	}



}
