package ltlf;

public interface LTLFormula extends TraceChecker {

	static LTLFormula always(TraceChecker checker) {
		return new Always(checker);
	}

	class Always implements LTLFormula {
		private final TraceChecker checker;

		public Always(TraceChecker checker) {
			this.checker = checker;
		}

		@Override
		public boolean check(LTLTrace trace) {
			for (LTLState state : trace.states()) {
				if (!checker.check(LTLTrace.of(state))) {
					return false;
				}
			}
			return true;
		}
	}
}
