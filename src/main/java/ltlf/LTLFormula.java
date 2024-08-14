package ltlf;

public interface LTLFormula<S> {

	boolean validate(LTLTrace<S> trace);

	default boolean validate(LTLState<S> state) {
		return validate(LTLTrace.of(state));
	}

	default LTLFormula<S> and(LTLFormula<S> other) {
		return state -> validate(state) && other.validate(state);
	}

	default LTLFormula<S> or(LTLFormula<S> other) {
		return state -> validate(state) || other.validate(state);
	}

	default LTLFormula<S> implies(LTLFormula<S> other) {
		return state -> !validate(state) || other.validate(state);
	}

	default LTLFormula<S> negate() {
		return state -> !this.validate(state);
	}

	default LTLFormula<S> until(LTLFormula<S> condition) {
		return new Until<>(this, condition);
	}

	static <S> LTLFormula<S> not(LTLFormula<S> formula) {
		return formula.negate();
	}

	static <S> LTLFormula<S> always(LTLFormula<S> formula) {
		return new Always<>(formula);
	}

	static <S> LTLFormula<S> next(LTLFormula<S> formula) {
		return trace -> {
			if (trace.isEmpty()) {
				return false;
			}
			return formula.validate(trace.rest());
		};
	}

	static <S> LTLFormula<S> eventually(LTLFormula<S> formula) {
		return new Eventually<>(formula);
	}

	static <S> LTLFormula<S> last(LTLFormula<S> formula) {
		return trace -> {
			if (trace.isEmpty()) {
				return false;
			}
			return formula.validate(trace.getLast());
		};
	}

	@FunctionalInterface
	interface Fact<S> extends LTLFormula<S> {

		boolean check(S state);

		default boolean validate(LTLTrace<S> trace) {
			if (trace.isEmpty()) {
				return false;
			}
			LTLState<S> first = trace.states().getFirst();
			return check(first.state());
		}
	}

	class Eventually<S> implements LTLFormula<S> {
		private final LTLFormula<S> formula;

		public Eventually(LTLFormula<S> formula) {
			this.formula = formula;
		}

		@Override
		public boolean validate(LTLTrace<S> trace) {
			LTLTrace<S> current = trace;
			while (!current.isEmpty()) {
				if (formula.validate(current)) {
					return true;
				}
				current = current.rest();
			}
			return false;
		}
	}

	class Always<S> implements LTLFormula<S> {
		private final LTLFormula<S> formula;

		public Always(LTLFormula<S> formula) {
			this.formula = formula;
		}

		@Override
		public boolean validate(LTLTrace<S> trace) {
			LTLTrace<S> current = trace;
			while (!current.isEmpty()) {
				if (!formula.validate(current)) {
					return false;
				}
				current = current.rest();
			}
			return true;
		}
	}

	class Until<S> implements LTLFormula<S> {
		private final LTLFormula<S> hold;
		private final LTLFormula<S> until;

		public Until(LTLFormula<S> hold, LTLFormula<S> until) {
			this.hold = hold;
			this.until = until;
		}

		@Override
		public boolean validate(LTLTrace<S> trace) {
			if (trace.isEmpty() || !hold.validate(trace)) {
				return false;
			}

			LTLTrace<S> rest = trace.rest();
			return rest.isEmpty()
					   || until.validate(rest)
					   || validate(rest);
		}
	}
}
