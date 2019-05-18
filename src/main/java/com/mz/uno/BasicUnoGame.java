package com.mz.uno;

import java.util.Arrays;
import java.util.List;

import com.mz.uno.cards.UnoCard;
import com.mz.uno.cards.UnoCardColor;
import com.mz.uno.cards.impl.UnoActionCard;
import com.mz.uno.cards.impl.UnoDrawCard;
import com.mz.uno.cards.impl.UnoWildCard;
import com.mz.uno.hands.Hand;

public abstract class BasicUnoGame extends UnoGame {

	protected abstract void onColorChanged(Hand hand, UnoCardColor newColor);

	protected abstract void onPileShuffle();

	protected abstract void onDrawCards(Hand hand, int quantity);

	protected abstract void onInvalidCard(Hand hand);

	protected abstract void onCardPlaced(Hand hand, UnoCard card);

	public BasicUnoGame(Hand playerOneHand, Hand playerTwoHand) {
		super(playerOneHand, playerTwoHand);
	}

	private void changeColor(Hand hand, UnoCard card) {
		if (!card.getColor().equals(UnoCardColor.WILD))
			return;

		UnoCardColor color = hand.chooseColor(this);
		onColorChanged(hand, color);

		if (card instanceof UnoWildCard)
			((UnoWildCard) card).setColor(color);

		if (card instanceof UnoDrawCard)
			((UnoDrawCard) card).setColor(color);
	}

	@Override
	protected void playHand(Hand hand) {
		start: while (true) {
			UnoCard topCard = this.discard.getTop();
			boolean hasToDraw = topCard instanceof UnoDrawCard && !((UnoDrawCard) topCard).isPlayed();

			if (!hasToDraw)
				changeColor(hand, topCard);

			boolean drawn = false;
			while (true) {
				UnoCard card = hand.playCard(this, drawn);

				if (card == null && !drawn) {
					// In case hand throws nothing

					int draw = 1;
					if (hasToDraw) {
						draw = this.discard.getConsecutiveDraw();
						this.discard.markTopPlayed();
					}

					if (this.draw.getSize() < draw) {
						if (this.discard.getSize() < draw /* minimum draw requirement */ + 1 /* the top card */)
							return;

						onPileShuffle();
						discardIntoDraw();
					}

					List<UnoCard> drawnCards = hand.draw(this.draw, draw);
					onDrawCards(hand, draw);
					if (drawnCards.size() == 1 && UnoUtils.analyzePossibleCards(topCard, drawnCards).size() == 1) {
						drawn = true;
						continue;
					}

					return;
				} else if (card == null && drawn) {
					return;
				}

				if (UnoUtils.analyzePossibleCards(topCard, Arrays.asList(card)).isEmpty() || !hand.getCards().remove(card)) {
					// Should be checked by Hand implementation in the first place
					onInvalidCard(hand);
					continue;
				}
				// Checks if the card is valid and exists

				onCardPlaced(hand, card);

				if (card instanceof UnoWildCard)
					changeColor(hand, card);

				this.discard.add(card);

				if (card instanceof UnoActionCard && hand.getSize() > 0)
					continue start;

				break;
			}

			break;
		}
	}

}
