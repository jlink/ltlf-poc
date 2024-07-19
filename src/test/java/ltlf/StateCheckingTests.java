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
		assertThat(notA.checkState(atoms("b", "c"))).isTrue();
		assertThat(notA.checkState(atoms("b", "a"))).isFalse();
	}

	@Example
	void and() {
		StateChecker aAndB = fact("a").and(fact("b"));
		assertThat(aAndB.checkState(atoms("b", "a"))).isTrue();
		assertThat(aAndB.checkState(atoms("b", "x"))).isFalse();
	}

	@Example
	void or() {
		StateChecker aOrB = fact("a").or(fact("b"));
		assertThat(aOrB.checkState(atoms("b", "x"))).isTrue();
		assertThat(aOrB.checkState(atoms("y", "x"))).isFalse();
	}

	@Example
	void implies() {
		StateChecker aOrB = fact("a").implies(fact("b"));
		assertThat(aOrB.checkState(atoms("c", "x"))).isTrue();
		assertThat(aOrB.checkState(atoms("a", "x", "b"))).isTrue();
		assertThat(aOrB.checkState(atoms("a"))).isFalse();
	}

}
