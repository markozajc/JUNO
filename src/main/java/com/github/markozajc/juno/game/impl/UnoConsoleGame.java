package com.github.markozajc.juno.game.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.annotation.Nonnull;

import com.github.markozajc.juno.decks.impl.UnoStandardDeck;
import com.github.markozajc.juno.game.UnoControlledGame;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.players.UnoPlayer;
import com.github.markozajc.juno.players.impl.UnoConsolePlayer;
import com.github.markozajc.juno.players.impl.UnoStrategicPlayer;
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

	@SuppressWarnings({
			"resource", "null"
	})
	@Nonnull
	private static UnoRulePack getRulePack() {
		List<UnoHouseRule> rules = new ArrayList<>();
		Scanner s = new Scanner(System.in);
		for (UnoHouseRule rule : UnoHouseRule.values()) {
			System.out.print("Activate the " + rule.getName() + " house rule? [y/n] ");
			if (s.nextLine().equalsIgnoreCase("y"))
				rules.add(rule);
		}

		return UnoOfficialRules.getPack(rules.toArray(new UnoHouseRule[rules.size()]));
	}

	/**
	 * Creates a new {@link UnoConsoleGame} with a {@link UnoConsolePlayer} named "You"
	 * and a {@link UnoStrategicPlayer} named "Billy the StrategicUnoHand".
	 */
	public UnoConsoleGame() {
		super(new UnoConsolePlayer("You"), new UnoStrategicPlayer("Billy the StrategicUnoHand"), new UnoStandardDeck(),
				7, getRulePack());
	}

	/**
	 * The main method
	 *
	 * @param args
	 *            arguments (will be ignored)
	 */
	public static void main(String[] args) {
		UnoGame game = new UnoConsoleGame();

		UnoPlayer winner = game.playGame();
		if (winner == null) {
			System.out.println("It's a draw!");

		} else {
			System.out.println(winner.getName() + " won!");
		}
	}

	@Override
	public void onEvent(String format, Object... arguments) {
		System.out.printf(format, arguments);
		System.out.println();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

}
