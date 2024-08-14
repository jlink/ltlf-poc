package ltlf;

import java.util.*;

public record LTLState<S>(S state) {

	public static LTLState<Set<String>> atoms(String ... atoms) {
		return new LTLState<>(normalize(atoms));
	}

	private static Set<String> normalize(String[] atoms) {
		Set<String> normalized = new LinkedHashSet<>();
		for (String fact : atoms) {
			normalized.add(fact.trim().toLowerCase());
		}
		return normalized;
	}

	@Override
	public String toString() {
		return state.toString();
	}
}
