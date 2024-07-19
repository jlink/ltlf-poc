package ltlf;

public interface TraceChecker {

	static TraceChecker always(StateChecker checker) {
		return new Always(checker);
	}

	boolean check(LTLTrace trace);

	class Always implements TraceChecker {
		private final StateChecker checker;

		public Always(StateChecker checker) {
			this.checker = checker;
		}

		@Override
		public boolean check(LTLTrace trace) {
			for (LTLState state : trace.states()) {
				if (!checker.check(state)) {
					return false;
				}
			}
			return true;
		}
	}
}
