package ltlf;

import java.util.*;

public class LTL {

	private final Set<LTLFormula> formulae = new LinkedHashSet<>();

	public boolean matches(LTLState... trace) {
		return matches(new LTLTrace(Arrays.asList(trace)));
	}

	public boolean matches(LTLTrace trace) {
		return formulae.stream().allMatch(f -> f.matches(trace));
	}

	public void addFormula(LTLFormula formula) {
		formulae.add(formula);
	}
}
