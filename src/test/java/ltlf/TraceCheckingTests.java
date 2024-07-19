package ltlf;

import net.jqwik.api.*;

import static ltlf.LTLFormula.*;
import static ltlf.LTLState.*;
import static ltlf.StateChecker.*;
import static org.assertj.core.api.Assertions.*;

class TraceCheckingTests {

	@Group
	class Always {

		@Example
		void matches() {
			var always = always(fact("a"));
			boolean matches = always.check(LTLTrace.of(
				atoms("a", "b", "c"),
				atoms("a", "b"),
				atoms("a")
			));

			assertThat(matches).isTrue();
		}

		@Example
		void doesNotMatch() {
			var always = always(fact("b"));
			boolean matches = always.check(LTLTrace.of(
				atoms("a", "b", "c"),
				atoms("a", "b"),
				atoms("a")
			));

			assertThat(matches).isFalse();
		}

		@Example
		void nested() {
			var always = always(always(fact("a")));
			boolean matches = always.check(LTLTrace.of(
				atoms("a", "b", "c"),
				atoms("a", "b"),
				atoms("a")
			));

			assertThat(matches).isTrue();
		}

	}

}
