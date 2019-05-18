package com.mz.uno.hands;

import java.util.ArrayList;
import java.util.List;

import com.mz.uno.UnoGame;
import com.mz.uno.cards.UnoCard;
import com.mz.uno.cards.UnoCardColor;
import com.mz.uno.piles.UnoDrawPile;
import com.mz.uno.piles.UnoPile;

public abstract class Hand implements UnoPile {

	protected final List<UnoCard> cards = new ArrayList<>();

	protected final String name;

	public Hand(String name) {
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

	public final List<UnoCard> draw(UnoDrawPile pile, int amount) {
		List<UnoCard> drawnCards = pile.draw(amount);
		this.cards.addAll(drawnCards);
		return drawnCards;
	}

	public final List<UnoCard> clear() {
		List<UnoCard> copy = new ArrayList<>(this.cards);

		this.cards.clear();

		return copy;
	}

	public abstract UnoCard playCard(UnoGame game, boolean drawn);

	public abstract UnoCardColor chooseColor(UnoGame game);

	public String getName() {
		return this.name;
	}

}
