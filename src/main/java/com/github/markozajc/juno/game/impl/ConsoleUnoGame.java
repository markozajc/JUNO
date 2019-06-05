package com.github.markozajc.juno.game.impl;

import com.github.markozajc.juno.cards.UnoStandardDeck;
import com.github.markozajc.juno.game.UnoControlledGame;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.players.UnoPlayer;
import com.github.markozajc.juno.players.impl.ConsoleUnoPlayer;
import com.github.markozajc.juno.players.impl.StrategicUnoPlayer;
import com.github.markozajc.juno.rules.pack.impl.UnoOfficialRules;

/**
 * A console-based {@link UnoControlledGame} implementation. This is not meant to be
 * used in production and is solely an example implementation.
 *
 * @author Marko Zajc
 */
public class ConsoleUnoGame extends UnoControlledGame {

	/**
	 * Creates a new {@link ConsoleUnoGame} with a {@link ConsoleUnoPlayer} named "You"
	 * and a {@link StrategicUnoPlayer} named "Billy the StrategicUnoHand".
	 */
	public ConsoleUnoGame() {
		super(new ConsoleUnoPlayer("You"), new StrategicUnoPlayer("Billy the StrategicUnoHand"), new UnoStandardDeck(),
				7, UnoOfficialRules.getPack());
	}

	/**
	 * The main method
	 *
	 * @param args
	 *            arguments (will be ignored)
	 */
	public static void main(String[] args) {
		UnoGame game = new ConsoleUnoGame();

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
	}

}
