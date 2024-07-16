package ltlf;

import java.util.*;

public class LTLState {
	private final Set<String> atoms;

	public static LTLState atoms(String ... atoms) {
		return new LTLState(normalize(atoms));
	}

	private static Set<String> normalize(String[] atoms) {
		Set<String> normalized = new LinkedHashSet<>();
		for (String fact : atoms) {
			normalized.add(fact.trim().toLowerCase());
		}
		return normalized;
	}

	private LTLState(Set<String> atoms) {
		this.atoms = atoms;
	}

	public boolean contains(String atom) {
		return atoms.contains(atom);
	}
}
