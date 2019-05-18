package com.mz.uno.piles;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import com.mz.uno.cards.UnoCard;

public class UnoDrawPile implements UnoPile {

	private final Queue<UnoCard> cards = new ArrayDeque<>(UnoStandardDeck.getExpectedSize());

	public UnoDrawPile(List<UnoCard> cards) {
		this.cards.forEach(UnoCard::reset);
		// Resets all card states

		this.cards.addAll(cards);
		shuffle();
		// Shuffles the cards
	}

	@Override
	public List<UnoCard> getCards() {
		return this.cards.stream().collect(Collectors.toList());
	}

	@Override
	public int getSize() {
		return this.cards.size();
	}

	public void merge(UnoDrawPile pile) {
		this.cards.addAll(pile.cards);
		this.cards.forEach(UnoCard::reset);
		shuffle();
	}

	public List<UnoCard> draw(int amount) {
		List<UnoCard> result = new ArrayList<>(amount);
		for (int i = 0; i < amount; i++) {
			result.add(this.cards.poll());
		}

		return Collections.unmodifiableList(result);
	}

	public UnoCard draw() {
		return this.cards.poll();
	}

	public void shuffle() {
		List<UnoCard> cardsCopy = new ArrayList<>(getCards());
		Collections.shuffle(cardsCopy);
		this.cards.removeIf(c -> true);
		this.cards.addAll(cardsCopy);
	}

}
