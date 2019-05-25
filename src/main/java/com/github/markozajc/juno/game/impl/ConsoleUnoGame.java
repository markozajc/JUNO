package com.github.markozajc.juno.game.impl;

import javax.annotation.Nonnull;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.UnoCardColor;
import com.github.markozajc.juno.game.BasicUnoGame;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.hands.impl.ConsoleUnoHand;
import com.github.markozajc.juno.hands.impl.StrategicUnoHand;

public class ConsoleUnoGame extends BasicUnoGame {

	public ConsoleUnoGame(@Nonnull UnoHand playerOneHand, @Nonnull UnoHand playerTwoHand) {
		super(playerOneHand, playerTwoHand);
	}

	public static void main(String[] args) {
		UnoGame game = new ConsoleUnoGame(new ConsoleUnoHand("You"), new StrategicUnoHand("Billy"));

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
	protected void onInvalidCard(UnoHand hand) {
		System.out.println(hand.getName() + " placed an invalid card.");

	}

	@Override
	protected void onCardPlaced(UnoHand hand, UnoCard card) {
		System.out.println(hand.getName() + " placed " + card + ".");
	}

}
