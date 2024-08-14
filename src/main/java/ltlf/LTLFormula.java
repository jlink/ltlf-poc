package ltlf;

public interface LTLFormula {

	boolean validate(LTLTrace trace);

	default boolean validate(LTLState state) {
		return validate(LTLTrace.of(state));
	}

	default LTLFormula and(LTLFormula other) {
		return state -> validate(state) && other.validate(state);
	}

	default LTLFormula or(LTLFormula other) {
		return state -> validate(state) || other.validate(state);
	}

	default LTLFormula implies(LTLFormula other) {
		return state -> !validate(state) || other.validate(state);
	}

	static LTLFormula not(LTLFormula checker) {
		return state -> !checker.validate(state);
	}

	static LTLFormula always(LTLFormula checker) {
		return new Always(checker);
	}

	static LTLFormula next(LTLFormula checker) {
		return trace -> {
			if (trace.isEmpty()) {
				return false;
			}
			return checker.validate(trace.rest());
		};
	}

	static LTLFormula eventually(LTLFormula checker) {
		return new Eventually(checker);
	}

	static LTLFormula last(LTLFormula checker) {
		return trace -> {
			if (trace.isEmpty()) {
				return false;
			}
			return checker.validate(trace.getLast());
		};
	}

	default LTLFormula until(LTLFormula condition) {
		return new Until(this, condition);
	}

	interface Fact<S> extends LTLFormula {

		boolean check(LTLState<S> state);

		default boolean validate(LTLTrace trace) {
			if (trace.isEmpty()) {
				return false;
			}
			LTLState<S> first = (LTLState<S>) trace.states().getFirst();
			return check(first);
		}


	}

	class Eventually implements LTLFormula {
		private final LTLFormula checker;

		public Eventually(LTLFormula checker) {
			this.checker = checker;
		}

		@Override
		public boolean validate(LTLTrace trace) {
			LTLTrace current = trace;
			while (!current.isEmpty()) {
				if (checker.validate(current)) {
					return true;
				}
				current = current.rest();
			}

			return false;
		}
	}

	class Always implements LTLFormula {
		private final LTLFormula checker;

		public Always(LTLFormula checker) {
			this.checker = checker;
		}

		@Override
		public boolean validate(LTLTrace trace) {
			LTLTrace current = trace;
			while (!current.isEmpty()) {
				if (!checker.validate(current)) {
					return false;
				}
				current = current.rest();
			}

			return true;
		}
	}

	class Until implements LTLFormula {
		private final LTLFormula hold;
		private final LTLFormula until;

		public Until(LTLFormula hold, LTLFormula until) {
			this.hold = hold;
			this.until = until;
		}

		@Override
		public boolean validate(LTLTrace trace) {
			if (trace.isEmpty() || !hold.validate(trace)) {
				return false;
			}

			LTLTrace rest = trace.rest();
			return rest.isEmpty()
					   || until.validate(rest)
					   || validate(rest);
		}
	}
}
