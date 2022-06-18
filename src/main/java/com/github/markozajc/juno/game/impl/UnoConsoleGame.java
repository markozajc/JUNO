package com.github.markozajc.juno.game.impl;

import static java.lang.System.*;
import static java.lang.Thread.*;

import java.util.*;

import javax.annotation.Nonnull;

import com.github.markozajc.juno.decks.impl.UnoStandardDeck;
import com.github.markozajc.juno.game.*;
import com.github.markozajc.juno.players.UnoPlayer;
import com.github.markozajc.juno.players.impl.*;
import com.github.markozajc.juno.rules.pack.UnoRulePack;
import com.github.markozajc.juno.rules.pack.impl.UnoOfficialRules;
import com.github.markozajc.juno.rules.pack.impl.UnoOfficialRules.UnoHouseRule;

/**
 * A console-based {@link UnoControlledGame} implementation. This is not meant to be
 * used in production and is solely an example implementation.
 *
 * @author Marko Zajc
 */
public class UnoConsoleGame extends UnoControlledGame {

	@SuppressWarnings({ "resource", "null" })
	@Nonnull
	private static UnoRulePack getRulePack() {
		var rules = new ArrayList<UnoHouseRule>();
		Scanner s = new Scanner(in);
		for (var rule : UnoHouseRule.values()) {
			out.print("Activate the " + rule.getName() + " house rule? [y/n] ");
			if ("y".equalsIgnoreCase(s.nextLine()))
				rules.add(rule);
		}

		return UnoOfficialRules.getPack(rules.toArray(new UnoHouseRule[rules.size()]));
	}

	/**
	 * Creates a new {@link UnoConsoleGame} with a {@link UnoConsolePlayer} named "You"
	 * and a {@link UnoStrategicPlayer} named "Billy the StrategicUnoHand".
	 */
	public UnoConsoleGame() {
		super(new UnoConsolePlayer("You"), new UnoStrategicPlayer("Billy the StrategicUnoHand"),
			  UnoStandardDeck.getDeck(), 7, getRulePack());
	}

	/**
	 * The main method
	 *
	 * @param args
	 *            arguments (will be ignored)
	 */
	public static void main(String[] args) {
		UnoGame game = new UnoConsoleGame();

		UnoWinner winner = game.play();
		UnoPlayer winnerPlayer = winner.getWinner();
		if (winnerPlayer == null)
			out.println("It's a draw!");
		else
			out.println(winnerPlayer.getName() + " won!");

		out.print("Reason: ");
		switch (winner.getEndReason()) {
			case REQUESTED:
				out.println("you quit.");
				break;
			case FALLBACK:
				out.println("the draw pile was depleted and there weren't any cards in the discard pile.");
				break;
			case VICTORY:
				out.println("placed all cards.");
				break;
			case UNKNOWN:
				out.println("this shouldn't have happened!" +
					"Please send a log of the game to https://github.com/markozajc/JUNO/issues.");
				break;

		}
	}

	@Override
	public void onEvent(String format, Object... arguments) {
		out.printf(format, arguments);
		out.println();
		try {
			sleep(500);
		} catch (InterruptedException e) {
			currentThread().interrupt();
		}
	}

}
