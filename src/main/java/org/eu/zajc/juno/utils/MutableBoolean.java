// SPDX-License-Identifier: GPL-3.0
/*
 * JUNO, the UNO library for Java
 * Copyright (C) 2019-2024 Marko Zajc
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

/**
 * A class thats records a boolean state for use in lambdas.
 *
 * @author Marko Zajc
 */
public class MutableBoolean {

	private boolean value;

	/**
	 * Sets the value
	 *
	 * @param value
	 *            the new value
	 */
	public void set(boolean value) {
		this.value = value;
	}

	/**
	 * @return the current value
	 */
	public boolean get() {
		return this.value;
	}

}
