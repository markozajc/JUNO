package com.github.markozajc.juno.rules.pack;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.util.*;

import javax.annotation.*;

import com.github.markozajc.juno.rules.*;
import com.github.markozajc.juno.rules.UnoRule.ConflictResolution;

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
	 * Creates a new {@link UnoRulePack} from {@link UnoRule}s.
	 *
	 * @param rules
	 *            the {@link UnoRule}s
	 */
	@SuppressWarnings("null")
	public UnoRulePack(@Nonnull UnoRule... rules) {
		this(asList(rules));
	}

	/**
	 * Creates a new {@link UnoRulePack} from a {@link Collection} of
	 * {@link UnoRulePack}s
	 *
	 * @param packs
	 *            the {@link UnoRulePack}s
	 *
	 * @return the combined {@link UnoRulePack}
	 */
	@Nonnull
	@SuppressWarnings("null")
	public static UnoRulePack ofPacks(@Nonnull Collection<UnoRulePack> packs) {
		return new UnoRulePack(packs.stream().flatMap(p -> p.getRules().stream()).collect(toList()));
		// Lambda magic to flatten a list of UnoRulePack-s
	}

	/**
	 * Creates a new {@link UnoRulePack} from {@link UnoRulePack}s
	 *
	 * @param packs
	 *            the {@link UnoRulePack}s
	 *
	 * @return the combined {@link UnoRulePack}
	 */
	@SuppressWarnings("null")
	@Nonnull
	public static UnoRulePack ofPacks(@Nonnull UnoRulePack... packs) {
		return ofPacks(asList(packs));
	}

	/**
	 * @return this {@link UnoRulePack}'s rules
	 */
	@Nonnull
	public List<UnoRule> getRules() {
		return this.rules;
	}

	/**
	 * Creates a new {@link UnoRulePack} that includes this {@link UnoRulePack} and a
	 * {@link Collection} of additional {@link UnoRulePack}.
	 *
	 * @param packs
	 *            {@link Collection} of {@link UnoRulePack}s to include
	 *
	 * @return the combined {@link UnoRulePack}
	 */
	@CheckReturnValue
	@Nonnull
	public UnoRulePack addPacks(@Nonnull Collection<UnoRulePack> packs) {
		List<UnoRulePack> newPack = new ArrayList<>(packs.size() + 1);
		newPack.add(this);
		newPack.addAll(packs);
		return UnoRulePack.ofPacks(newPack);
	}

	/**
	 * Creates a new {@link UnoRulePack} that includes this {@link UnoRulePack} and
	 * additional {@link UnoRulePack}.
	 *
	 * @param packs
	 *            {@link UnoRulePack}s to include
	 *
	 * @return the combined {@link UnoRulePack}
	 */
	@Nonnull
	@CheckReturnValue
	@SuppressWarnings("null")
	public UnoRulePack addPacks(@Nonnull UnoRulePack... packs) {
		return this.addPacks(asList(packs));
	}

	/**
	 * Analyzes and resolves all {@link UnoRule} conflicts and returns the new list of
	 * {@link UnoRule}s in a new {@link UnoRulePack}.
	 *
	 * @return the {@link UnoRulePack} with resolved conflicts
	 *
	 * @throws UnoRuleConflictException
	 *             in case a {@link ConflictResolution#FAIL} is returned at any time
	 *
	 * @see UnoRule#conflictsWith(UnoRule)
	 */
	@CheckReturnValue
	@Nonnull
	public UnoRulePack resolveConflicts() throws UnoRuleConflictException {
		List<UnoRule> conflicting = new ArrayList<>();

		for (UnoRule rule : this.rules) {
			// Iterates over all rules

			for (UnoRule checkRule : this.rules) {
				// Iterates over all rules for each rule

				if (checkRule.equals(rule))
					continue;
				// Skips the same rule

				var conflict = rule.conflictsWith(checkRule);
				// Analyzes the conflicts

				if (conflict != null) {
					// Proceeds if conflicts have been found
					switch (conflict) {
						case FAIL:
							throw new UnoRuleConflictException(rule, checkRule);
						// Fails if there need be

						case BACKOFF:
							conflicting.add(rule);
							break;
						// Backs off the rule if requested

						case REPLACE:
							conflicting.add(checkRule);
							break;
						// Replaces the rule if requested
					}
				}
			}
		}

		List<UnoRule> newRules = new ArrayList<>(this.rules);
		newRules.removeAll(conflicting);
		// Removes all conflicting rules

		return new UnoRulePack(newRules);
	}

}
