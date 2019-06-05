package com.github.markozajc.juno.rules.pack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.github.markozajc.juno.rules.UnoRule;

/**
 * A pack of {@link UnoRule}s. Multiple {@link UnoRulePack}s can be combined into one
 * using {@link #ofPacks(Collection)}.
 *
 * @author Marko Zajc
 */
public class UnoRulePack {

	@Nonnull
	private final List<UnoRule> rules;

	/**
	 * Creates a new {@link UnoRulePack} from a {@link Collection} of {@link UnoRule}s.
	 *
	 * @param rules
	 *            the {@link UnoRule}s
	 */
	public UnoRulePack(@Nonnull Collection<UnoRule> rules) {
		this.rules = new ArrayList<>(rules);
	}

	/**
	 * Creates a new {@link UnoRulePack} from a vararg of {@link UnoRule}s.
	 *
	 * @param rules
	 *            the {@link UnoRule}s
	 */
	@SuppressWarnings("null")
	public UnoRulePack(@Nonnull UnoRule... rules) {
		this(Arrays.asList(rules));
	}

	/**
	 * Creates a new {@link UnoRulePack} from a {@link Collection} of
	 * {@link UnoRulePack}s
	 *
	 * @param packs
	 *            the {@link UnoRulePack}s
	 * @return the combined {@link UnoRulePack}
	 */
	@SuppressWarnings("null")
	@Nonnull
	public static UnoRulePack ofPacks(@Nonnull Collection<UnoRulePack> packs) {
		return new UnoRulePack(packs.stream().flatMap(p -> p.getRules().stream()).collect(Collectors.toList()));
		// Lambda magic to flatten a list of UnoRulePack-s
	}

	/**
	 * Creates a new {@link UnoRulePack} from a vararg of {@link UnoRulePack}s
	 *
	 * @param packs
	 *            the {@link UnoRulePack}s
	 * @return the combined {@link UnoRulePack}
	 */
	@SuppressWarnings("null")
	@Nonnull
	public static UnoRulePack ofPacks(@Nonnull UnoRulePack... packs) {
		return ofPacks(Arrays.asList(packs));
	}

	/**
	 * @return this {@link UnoRulePack}'s rules
	 */
	@Nonnull
	public List<UnoRule> getRules() {
		return this.rules;
	}

	public void addPacks(@Nonnull Collection<UnoRulePack> packs) {
		this.rules.addAll(packs.stream().flatMap(p -> p.getRules().stream()).collect(Collectors.toList()));
	}

	@SuppressWarnings("null")
	public void addPacks(@Nonnull UnoRulePack... packs) {
		this.addPacks(Arrays.asList(packs));
	}

}
