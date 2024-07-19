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
			LTLTrace current = trace;
			while (!current.isEmpty()) {
				if (!checker.check(current)) {
					return false;
				}
				current = current.rest();
			}

			return true;
		}

	}
}
