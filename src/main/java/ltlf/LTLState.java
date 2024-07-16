package ltlf;

import java.util.*;

public class LTLState {
	private final List<String> facts;

	public static LTLState facts(String ... facts) {
		return new LTLState(normalize(facts));
	}

	private static List<String> normalize(String[] facts) {
		List<String> normalized = new ArrayList<>();
		for (String fact : facts) {
			normalized.add(fact.trim().toLowerCase());
		}
		return normalized;
	}

	private LTLState(List<String> facts) {
		this.facts = facts;
	}
}
