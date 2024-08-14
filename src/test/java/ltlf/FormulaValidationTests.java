package ltlf;

import java.util.*;

import net.jqwik.api.*;

import static ltlf.LTLFormula.not;
import static ltlf.LTLFormula.*;
import static org.assertj.core.api.Assertions.*;

@Group
class FormulaValidationTests {

	@Example
	void facts() {
		assertThat(Atomics.fact("a").validate(Atomics.state("a"))).isTrue();
		assertThat(Atomics.fact("a").validate(Atomics.state("x", "a", "y"))).isTrue();
		assertThat(Atomics.fact("a").validate(Atomics.state("x", "y"))).isFalse();
		assertThat(Atomics.fact("a").validate(Atomics.state())).isFalse();
		assertThat(Atomics.fact("a").validate(LTLTrace.of())).isFalse();
	}

	@Example
	void lastFormula() {
		assertThat(last(Atomics.fact("a")).validate(LTLTrace.of(
			Atomics.state("a"),
			Atomics.state("a")
		))).isTrue();
		assertThat(last(Atomics.fact("a")).validate(LTLTrace.of(
			Atomics.state("a"),
			Atomics.state("b")
		))).isFalse();
		assertThat(last(Atomics.fact("a")).validate(LTLTrace.of())).isFalse();
	}

	@Group
	class Until {

		@Example
		void holds() {
			LTLFormula<Set<String>> aUntilB = Atomics.fact("a").until(Atomics.fact("b"));

			assertThat(aUntilB.validate(LTLTrace.of(
				Atomics.state("a"),
				Atomics.state("a"),
				Atomics.state("a"),
				Atomics.state("a"),
				Atomics.state("b")
			))).isTrue();

			assertThat(aUntilB.validate(LTLTrace.of(
				Atomics.state("a"),
				Atomics.state("a"),
				Atomics.state("a"),
				Atomics.state("a"),
				Atomics.state("b"),
				Atomics.state("x")
			))).isTrue();

			assertThat(aUntilB.validate(LTLTrace.of(
				Atomics.state("a"),
				Atomics.state("a"),
				Atomics.state("a"),
				Atomics.state("a"),
				Atomics.state("b", "a"),
				Atomics.state("x")
			))).isTrue();

			assertThat(aUntilB.validate(LTLTrace.of(
				Atomics.state("a"),
				Atomics.state("b")
			))).isTrue();

			assertThat(aUntilB.validate(LTLTrace.of(
				Atomics.state("a")
			))).isTrue();

		}

		@Example
		void doesNotHold() {
			LTLFormula<Set<String>> aUntilB = Atomics.fact("a").until(Atomics.fact("b"));

			assertThat(aUntilB.validate(LTLTrace.of(
				Atomics.state("a"),
				Atomics.state("a"),
				Atomics.state("a"),
				Atomics.state("x")
			))).isFalse();

			assertThat(aUntilB.validate(LTLTrace.of(
				Atomics.state("x"),
				Atomics.state("a"),
				Atomics.state("a"),
				Atomics.state("b")
			))).isFalse();

			assertThat(aUntilB.validate(LTLTrace.of(
				Atomics.state("b")
			))).isFalse();

			assertThat(aUntilB.validate(LTLTrace.of())).isFalse();
		}
	}

	@Group
	class Not {

		@Example
		void notAFact() {
			LTLFormula<Set<String>> notA = not(Atomics.fact("a"));
			assertThat(notA.validate(Atomics.state("b", "c"))).isTrue();
			assertThat(notA.validate(Atomics.state("b", "a"))).isFalse();
		}

		@Example
		void notAlways() {
			LTLFormula<Set<String>> notAlwaysA = not(always(Atomics.fact("a")));
			assertThat(notAlwaysA.validate(LTLTrace.of(
				Atomics.state("b", "a"),
				Atomics.state("b", "c")
			))).isTrue();
			assertThat(notAlwaysA.validate(LTLTrace.of(
				Atomics.state("b", "a"),
				Atomics.state("b", "a"),
				Atomics.state("a")
			))).isFalse();
		}

		@Example
		void notNext() {
			LTLFormula<Set<String>> notNext = not(next(Atomics.fact("a")));
			assertThat(notNext.validate(LTLTrace.of(
				Atomics.state("b", "a"),
				Atomics.state("b", "c")
			))).isTrue();
			assertThat(notNext.validate(LTLTrace.of(
				Atomics.state("b"),
				Atomics.state("b", "a"),
				Atomics.state("x")
			))).isFalse();
		}

	}

	@Group
	class And {

		@Example
		void factAndFact() {
			LTLFormula<Set<String>> aAndB = Atomics.fact("a").and(Atomics.fact("b"));
			assertThat(aAndB.validate(Atomics.state("b", "a"))).isTrue();
			assertThat(aAndB.validate(Atomics.state("b", "x"))).isFalse();
		}

		@Example
		void factAndNext() {
			LTLFormula<Set<String>> aAndThenB = Atomics.fact("a").and(next(Atomics.fact("b")));

			assertThat(aAndThenB.validate(LTLTrace.of(
				Atomics.state("b", "a"),
				Atomics.state("b"),
				Atomics.state("a")
			))).isTrue();

			assertThat(aAndThenB.validate(LTLTrace.of(
				Atomics.state("b"),
				Atomics.state("b"),
				Atomics.state("a")
			))).isFalse();

			assertThat(aAndThenB.validate(LTLTrace.of(
				Atomics.state("a", "b"),
				Atomics.state("a"),
				Atomics.state("b")
			))).isFalse();
		}

	}

	@Group
	class Or {

		@Example
		void factOrFact() {
			LTLFormula<Set<String>> aOrB = Atomics.fact("a").or(Atomics.fact("b"));
			assertThat(aOrB.validate(Atomics.state("b", "x"))).isTrue();
			assertThat(aOrB.validate(Atomics.state("y", "x"))).isFalse();
		}

		@Example
		void factOrNext() {
			LTLFormula<Set<String>> aOrNextB = Atomics.fact("a").or(next(Atomics.fact("b")));

			assertThat(aOrNextB.validate(LTLTrace.of(
				Atomics.state("b", "a"),
				Atomics.state("c"),
				Atomics.state("a")
			))).isTrue();

			assertThat(aOrNextB.validate(LTLTrace.of(
				Atomics.state("b"),
				Atomics.state("b"),
				Atomics.state("x")
			))).isTrue();

			assertThat(aOrNextB.validate(LTLTrace.of(
				Atomics.state("b"),
				Atomics.state("a"),
				Atomics.state("c")
			))).isFalse();
		}
	}

	@Group
	class Implies {

		@Example
		void factImpliesFact() {
			LTLFormula<Set<String>> aImpliesB = Atomics.fact("a").implies(Atomics.fact("b"));
			assertThat(aImpliesB.validate(Atomics.state("c", "x"))).isTrue();
			assertThat(aImpliesB.validate(Atomics.state("a", "x", "b"))).isTrue();
			assertThat(aImpliesB.validate(Atomics.state("a"))).isFalse();
		}

		@Example
		void factImpliesNext() {
			LTLFormula<Set<String>> aImpliesNextB = Atomics.fact("a").implies(next(Atomics.fact("b")));

			assertThat(aImpliesNextB.validate(LTLTrace.of(
				Atomics.state("c"),
				Atomics.state("c", "a"),
				Atomics.state("b")
			))).isTrue();

			assertThat(aImpliesNextB.validate(LTLTrace.of(
				Atomics.state("a"),
				Atomics.state("b", "a"),
				Atomics.state("b")
			))).isTrue();

			assertThat(aImpliesNextB.validate(LTLTrace.of(
				Atomics.state("a"),
				Atomics.state("a"),
				Atomics.state("b")
			))).isFalse();
		}

	}

	@Group
	class Next {

		@Example
		void matches() {
			var next = next(Atomics.fact("a"));
			boolean matches = next.validate(LTLTrace.of(
				Atomics.state("c"),
				Atomics.state("a", "b"),
				Atomics.state("b")
			));

			assertThat(matches).isTrue();
		}

		@Example
		void doesNotMatch() {
			var next = next(Atomics.fact("b"));

			assertThat(next.validate(LTLTrace.of(
				Atomics.state("a", "b", "c"),
				Atomics.state("a"),
				Atomics.state("a", "b")
			))).isFalse();

			assertThat(next.validate(LTLTrace.of())).isFalse();
		}

		@Example
		void withNesting() {
			var next = next(always(Atomics.fact("a")));

			assertThat(next.validate(LTLTrace.of(
				Atomics.state("c"),
				Atomics.state("a", "b"),
				Atomics.state("a")
			))).isTrue();

			assertThat(next.validate(LTLTrace.of(
				Atomics.state("c"),
				Atomics.state("a", "b"),
				Atomics.state("b")
			))).isFalse();
		}

		@Example
		void withPrecondition() {
			var ifPreThenNext = Atomics.fact("pre").implies(next(Atomics.fact("next")));

			assertThat(ifPreThenNext.validate(LTLTrace.of(
				Atomics.state("pre"),
				Atomics.state("a", "next"),
				Atomics.state("a")
			))).isTrue();

			assertThat(ifPreThenNext.validate(LTLTrace.of(
				Atomics.state("a"),
				Atomics.state("a")
			))).isTrue();

			assertThat(ifPreThenNext.validate(LTLTrace.of(
				Atomics.state("pre"),
				Atomics.state("a")
			))).isFalse();

			assertThat(ifPreThenNext.validate(LTLTrace.of(
				Atomics.state("pre")
			))).isFalse();

			assertThat(always(ifPreThenNext).validate(LTLTrace.of(
				Atomics.state("pre"),
				Atomics.state("a", "next", "pre"),
				Atomics.state("a")
			))).isFalse();
		}

	}

	@Group
	class Eventually {

		@Example
		void matches() {
			var eventually = eventually(Atomics.fact("a"));

			assertThat(eventually.validate(LTLTrace.of(
				Atomics.state("c"),
				Atomics.state("a", "b"),
				Atomics.state("b")
			))).isTrue();

			assertThat(eventually.validate(LTLTrace.of(
				Atomics.state("c"),
				Atomics.state("c"),
				Atomics.state(),
				Atomics.state("c"),
				Atomics.state(),
				Atomics.state("a", "b")
			))).isTrue();
		}

		@Example
		void doesNotMatch() {
			var eventually = eventually(Atomics.fact("a"));

			assertThat(eventually.validate(LTLTrace.of(
				Atomics.state("c"),
				Atomics.state("b"),
				Atomics.state(),
				Atomics.state("b")
			))).isFalse();

			assertThat(eventually.validate(LTLTrace.of())).isFalse();
		}

		@Example
		void nested() {
			var eventually = eventually(always(Atomics.fact("a")));

			assertThat(eventually.validate(LTLTrace.of(
				Atomics.state("c"),
				Atomics.state(),
				Atomics.state("a", "b"),
				Atomics.state("a")
			))).isTrue();

			assertThat(eventually.validate(LTLTrace.of(
				Atomics.state("c"),
				Atomics.state("a", "b"),
				Atomics.state("a"),
				Atomics.state()
			))).isFalse();
		}

	}

	@Group
	class Always {

		@Example
		void matches() {
			var always = always(Atomics.fact("a"));
			boolean matches = always.validate(LTLTrace.of(
				Atomics.state("a", "b", "c"),
				Atomics.state("a", "b"),
				Atomics.state("a")
			));

			assertThat(matches).isTrue();
		}

		@Example
		void doesNotMatch() {
			var always = always(Atomics.fact("b"));
			boolean matches = always.validate(LTLTrace.of(
				Atomics.state("a", "b", "c"),
				Atomics.state("a", "b"),
				Atomics.state("a")
			));

			assertThat(matches).isFalse();
		}

		@Example
		void nested() {
			var always = always(always(Atomics.fact("a")));
			boolean matches = always.validate(LTLTrace.of(
				Atomics.state("a", "b", "c"),
				Atomics.state("a", "b"),
				Atomics.state("a")
			));

			assertThat(matches).isTrue();
		}

	}

}
