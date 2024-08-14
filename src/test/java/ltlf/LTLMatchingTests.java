package ltlf;

import java.util.*;

import net.jqwik.api.*;

import static ltlf.LTLFormula.*;
import static ltlf.LTLState.*;
import static org.assertj.core.api.Assertions.*;

class LTLMatchingTests {

	@Example
	void anyTraceMatchesNoFormulae() {
		LTL<Set<String>> ltl = new LTL<>();

		assertThat(ltl.matches(
			atoms("a", "b", "c"),
			atoms("a", "b"),
			atoms("a")
		)).isTrue();

		assertThat(ltl.matches()).isTrue();
	}

	@Example
	void alwaysMatches() {
		LTL<Set<String>> ltl = new LTL<>();
		ltl.addFormula(
			always(AtomicFact.fact("a"))
		);

		assertThat(ltl.matches(atoms("a"), atoms("a", "b"))).isTrue();
		assertThat(ltl.matches()).isTrue();
	}

	@Example
	void alwaysDoesNotMatch() {
		LTL<Set<String>> ltl = new LTL<>();
		ltl.addFormula(
			always(AtomicFact.fact("b"))
		);

		boolean matches = ltl.matches(atoms("a", "b", "c"), atoms("a"));
		assertThat(matches).isFalse();
	}

	@Example
	void matchSeveralFormulae() {
		LTL<Set<String>> ltl = new LTL<>();
		ltl.addFormula(always(AtomicFact.fact("a")));
		ltl.addFormula(AtomicFact.fact("b"));
		ltl.addFormula(eventually(AtomicFact.fact("c")));

		assertThat(ltl.matches(
			atoms("a", "b"),
			atoms("a"),
			atoms("a"),
			atoms("c", "a"),
			atoms("a")
		)).isTrue();

		assertThat(ltl.matches(
			atoms("a"),
			atoms("a"),
			atoms("a"),
			atoms("c", "a"),
			atoms("a")
		)).isFalse();

		assertThat(ltl.matches(
			atoms("b"),
			atoms("a"),
			atoms("a"),
			atoms("c", "a"),
			atoms("a")
		)).isFalse();

		assertThat(ltl.matches(
			atoms("a", "b"),
			atoms("a"),
			atoms("a"),
			atoms("a"),
			atoms("a")
		)).isFalse();
	}

}
