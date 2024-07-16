package ltlf;

import net.jqwik.api.*;

import static ltlf.StateChecker.*;
import static ltlf.LTLFormula.*;
import static ltlf.LTLState.*;
import static org.assertj.core.api.Assertions.*;

class LTLTests {

	@Example
	void matchEmptyTrace() {
		LTL ltl = new LTL();
		boolean matches = ltl.matches();

		assertThat(matches).isTrue();
	}

	@Example
	void matchNoFormulae() {
		LTL ltl = new LTL();

		boolean matches = ltl.matches(
			atoms("a", "b", "c"),
			atoms("a", "b"),
			atoms("a")
		);

		assertThat(matches).isTrue();
	}

	@Example
	void alwaysMatches() {
		LTL ltl = new LTL();
		ltl.addFormula(
			always(fact("a"))
		);

		boolean matches = ltl.matches(
			atoms("a", "b", "c"),
			atoms("a", "b"),
			atoms("a")
		);

		assertThat(matches).isTrue();
	}

	@Example
	void notChecker() {
		StateChecker notA = not(fact("a"));
		assertThat(notA.check(atoms("b", "c"))).isTrue();
		assertThat(notA.check(atoms("b", "a"))).isFalse();
	}

	@Example
	void andChecker() {
		StateChecker aAndB = fact("a").and(fact("b"));
		assertThat(aAndB.check(atoms("b", "a"))).isTrue();
		assertThat(aAndB.check(atoms("b", "x"))).isFalse();
	}

	@Example
	void orChecker() {
		StateChecker aOrB = fact("a").or(fact("b"));
		assertThat(aOrB.check(atoms("b", "x"))).isTrue();
		assertThat(aOrB.check(atoms("y", "x"))).isFalse();
	}

	@Example
	void alwaysDoesNotMatch() {
		LTL ltl = new LTL();
		ltl.addFormula(
			always(fact("b"))
		);

		boolean matches = ltl.matches(
			atoms("a", "b", "c"),
			atoms("a", "b"),
			atoms("a")
		);

		assertThat(matches).isFalse();
	}

}
