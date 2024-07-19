package ltlf;

import net.jqwik.api.*;

import static ltlf.LTLFormula.*;
import static ltlf.LTLState.*;
import static ltlf.StateChecker.*;
import static org.assertj.core.api.Assertions.*;

@Group
class TraceCheckingTests {

	@Group
	class Next {

		@Example
		void matches() {
			var next = next(fact("a"));
			boolean matches = next.check(LTLTrace.of(
				atoms("c"),
				atoms("a", "b"),
				atoms("b")
			));

			assertThat(matches).isTrue();
		}

		@Example
		void doesNotMatch() {
			var next = next(fact("b"));
			boolean matches = next.check(LTLTrace.of(
				atoms("a", "b", "c"),
				atoms("a"),
				atoms("a", "b")
			));

			assertThat(matches).isFalse();
		}

		@Example
		void withNesting() {
			var next = next(always(fact("a")));

			assertThat(next.check(LTLTrace.of(
				atoms("c"),
				atoms("a", "b"),
				atoms("a")
			))).isTrue();

			assertThat(next.check(LTLTrace.of(
				atoms("c"),
				atoms("a", "b"),
				atoms("b")
			))).isFalse();
		}

	}

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
