package com.mz.uno.hands.impl;

import java.util.List;
import java.util.Scanner;

import com.mz.uno.UnoGame;
import com.mz.uno.UnoUtils;
import com.mz.uno.cards.UnoCard;
import com.mz.uno.cards.UnoCardColor;
import com.mz.uno.cards.impl.UnoDrawCard;
import com.mz.uno.hands.Hand;

public class ConsoleHand extends Hand {

	private static final String INVALID_CHOICE_STRING = "Invalid choice!";
	private static final Scanner SCANNER = new Scanner(System.in);

	public ConsoleHand(String name) {
		super(name);
	}

	@Override
	public UnoCard playCard(UnoGame game, boolean drawn) {
		UnoCard top = game.discard.getTop();
		List<UnoCard> possible = UnoUtils.analyzePossibleCards(top, this.cards);

		System.out.println("Choose a card:      [" + game.playerTwoHand.getName() + "'s hand size: "
				+ game.playerTwoHand.getSize() + " | Draw pile size: " + game.draw.getSize() + " | Discard pile size: "
				+ game.discard.getSize() + " | Top card: " + game.discard.getTop() + "]");

		if (drawn) {
			System.out.println("0 - Pass");

		} else if (top instanceof UnoDrawCard && !((UnoDrawCard) top).isPlayed()) {
			System.out.println("0 - Draw " + game.discard.getConsecutiveDraw() + " cards from a " + top);

		} else {
			System.out.println("0 - Draw");
		}

		int i = 1;
		for (UnoCard card : this.cards) {
			if (possible.contains(card)) {
				System.out.println(i + " - " + card + "");
			} else {
				System.out.println(i + " - \t" + card);
			}

			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}

			i++;
		}

		while (true) {
			int choice;
			try {
				choice = Integer.parseInt(SCANNER.nextLine());
			} catch (NumberFormatException e) {
				System.out.println(INVALID_CHOICE_STRING);
				continue;
			}

			if (choice == 0)
				return null;

			if (choice > this.cards.size()) {
				System.out.println(INVALID_CHOICE_STRING);
				continue;
			}

			UnoCard card = this.cards.get(choice - 1);

			if (!possible.contains(card)) {
				System.out.println(INVALID_CHOICE_STRING);
				continue;
			}

			return card;
		}
	}

	@Override
	public UnoCardColor chooseColor(UnoGame game) {
		System.out.println("Choose a color:");

		System.out.println("0 - Yellow");
		System.out.println("1 - Red");
		System.out.println("2 - Green");
		System.out.println("3 - Blue");

		while (true) {
			int choice;
			try {
				choice = Integer.parseInt(SCANNER.nextLine());
			} catch (NumberFormatException e) {
				System.out.println(INVALID_CHOICE_STRING);
				continue;
			}

			switch (choice) {
			case 0:
				return UnoCardColor.YELLOW;
			case 1:
				return UnoCardColor.RED;
			case 2:
				return UnoCardColor.GREEN;
			case 3:
				return UnoCardColor.BLUE;
			default:
				break;
			}

			System.out.println(INVALID_CHOICE_STRING);
		}
	}

}
