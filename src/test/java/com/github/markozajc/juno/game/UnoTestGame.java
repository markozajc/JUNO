package com.github.markozajc.juno.game;

import com.github.markozajc.juno.decks.UnoDeck;
import com.github.markozajc.juno.decks.impl.UnoStandardDeck;
import com.github.markozajc.juno.players.UnoPlayer;
import com.github.markozajc.juno.rules.pack.UnoRulePack;
import com.github.markozajc.juno.rules.pack.impl.UnoOfficialRules;

import javax.annotation.Nonnull;

import static java.lang.System.out;

public class UnoTestGame extends UnoControlledGame {
    public UnoTestGame(UnoPlayer... players) {
        this(UnoStandardDeck.getDeck(), 7, UnoOfficialRules.getPack(), players);
    }

    public UnoTestGame(@Nonnull UnoDeck unoDeck, int cardAmount, @Nonnull UnoRulePack rules, UnoPlayer... players) {
        super(unoDeck, cardAmount, rules, players);
    }

    @Override
    public void onEvent(String format, Object... arguments) {
        out.printf(format + "%n", arguments);
    }
}
