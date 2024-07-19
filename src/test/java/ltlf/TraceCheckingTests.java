package ltlf;

import net.jqwik.api.*;

import static ltlf.LTLState.*;
import static ltlf.StateChecker.*;
import static ltlf.LTLFormula.*;
import static org.assertj.core.api.Assertions.*;

class TraceCheckingTests {

	@Example
	void alwaysChecker() {
		var always = always(fact("a"));
		boolean matches = always.check(LTLTrace.of(
			atoms("a", "b", "c"),
			atoms("a", "b"),
			atoms("a")
		));

		assertThat(matches).isTrue();
	}

	@Example
	void alwaysDoesNotMatch() {
		var always = always(fact("b"));
		boolean matches = always.check(LTLTrace.of(
			atoms("a", "b", "c"),
			atoms("a", "b"),
			atoms("a")
		));

		assertThat(matches).isFalse();
	}

}
