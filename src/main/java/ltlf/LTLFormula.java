package ltlf;

public interface LTLFormula extends TraceChecker {

	static LTLFormula always(StateChecker checker) {
		return new Always(checker);
	}

	class Always implements LTLFormula {
		private final StateChecker checker;

		public Always(StateChecker checker) {
			this.checker = checker;
		}

		@Override
		public boolean check(LTLTrace trace) {
			for (LTLState state : trace.states()) {
				if (!checker.checkState(state)) {
					return false;
				}
			}
			return true;
		}
	}
}
