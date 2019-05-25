package com.github.markozajc.juno.game;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.UnoCardColor;
import com.github.markozajc.juno.cards.impl.UnoActionCard;
import com.github.markozajc.juno.cards.impl.UnoDrawCard;
import com.github.markozajc.juno.cards.impl.UnoWildCard;
import com.github.markozajc.juno.hands.UnoHand;
import com.github.markozajc.juno.utils.UnoUtils;

public abstract class BasicUnoGame extends UnoGame {

	@SuppressWarnings("unused")
	protected void onCardPlaced(UnoHand hand, UnoCard card) {}

	@SuppressWarnings("unused")
	protected void onColorChanged(UnoHand hand, UnoCardColor newColor) {}

	protected void onPileShuffle() {}

	@SuppressWarnings("unused")
	protected void onDrawCards(UnoHand hand, int quantity) {}

	@SuppressWarnings("unused")
	protected void onInvalidCard(UnoHand hand) {}

	/**
	 * Executed when an illegal color request has been made. This means that a hand has
	 * returned {@link UnoCardColor#WILD} on {@link UnoHand#chooseColor(UnoGame)}.
	 *
	 * @param hand
	 *            the offending {@link UnoHand}
	 * @param color
	 *            the invalid {@link UnoCardColor}
	 */
	@SuppressWarnings("unused")
	protected void onInvalidColor(UnoHand hand, UnoCardColor color) {}

	public BasicUnoGame(@Nonnull UnoHand playerOneHand, @Nonnull UnoHand playerTwoHand) {
		super(playerOneHand, playerTwoHand);
	}

	private void changeColor(UnoHand hand, UnoCard card) {
		if (!card.getColor().equals(UnoCardColor.WILD))
			return;

		UnoCardColor color = hand.chooseColor(this);
		while (color == UnoCardColor.WILD) {
			onInvalidColor(hand, color);
			color = hand.chooseColor(this);
		}
		onColorChanged(hand, color);

		if (card instanceof UnoWildCard)
			((UnoWildCard) card).setColor(color);

		if (card instanceof UnoDrawCard)
			((UnoDrawCard) card).setColor(color);
	}

	@Override
	protected void playHand(UnoHand hand) {
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

				if (UnoUtils.analyzePossibleCards(topCard, Arrays.asList(card)).isEmpty()
						|| !hand.getCards().remove(card)) {
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
