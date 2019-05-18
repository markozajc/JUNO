package com.mz.uno;

import com.mz.uno.cards.UnoCard;
import com.mz.uno.cards.UnoCardColor;
import com.mz.uno.hands.Hand;
import com.mz.uno.hands.impl.ConsoleHand;
import com.mz.uno.hands.impl.StrategicHand;

public class ConsoleUnoGame extends BasicUnoGame {


	public ConsoleUnoGame(Hand playerOneHand, Hand playerTwoHand) {
		super(playerOneHand, playerTwoHand);
	}

	public static void main(String[] args) {
		UnoGame game = new ConsoleUnoGame(new StrategicHand("Billy"), new ConsoleHand("You"));

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
	protected void onColorChanged(Hand hand, UnoCardColor newColor) {
		System.out.println(hand.getName() + " chose " + newColor + ".");

	}

	@Override
	protected void onPileShuffle() {
		System.out.println("Shuffling the discard pile into draw pile..");

	}

	@Override
	protected void onDrawCards(Hand hand, int quantity) {
		System.out.println(hand.getName() + " drew " + quantity + " card(s).");

	}

	@Override
	protected void onInvalidCard(Hand hand) {
		System.out.println(hand.getName() + " placed an invalid card.");

	}

	@Override
	protected void onCardPlaced(Hand hand, UnoCard card) {
		System.out.println(hand.getName() + " placed " + card + ".");
	}

}
