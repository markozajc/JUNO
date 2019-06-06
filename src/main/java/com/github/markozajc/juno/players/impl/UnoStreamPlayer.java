package com.github.markozajc.juno.players.impl;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.UnoCardColor;
import com.github.markozajc.juno.cards.impl.UnoDrawCard;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.players.UnoPlayer;
import com.github.markozajc.juno.utils.UnoRuleUtils;

/**
 * A human-driven player that uses a {@link Scanner} to read input and sends the
 * output to the given {@link PrintStream}. Blocks invalid card and color placements
 * automatically. This is meant as an example hand.
 *
 * @author Marko Zajc
 */
public class UnoStreamPlayer extends UnoPlayer {

	private static final String INVALID_CHOICE_STRING = "Invalid choice!";

	private final Scanner scanner;
	private final PrintStream ps;

	/**
	 * Creates a new {@link UnoStreamPlayer}.
	 *
	 * @param is
	 *            {@link InputStream} to read from
	 * @param ps
	 *            {@link PrintStream} to write to
	 */
	public UnoStreamPlayer(@Nonnull String name, @Nonnull InputStream is, @Nonnull PrintStream ps) {
		super(name);
		this.scanner = new Scanner(is);
		this.ps = ps;
	}

	@SuppressWarnings("null")
	@Override
	public UnoCard playCard(UnoGame game, UnoPlayer next) {
		UnoCard top = game.getDiscard().getTop();
		List<UnoCard> possible = UnoRuleUtils.combinedPlacementAnalysis(top, this.getHand().getCards(), game.getRules(),
			this.getHand());

		this.ps.println("Choose a card: [" + next.getName() + " hand size: " + next.getHand().getSize()
				+ " | Draw pile size: " + game.getDraw().getSize() + " | Discard pile size: "
				+ game.getDiscard().getSize() + " | Top card: " + game.getDiscard().getTop() + "]");

		if (top instanceof UnoDrawCard && ((UnoDrawCard) top).isOpen()) {
			this.ps.println("0 - Draw " + game.getDiscard().getConsecutiveDraw() + " cards from a " + top);

		} else {
			this.ps.println("0 - Draw");
		}

		int i = 1;
		for (UnoCard card : this.getHand().getCards()) {
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
			String nextLine = this.scanner.nextLine();
			if (nextLine.equalsIgnoreCase("rules")) {
				this.ps.println("Active rules: " + game.getRules()
						.getRules()
						.stream()
						.map(r -> r.getClass().getSimpleName())
						.collect(Collectors.joining(", ")));
				continue;
			}

			int choice;
			try {
				choice = Integer.parseInt(nextLine);
			} catch (NumberFormatException e) {
				this.ps.println(INVALID_CHOICE_STRING);
				continue;
			}

			if (choice == 0)
				return null;

			if (choice > this.getCards().size()) {
				this.ps.println(INVALID_CHOICE_STRING);
				continue;
			}

			UnoCard card = this.getCards().get(choice - 1);

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
	public boolean shouldPlayDrawnCard(UnoGame game, UnoCard drawnCard, UnoPlayer next) {
		this.ps.println("You have drawn a " + drawnCard.toString() + ". Do you want to place it? [y/n]");
		return this.scanner.nextLine().equals("y");
	}

}
