package com.github.markozajc.juno.cards.impl;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.UnoCardColor;

import javax.annotation.Nonnull;

public class UnoReverseCard extends UnoCard {

    /**
     * Creates a new {@link UnoReverseCard}.
     * @param color
     *            the {@link UnoCardColor}
     */
    public UnoReverseCard(@Nonnull UnoCardColor color) {
        super(color);
    }

    @Override
    public String toString() {
        return this.getColor() + " reverse";
    }

    @Override
    public UnoCard cloneCard() {
        return new UnoReverseCard(getColor());
    }

}
