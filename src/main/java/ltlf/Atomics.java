package ltlf;

import java.util.*;

/**
 * Represents a system composed of atomic propositions.
 * One atomic proposition is a fact that can be either true or false.
 * Atomic propositions - aka Atoms - are represented by a String which is present in the set or not.
 */
public class Atomics {

	public static LTLFormula<Set<String>> fact(String atom) {
		return new AtomicFact(atom);
	}

	public static LTLState<Set<String>> state(String ... atoms) {
		return new LTLState<>(normalize(atoms));
	}

	private static Set<String> normalize(String[] atoms) {
		Set<String> normalized = new LinkedHashSet<>();
		for (String fact : atoms) {
			normalized.add(fact.trim().toLowerCase());
		}
		return normalized;
	}

	public record AtomicFact(String atom) implements LTLFormula.Fact<Set<String>> {

		public AtomicFact(String atom) {
			this.atom = atom.trim().toLowerCase();
		}

		@Override
		public boolean check(LTLState<Set<String>> state) {
			return state.state().contains(atom);
		}
	}
}
