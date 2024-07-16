package ltlf;

public interface LTLFormula {

	static LTLFormula always(StateChecker checker) {
		return new Always(checker);
	}

	boolean matches(LTLTrace trace);

	class Always implements LTLFormula {
		private final StateChecker checker;

		public Always(StateChecker checker) {
			this.checker = checker;
		}

		@Override
		public boolean matches(LTLTrace trace) {
			for (LTLState state : trace.states()) {
				if (!checker.check(state)) {
					return false;
				}
			}
			return true;
		}
	}
}
