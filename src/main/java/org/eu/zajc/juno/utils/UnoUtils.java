// SPDX-License-Identifier: GPL-3.0
/*
 * JUNO, the UNO library for Java 
 * Copyright (C) 2019-2023 Marko Zajc
 *
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this
 * program. If not, see <https://www.gnu.org/licenses/>.
 */
package org.eu.zajc.juno.utils;

import static java.util.Collections.sort;
import static java.util.stream.Collectors.toList;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

import org.eu.zajc.juno.cards.*;
import org.eu.zajc.juno.cards.impl.UnoNumericCard;

/**
 * A class containing various utilities, mostly used to process lists of
 * {@link UnoCard}s.
 *
 * @author Marko Zajc
 */
public class UnoUtils {

	private UnoUtils() {}

	/**
	 * Analyzes cards and returns them in an {@link Entry} of {@link UnoCardColor}
	 * (right) and quantity of cards of that color (left).
	 *
	 * @param cards
	 *            cards to analyze
	 *
	 * @return {@link Entry} of quantity and {@link UnoCardColor}
	 */
	public static List<Entry<Long, UnoCardColor>> analyzeColors(Collection<UnoCard> cards) {
		List<Entry<Long, UnoCardColor>> result = new ArrayList<>();

		for (var color : UnoCardColor.values())
			result.add(new SimpleEntry<>(cards.stream().filter(c -> c.getColor() == color).count(), color));

		sort(result, (v1, v2) -> v2.getKey().compareTo(v1.getKey()));
		return result;
	}

	/**
	 * Gets all cards of a certain color from a list of {@link UnoCard}s.
	 *
	 * @param <T>
	 *            card type
	 * @param color
	 *            {@link UnoCardColor} to search for
	 * @param cards
	 *            {@link Collection} of {@link UnoCard}s to search through
	 *
	 * @return {@link List} containing only cards of a certain color
	 */
	public static <T extends UnoCard> List<T> getColorCards(UnoCardColor color, Collection<T> cards) {
		return cards.stream().filter(c -> c.getColor() == color).collect(toList());
	}

	/**
	 * Gets all {@link UnoNumericCard} with a certain number from a list of
	 * {@link UnoCard}s.
	 *
	 * @param number
	 *            number to search for
	 * @param cards
	 *            {@link List} of {@link UnoCard}s to search through
	 *
	 * @return {@link List} containing only cards of a certain color
	 */
	public static List<UnoNumericCard> getNumberCards(int number, Collection<UnoCard> cards) {
		return filterKind(UnoNumericCard.class, cards).stream().filter(c -> c.getNumber() == number).collect(toList());
	}

	/**
	 * Gets all {@link UnoCard}s of a certain kind.
	 *
	 * @param <T>
	 *            the superclass of {@link UnoCard} to search for
	 * @param targetKind
	 *            the {@link Class} of {@link UnoCard} to search for
	 * @param cards
	 *            {@link Collection} of {@link UnoCard}s to search through
	 *
	 * @return {@link List} of cards of a certain kind
	 */
	public static <T extends UnoCard> List<T> filterKind(Class<T> targetKind, Collection<UnoCard> cards) {
		return cards.stream().filter(targetKind::isInstance).map(targetKind::cast).collect(toList());
	}

}
