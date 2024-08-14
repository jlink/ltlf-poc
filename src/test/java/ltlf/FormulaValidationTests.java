package ltlf;

import net.jqwik.api.*;

import static ltlf.LTLFormula.not;
import static ltlf.LTLFormula.*;
import static ltlf.LTLState.*;
import static org.assertj.core.api.Assertions.*;

@Group
class FormulaValidationTests {

	@Example
	void facts() {
		assertThat(fact("a").validate(atoms("a"))).isTrue();
		assertThat(fact("a").validate(atoms("x", "a", "y"))).isTrue();
		assertThat(fact("a").validate(atoms("x", "y"))).isFalse();
		assertThat(fact("a").validate(atoms())).isFalse();
		assertThat(fact("a").validate(LTLTrace.of())).isFalse();
	}

	@Example
	void lastFormula() {
		assertThat(last(fact("a")).validate(LTLTrace.of(
			atoms("a"),
			atoms("a")
		))).isTrue();
		assertThat(last(fact("a")).validate(LTLTrace.of(
			atoms("a"),
			atoms("b")
		))).isFalse();
		assertThat(last(fact("a")).validate(LTLTrace.of())).isFalse();
	}

	@Group
	class Until {

		@Example
		void holds() {
			LTLFormula aUntilB = fact("a").until(fact("b"));

			assertThat(aUntilB.validate(LTLTrace.of(
				atoms("a"),
				atoms("a"),
				atoms("a"),
				atoms("a"),
				atoms("b")
			))).isTrue();

			assertThat(aUntilB.validate(LTLTrace.of(
				atoms("a"),
				atoms("a"),
				atoms("a"),
				atoms("a"),
				atoms("b"),
				atoms("x")
			))).isTrue();

			assertThat(aUntilB.validate(LTLTrace.of(
				atoms("a"),
				atoms("a"),
				atoms("a"),
				atoms("a"),
				atoms("b", "a"),
				atoms("x")
			))).isTrue();

			assertThat(aUntilB.validate(LTLTrace.of(
				atoms("a"),
				atoms("b")
			))).isTrue();

			assertThat(aUntilB.validate(LTLTrace.of(
				atoms("a")
			))).isTrue();

		}

		@Example
		void doesNotHold() {
			LTLFormula aUntilB = fact("a").until(fact("b"));

			assertThat(aUntilB.validate(LTLTrace.of(
				atoms("a"),
				atoms("a"),
				atoms("a"),
				atoms("x")
			))).isFalse();

			assertThat(aUntilB.validate(LTLTrace.of(
				atoms("x"),
				atoms("a"),
				atoms("a"),
				atoms("b")
			))).isFalse();

			assertThat(aUntilB.validate(LTLTrace.of(
				atoms("b")
			))).isFalse();

			assertThat(aUntilB.validate(LTLTrace.of())).isFalse();
		}
	}

	@Group
	class Not {

		@Example
		void notAFact() {
			LTLFormula notA = not(fact("a"));
			assertThat(notA.validate(atoms("b", "c"))).isTrue();
			assertThat(notA.validate(atoms("b", "a"))).isFalse();
		}

		@Example
		void notAlways() {
			LTLFormula notAlwaysA = not(always(fact("a")));
			assertThat(notAlwaysA.validate(LTLTrace.of(
				atoms("b", "a"),
				atoms("b", "c")
			))).isTrue();
			assertThat(notAlwaysA.validate(LTLTrace.of(
				atoms("b", "a"),
				atoms("b", "a"),
				atoms("a")
			))).isFalse();
		}

		@Example
		void notNext() {
			LTLFormula notNext = not(next(fact("a")));
			assertThat(notNext.validate(LTLTrace.of(
				atoms("b", "a"),
				atoms("b", "c")
			))).isTrue();
			assertThat(notNext.validate(LTLTrace.of(
				atoms("b"),
				atoms("b", "a"),
				atoms("x")
			))).isFalse();
		}

	}

	@Group
	class And {

		@Example
		void factAndFact() {
			LTLFormula aAndB = fact("a").and(fact("b"));
			assertThat(aAndB.validate(atoms("b", "a"))).isTrue();
			assertThat(aAndB.validate(atoms("b", "x"))).isFalse();
		}

		@Example
		void factAndNext() {
			LTLFormula aAndThenB = fact("a").and(next(fact("b")));

			assertThat(aAndThenB.validate(LTLTrace.of(
				atoms("b", "a"),
				atoms("b"),
				atoms("a")
			))).isTrue();

			assertThat(aAndThenB.validate(LTLTrace.of(
				atoms("b"),
				atoms("b"),
				atoms("a")
			))).isFalse();

			assertThat(aAndThenB.validate(LTLTrace.of(
				atoms("a", "b"),
				atoms("a"),
				atoms("b")
			))).isFalse();
		}

	}

	@Group
	class Or {

		@Example
		void factOrFact() {
			LTLFormula aOrB = fact("a").or(fact("b"));
			assertThat(aOrB.validate(atoms("b", "x"))).isTrue();
			assertThat(aOrB.validate(atoms("y", "x"))).isFalse();
		}

		@Example
		void factOrNext() {
			LTLFormula aOrNextB = fact("a").or(next(fact("b")));

			assertThat(aOrNextB.validate(LTLTrace.of(
				atoms("b", "a"),
				atoms("c"),
				atoms("a")
			))).isTrue();

			assertThat(aOrNextB.validate(LTLTrace.of(
				atoms("b"),
				atoms("b"),
				atoms("x")
			))).isTrue();

			assertThat(aOrNextB.validate(LTLTrace.of(
				atoms("b"),
				atoms("a"),
				atoms("c")
			))).isFalse();
		}
	}

	@Group
	class Implies {

		@Example
		void factImpliesFact() {
			LTLFormula aImpliesB = fact("a").implies(fact("b"));
			assertThat(aImpliesB.validate(atoms("c", "x"))).isTrue();
			assertThat(aImpliesB.validate(atoms("a", "x", "b"))).isTrue();
			assertThat(aImpliesB.validate(atoms("a"))).isFalse();
		}

		@Example
		void factImpliesNext() {
			LTLFormula aImpliesNextB = fact("a").implies(next(fact("b")));

			assertThat(aImpliesNextB.validate(LTLTrace.of(
				atoms("c"),
				atoms("c", "a"),
				atoms("b")
			))).isTrue();

			assertThat(aImpliesNextB.validate(LTLTrace.of(
				atoms("a"),
				atoms("b", "a"),
				atoms("b")
			))).isTrue();

			assertThat(aImpliesNextB.validate(LTLTrace.of(
				atoms("a"),
				atoms("a"),
				atoms("b")
			))).isFalse();
		}

	}

	@Group
	class Next {

		@Example
		void matches() {
			var next = next(fact("a"));
			boolean matches = next.validate(LTLTrace.of(
				atoms("c"),
				atoms("a", "b"),
				atoms("b")
			));

			assertThat(matches).isTrue();
		}

		@Example
		void doesNotMatch() {
			var next = next(fact("b"));

			assertThat(next.validate(LTLTrace.of(
				atoms("a", "b", "c"),
				atoms("a"),
				atoms("a", "b")
			))).isFalse();

			assertThat(next.validate(LTLTrace.of())).isFalse();
		}

		@Example
		void withNesting() {
			var next = next(always(fact("a")));

			assertThat(next.validate(LTLTrace.of(
				atoms("c"),
				atoms("a", "b"),
				atoms("a")
			))).isTrue();

			assertThat(next.validate(LTLTrace.of(
				atoms("c"),
				atoms("a", "b"),
				atoms("b")
			))).isFalse();
		}

		@Example
		void withPrecondition() {
			var ifPreThenNext = fact("pre").implies(next(fact("next")));

			assertThat(ifPreThenNext.validate(LTLTrace.of(
				atoms("pre"),
				atoms("a", "next"),
				atoms("a")
			))).isTrue();

			assertThat(ifPreThenNext.validate(LTLTrace.of(
				atoms("a"),
				atoms("a")
			))).isTrue();

			assertThat(ifPreThenNext.validate(LTLTrace.of(
				atoms("pre"),
				atoms("a")
			))).isFalse();

			assertThat(ifPreThenNext.validate(LTLTrace.of(
				atoms("pre")
			))).isFalse();

			assertThat(always(ifPreThenNext).validate(LTLTrace.of(
				atoms("pre"),
				atoms("a", "next", "pre"),
				atoms("a")
			))).isFalse();
		}

	}

	@Group
	class Eventually {

		@Example
		void matches() {
			var eventually = eventually(fact("a"));

			assertThat(eventually.validate(LTLTrace.of(
				atoms("c"),
				atoms("a", "b"),
				atoms("b")
			))).isTrue();

			assertThat(eventually.validate(LTLTrace.of(
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

			assertThat(eventually.validate(LTLTrace.of(
				atoms("c"),
				atoms("b"),
				atoms(),
				atoms("b")
			))).isFalse();

			assertThat(eventually.validate(LTLTrace.of())).isFalse();
		}

		@Example
		void nested() {
			var eventually = eventually(always(fact("a")));

			assertThat(eventually.validate(LTLTrace.of(
				atoms("c"),
				atoms(),
				atoms("a", "b"),
				atoms("a")
			))).isTrue();

			assertThat(eventually.validate(LTLTrace.of(
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
			boolean matches = always.validate(LTLTrace.of(
				atoms("a", "b", "c"),
				atoms("a", "b"),
				atoms("a")
			));

			assertThat(matches).isTrue();
		}

		@Example
		void doesNotMatch() {
			var always = always(fact("b"));
			boolean matches = always.validate(LTLTrace.of(
				atoms("a", "b", "c"),
				atoms("a", "b"),
				atoms("a")
			));

			assertThat(matches).isFalse();
		}

		@Example
		void nested() {
			var always = always(always(fact("a")));
			boolean matches = always.validate(LTLTrace.of(
				atoms("a", "b", "c"),
				atoms("a", "b"),
				atoms("a")
			));

			assertThat(matches).isTrue();
		}

	}

}
