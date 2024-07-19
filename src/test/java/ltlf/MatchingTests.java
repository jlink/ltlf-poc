package ltlf;

import net.jqwik.api.*;

import static ltlf.StateChecker.*;
import static ltlf.LTLFormula.*;
import static ltlf.LTLState.*;
import static org.assertj.core.api.Assertions.*;

class MatchingTests {

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
