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
import com.github.markozajc.juno.utils.UnoRuleUtils;

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
	 * @param name
	 *            hand's name
	 * @param is
	 *            {@link InputStream} to read from
	 * @param ps
	 *            {@link PrintStream} to write to
	 */
	public StreamUnoHand(@Nonnull String name, @Nonnull InputStream is, @Nonnull PrintStream ps) {
		super(name);
		this.scanner = new Scanner(is);
		this.ps = ps;
	}

	@SuppressWarnings("null")
	@Override
	public UnoCard playCard(UnoGame game) {
		UnoCard top = game.getDiscard().getTop();
		List<UnoCard> possible = UnoRuleUtils.combinedPlacementAnalysis(top, this.cards, game.getRules(), this);

		this.ps.println(
			"Choose a card:      [" + game.playerTwoHand.getName() + "'s hand size: " + game.playerTwoHand.getSize()
					+ " | Draw pile size: " + game.getDraw().getSize() + " | Discard pile size: "
					+ game.getDiscard().getSize() + " | Top card: " + game.getDiscard().getTop() + "]");

		if (top instanceof UnoDrawCard && !((UnoDrawCard) top).isPlayed()) {
			this.ps.println("0 - Draw " + game.getDiscard().getConsecutiveDraw() + " cards from a " + top);

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

	@Override
	public boolean shouldPlayDrawnCard(UnoGame game, UnoCard drawnCard) {
		this.ps.println("You have drawn a " + drawnCard.toString() + ". Do you want to place it? [y/n]");
		return this.scanner.nextLine().equals("y");
	}

}
