package com.github.markozajc.juno.game.impl;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.UnoCardColor;
import com.github.markozajc.juno.game.BasicUnoGame;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.hands.impl.ConsoleUnoHand;
import com.github.markozajc.juno.hands.impl.StrategicUnoHand;

/**
 * A console-based {@link BasicUnoGame} implementation. This is not meant to be used
 * in production and is solely an example implementation.
 *
 * @author Marko Zajc
 */
public class ConsoleUnoGame extends BasicUnoGame {

	/**
	 * Creates a new {@link ConsoleUnoGame} with a {@link ConsoleUnoHand} named "You" and
	 * a {@link StrategicUnoHand} named "Billy the StrategicUnoHand".
	 */
	public ConsoleUnoGame() {
		super(new ConsoleUnoHand("You"), new StrategicUnoHand("Billy the StrategicUnoHand"));
	}

	/**
	 * The main method
	 *
	 * @param args arguments (will be ignored)
	 */
	public static void main(String[] args) {
		UnoGame game = new ConsoleUnoGame();

		switch (game.playGame()) {
			case PLAYER2:
				System.out.println(game.playerTwoHand.getName() + " won!");
				break;

			case NOBODY:
				System.out.println("It's a draw!");
				break;

			case PLAYER1:
				System.out.println(game.playerOneHand.getName() + " won!");
				break;

			default:
				break;
		}
	}

	@Override
	protected void onColorChanged(UnoHand hand, UnoCardColor newColor) {
		System.out.println(hand.getName() + " chose " + newColor + ".");

	}

	@Override
	protected void onPileShuffle() {
		System.out.println("Shuffling the discard pile into draw pile..");

	}

	@Override
	protected void onDrawCards(UnoHand hand, int quantity) {
		System.out.println(hand.getName() + " drew " + quantity + " card(s).");

	}

	@Override
	protected void onInvalidCard(UnoHand hand, UnoCard card) {
		System.out.println(hand.getName() + " placed a " + card + ", which is an invalid card.");

	}

	@Override
	protected void onCardPlaced(UnoHand hand, UnoCard card) {
		System.out.println(hand.getName() + " placed " + card + ".");
	}

}
