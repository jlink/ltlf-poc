package ltlf;

import java.util.*;

/**
 * A Linear Temporal Logic (LTL) system with finite semantics.
 * See https://www.cs.rice.edu/~vardi/papers/ijcai13.pdf
 *
 * <p>
 *     Finite semantics means that only finite traces are considered.
 *     - `Always` holds when it holds in all states of the trace.
 *     - `Eventually` holds when it holds in at least one state of the trace.
 *     - `Next` does not require that a next state even exists.
 *     - `Until` does not require that the second formula ever holds.
 *     - `Last` does require that the last state exists.
 * </p>
 */
public class LTL {

	private final Set<LTLFormula> formulae = new LinkedHashSet<>();

	public boolean matches(LTLState... trace) {
		return matches(new LTLTrace(Arrays.asList(trace)));
	}

	public boolean matches(LTLTrace trace) {
		return formulae.stream().allMatch(f -> f.check(trace));
	}

	public void addFormula(LTLFormula formula) {
		formulae.add(formula);
	}
}
