package ltlf;

import net.jqwik.api.*;

import static ltlf.LTLState.*;
import static ltlf.StateChecker.not;
import static ltlf.StateChecker.*;
import static org.assertj.core.api.Assertions.*;

class StateCheckingTests {

	@Example
	void notOperator() {
		StateChecker notA = not(fact("a"));
		assertThat(notA.check(atoms("b", "c"))).isTrue();
		assertThat(notA.check(atoms("b", "a"))).isFalse();
	}

	@Example
	void and() {
		StateChecker aAndB = fact("a").and(fact("b"));
		assertThat(aAndB.check(atoms("b", "a"))).isTrue();
		assertThat(aAndB.check(atoms("b", "x"))).isFalse();
	}

	@Example
	void or() {
		StateChecker aOrB = fact("a").or(fact("b"));
		assertThat(aOrB.check(atoms("b", "x"))).isTrue();
		assertThat(aOrB.check(atoms("y", "x"))).isFalse();
	}

	@Example
	void implies() {
		StateChecker aOrB = fact("a").implies(fact("b"));
		assertThat(aOrB.check(atoms("c", "x"))).isTrue();
		assertThat(aOrB.check(atoms("a", "x", "b"))).isTrue();
		assertThat(aOrB.check(atoms("a"))).isFalse();
	}

}
