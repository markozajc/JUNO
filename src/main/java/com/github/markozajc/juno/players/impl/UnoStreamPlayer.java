package com.github.markozajc.juno.players.impl;

import static com.github.markozajc.juno.cards.UnoCardColor.*;
import static com.github.markozajc.juno.rules.pack.impl.house.UnoProgressiveRulePack.getConsecutive;
import static com.github.markozajc.juno.utils.UnoRuleUtils.combinedPlacementAnalysis;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static java.lang.Thread.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.joining;

import java.io.*;
import java.util.Scanner;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

import com.github.markozajc.juno.cards.*;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.players.UnoPlayer;

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
	 * @param name
	 *            this player's name
	 * @param is
	 *            {@link InputStream} to read from
	 * @param ps
	 *            {@link PrintStream} to write to
	 */
	public UnoStreamPlayer(@Nonnull String name, @Nonnull InputStream is, @Nonnull PrintStream ps) {
		super(name);
		this.scanner = new Scanner(is, UTF_8);
		this.ps = ps;
	}

	@Override
	@SuppressWarnings("null")
	public UnoCard playCard(UnoGame game) {
		UnoCard top = game.getDiscard().getTop();
		var possible = combinedPlacementAnalysis(top, this.getHand().getCards(), game.getRules(), this.getHand());

		var handSizes = game.getPlayers()
			.stream()
			.filter(Predicate.not(Predicate.isEqual(this)))
			.map(p -> format("%s hand size: %d | ", p.getName(), p.getHand().getSize()))
			.collect(joining());

		this.ps.printf("Choose a card: [%sDraw pile size: %d | Discard pile size: %d | Top card: %s]%n", handSizes,
					   game.getDraw().getSize(), game.getDiscard().getSize(), game.getDiscard().getTop());

		var drawCards = getConsecutive(game.getDiscard());
		if (!drawCards.isEmpty()) {
			this.ps.printf("0 - Draw %d cards from %d %s%s%n", drawCards.size() * drawCards.get(0).getAmount(),
						   drawCards.size(), top, drawCards.size() == 1 ? "" : "s");

		} else {
			this.ps.println("0 \u2022 Draw");
		}

		int i = 1;
		for (var card : this.getHand().getCards()) {
			if (possible.contains(card))
				this.ps.printf("%d \u2022 %s%n", i, card);
			else
				this.ps.printf("%d - %s%n", i, card);

			try {
				sleep(5);
			} catch (InterruptedException e) {
				currentThread().interrupt();
			}

			i++;
		}
		this.ps.println("q \u2022 Quit");

		while (true) {
			String nextLine = this.scanner.nextLine();
			if ("rules".equalsIgnoreCase(nextLine)) {
				this.ps.printf("Active rules: %s%n",
							   game.getRules()
								   .getRules()
								   .stream()
								   .map(r -> r.getClass().getSimpleName())
								   .collect(joining(", ")));
				continue;
			}

			if ("q".equalsIgnoreCase(nextLine)) {
				game.endGame();
				return null;
			}

			int choice;
			try {
				choice = parseInt(nextLine);
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
		this.ps.println("0 \u2022 Yellow");
		this.ps.println("1 \u2022 Red");
		this.ps.println("2 \u2022 Green");
		this.ps.println("3 \u2022 Blue");

		while (true) {
			int choice;
			try {
				choice = parseInt(this.scanner.nextLine());
			} catch (NumberFormatException e) {
				this.ps.println(INVALID_CHOICE_STRING);
				continue;
			}

			switch (choice) {
				case 0:
					return YELLOW;
				case 1:
					return RED;
				case 2:
					return GREEN;
				case 3:
					return BLUE;
				default:
					break;
			}

			this.ps.println(INVALID_CHOICE_STRING);
		}
	}

	@Override
	public boolean shouldPlayDrawnCard(UnoGame game, UnoCard drawnCard, UnoPlayer next) {
		this.ps.printf("You have drawn a %s. Do you want to place it? [y/N]%n", drawnCard);
		return "y".equalsIgnoreCase(this.scanner.nextLine());
	}

}
