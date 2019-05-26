package com.github.markozajc.juno.hands.impl;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

import javax.annotation.Nonnull;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.UnoCardColor;
import com.github.markozajc.juno.cards.impl.UnoDrawCard;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.utils.UnoUtils;

/**
 * A human-driven hand that uses a {@link Scanner} to read input and sends the output
 * to the given {@link PrintStream}. Blocks invalid card and color placements
 * automatically. This is meant as an example hand.
 *
 * @author Marko Zajc
 */
public class StreamUnoHand extends UnoHand {

	private static final String INVALID_CHOICE_STRING = "Invalid choice!";

	private final Scanner scanner;
	private final PrintStream ps;

	/**
	 * Creates a new {@link StreamUnoHand}.
	 *
	 * @param name hand's name
	 * @param is {@link InputStream} to read from
	 * @param ps {@link PrintStream} to write to
	 */
	public StreamUnoHand(@Nonnull String name, @Nonnull InputStream is, @Nonnull PrintStream ps) {
		super(name);
		this.scanner = new Scanner(is);
		this.ps = ps;
	}

	@Override
	public UnoCard playCard(UnoGame game, boolean drawn) {
		UnoCard top = game.discard.getTop();
		List<UnoCard> possible = UnoUtils.analyzePossibleCards(top, this.cards);

		this.ps.println("Choose a card:      [" + game.playerTwoHand.getName() + "'s hand size: "
				+ game.playerTwoHand.getSize() + " | Draw pile size: " + game.draw.getSize() + " | Discard pile size: "
				+ game.discard.getSize() + " | Top card: " + game.discard.getTop() + "]");

		if (drawn) {
			this.ps.println("0 - Pass");

		} else if (top instanceof UnoDrawCard && !((UnoDrawCard) top).isPlayed()) {
			this.ps.println("0 - Draw " + game.discard.getConsecutiveDraw() + " cards from a " + top);

		} else {
			this.ps.println("0 - Draw");
		}

		int i = 1;
		for (UnoCard card : this.cards) {
			if (possible.contains(card)) {
				this.ps.println(i + " - " + card + "");
			} else {
				this.ps.println(i + " - \t" + card);
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
				choice = Integer.parseInt(this.scanner.nextLine());
			} catch (NumberFormatException e) {
				this.ps.println(INVALID_CHOICE_STRING);
				continue;
			}

			if (choice == 0)
				return null;

			if (choice > this.cards.size()) {
				this.ps.println(INVALID_CHOICE_STRING);
				continue;
			}

			UnoCard card = this.cards.get(choice - 1);

			if (!possible.contains(card)) {
				this.ps.println(INVALID_CHOICE_STRING);
				continue;
			}

			return card;
		}
	}

	@Override
	public UnoCardColor chooseColor(UnoGame game) {
		this.ps.println("Choose a color:");

		this.ps.println("0 - Yellow");
		this.ps.println("1 - Red");
		this.ps.println("2 - Green");
		this.ps.println("3 - Blue");

		while (true) {
			int choice;
			try {
				choice = Integer.parseInt(this.scanner.nextLine());
			} catch (NumberFormatException e) {
				this.ps.println(INVALID_CHOICE_STRING);
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

			this.ps.println(INVALID_CHOICE_STRING);
		}
	}

}
