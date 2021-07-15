package com.github.markozajc.juno.cards;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import com.github.markozajc.juno.TestUtils;
import com.github.markozajc.juno.cards.impl.*;
import com.github.markozajc.juno.players.UnoPlayer;

import static org.junit.jupiter.api.Assertions.*;

class UnoCardTest {

	@Test
	void testColor() {
		UnoCard wild = new UnoWildCard();
		// Creates a new wild-colored card

		wild.setColorMask(UnoCardColor.RED);
		// Set the color mask to red

		assertEquals(UnoCardColor.RED, wild.getColor());
		// Test the color mask

		assertEquals(UnoCardColor.WILD, wild.getOriginalColor());
		// Test the original color

		assertThrows(IllegalStateException.class, () -> wild.setColorMask(UnoCardColor.GREEN));
		// Test the multicall prevention

		///////////////////////////////////////////////////////////////////////////////////

		UnoCard nonWild = new UnoNumericCard(UnoCardColor.RED, 0);
		// Creates a new red-colored card

		assertThrows(IllegalStateException.class, () -> nonWild.setColorMask(UnoCardColor.GREEN));
		// Test the non-wild color mask block

		assertEquals(UnoCardColor.RED, nonWild.getColor());
		// Test if the color is still red
	}

	@SuppressWarnings("null")
	@Test
	void testPlacer() {
		UnoCard card = new UnoNumericCard(UnoCardColor.RED, 0);
		// Creates a new numeric card

		assertThrows(IllegalStateException.class, () -> card.getPlacer());
		// Test the no-placer getCall exception

		UnoPlayer placer = TestUtils.getDummyPlayer(Collections.emptyList());
		// Create a new dummy player

		card.setPlacer(placer);
		// Sets the card's placer

		assertEquals(placer, card.getPlacer());
		// Tests if the card's placer is right

		assertThrows(IllegalStateException.class, () -> card.setPlacer(placer));
		// Test the multicall prevention
	}

	@SuppressWarnings("null")
	@Test
	void testReset() {
		UnoCard card = new UnoWildCard();
		// Creates a new numeric card

		card.setPlacer(TestUtils.getDummyPlayer(Collections.emptyList()));
		card.setColorMask(UnoCardColor.GREEN);
		card.markOpen();
		// Sets the card's state

		card.reset();
		// Reset the card

		assertThrows(IllegalStateException.class, () -> card.getPlacer());
		assertEquals(UnoCardColor.WILD, card.getColor());
		assertFalse(card.isOpen());
		// Test the state

	}

}
