package com.github.markozajc.juno.hands;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.UnoCardColor;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.piles.UnoPile;
import com.github.markozajc.juno.piles.impl.UnoDrawPile;

public abstract class UnoHand implements UnoPile {

	@Nonnull
	protected final List<UnoCard> cards = new ArrayList<>();

	@Nonnull
	protected final String name;

	public UnoHand(@Nonnull String name) {
		this.name = name;
	}

	@Override
	public List<UnoCard> getCards() {
		return this.cards;
	}

	@Override
	public int getSize() {
		return this.cards.size();
	}

	@Nonnull
	public final List<UnoCard> draw(UnoDrawPile pile, int amount) {
		List<UnoCard> drawnCards = pile.draw(amount);
		this.cards.addAll(drawnCards);
		return drawnCards;
	}

	@Nonnull
	public final List<UnoCard> clear() {
		List<UnoCard> copy = new ArrayList<>(this.cards);

		this.cards.clear();

		return copy;
	}

	public abstract UnoCard playCard(UnoGame game, boolean drawn);

	@Nonnull
	public abstract UnoCardColor chooseColor(UnoGame game);

	@Nonnull
	public String getName() {
		return this.name;
	}

}
