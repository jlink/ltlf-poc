package ltlf;

import java.util.*;

import net.jqwik.api.*;

import static ltlf.LTLFormula.*;
import static org.assertj.core.api.Assertions.*;

class LTLMatchingTests {

	@Example
	void anyTraceMatchesNoFormulae() {
		LTL<Set<String>> ltl = new LTL<>();

		assertThat(ltl.matches(
			Atomics.state("a", "b", "c"),
			Atomics.state("a", "b"),
			Atomics.state("a")
		)).isTrue();

		assertThat(ltl.matches()).isTrue();
	}

	@Example
	void alwaysMatches() {
		LTL<Set<String>> ltl = new LTL<>();
		ltl.addFormula(
			always(Atomics.fact("a"))
		);

		assertThat(ltl.matches(Atomics.state("a"), Atomics.state("a", "b"))).isTrue();
		assertThat(ltl.matches()).isTrue();
	}

	@Example
	void alwaysDoesNotMatch() {
		LTL<Set<String>> ltl = new LTL<>();
		ltl.addFormula(
			always(Atomics.fact("b"))
		);

		boolean matches = ltl.matches(Atomics.state("a", "b", "c"), Atomics.state("a"));
		assertThat(matches).isFalse();
	}

	@Example
	void matchSeveralFormulae() {
		LTL<Set<String>> ltl = new LTL<>();
		ltl.addFormula(always(Atomics.fact("a")));
		ltl.addFormula(Atomics.fact("b"));
		ltl.addFormula(eventually(Atomics.fact("c")));

		assertThat(ltl.matches(
			Atomics.state("a", "b"),
			Atomics.state("a"),
			Atomics.state("a"),
			Atomics.state("c", "a"),
			Atomics.state("a")
		)).isTrue();

		assertThat(ltl.matches(
			Atomics.state("a"),
			Atomics.state("a"),
			Atomics.state("a"),
			Atomics.state("c", "a"),
			Atomics.state("a")
		)).isFalse();

		assertThat(ltl.matches(
			Atomics.state("b"),
			Atomics.state("a"),
			Atomics.state("a"),
			Atomics.state("c", "a"),
			Atomics.state("a")
		)).isFalse();

		assertThat(ltl.matches(
			Atomics.state("a", "b"),
			Atomics.state("a"),
			Atomics.state("a"),
			Atomics.state("a"),
			Atomics.state("a")
		)).isFalse();
	}

}
