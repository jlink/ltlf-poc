package ltlf;

public interface StateChecker extends TraceChecker {
	static StateChecker fact(String a) {
		return new Fact(a);
	}

	static StateChecker not(StateChecker checker) {
		return state -> !checker.checkState(state);
	}

	@Override
	default boolean check(LTLTrace trace) {
		if (trace.isEmpty()) {
			return true;
		}
		return checkState(trace.states().getFirst());
	}

	boolean checkState(LTLState state);

	default StateChecker and(StateChecker other) {
		return state -> checkState(state) && other.checkState(state);
	}

	default StateChecker or(StateChecker other) {
		return state -> checkState(state) || other.checkState(state);
	}

	default StateChecker implies(StateChecker other) {
		return state -> !checkState(state) || other.checkState(state);
	}

	class Fact implements StateChecker {
		private final String atom;

		public Fact(String atom) {
			this.atom = atom.trim().toLowerCase();
		}

		@Override
		public boolean checkState(LTLState state) {
			return state.contains(atom);
		}
	}
}
