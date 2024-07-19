package ltlf;

import net.jqwik.api.*;

import static ltlf.LTLFormula.*;
import static ltlf.LTLState.*;
import static org.assertj.core.api.Assertions.*;

@Group
class TraceCheckingTests {

	@Example
	void notOperator() {
		LTLFormula notA = not(fact("a"));
		assertThat(notA.check(atoms("b", "c"))).isTrue();
		assertThat(notA.check(atoms("b", "a"))).isFalse();
	}

	@Example
	void and() {
		LTLFormula aAndB = fact("a").and(fact("b"));
		assertThat(aAndB.check(atoms("b", "a"))).isTrue();
		assertThat(aAndB.check(atoms("b", "x"))).isFalse();
	}

	@Example
	void or() {
		LTLFormula aOrB = fact("a").or(fact("b"));
		assertThat(aOrB.check(atoms("b", "x"))).isTrue();
		assertThat(aOrB.check(atoms("y", "x"))).isFalse();
	}

	@Example
	void implies() {
		LTLFormula aOrB = fact("a").implies(fact("b"));
		assertThat(aOrB.check(atoms("c", "x"))).isTrue();
		assertThat(aOrB.check(atoms("a", "x", "b"))).isTrue();
		assertThat(aOrB.check(atoms("a"))).isFalse();
	}

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
	class Eventually {

		@Example
		void matches() {
			var eventually = eventually(fact("a"));

			assertThat(eventually.check(LTLTrace.of(
				atoms("c"),
				atoms("a", "b"),
				atoms("b")
			))).isTrue();

			assertThat(eventually.check(LTLTrace.of(
				atoms("c"),
				atoms("c"),
				atoms(),
				atoms("c"),
				atoms(),
				atoms("a", "b")
			))).isTrue();
		}

		@Example
		void doesNotMatch() {
			var eventually = eventually(fact("a"));

			assertThat(eventually.check(LTLTrace.of(
				atoms("c"),
				atoms("b"),
				atoms(),
				atoms("b")
			))).isFalse();

			assertThat(eventually.check(LTLTrace.of())).isFalse();
		}

		@Example
		void nested() {
			var eventually = eventually(always(fact("a")));

			assertThat(eventually.check(LTLTrace.of(
				atoms("c"),
				atoms(),
				atoms("a", "b"),
				atoms("a")
			))).isTrue();

			assertThat(eventually.check(LTLTrace.of(
				atoms("c"),
				atoms("a", "b"),
				atoms("a"),
				atoms()
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
