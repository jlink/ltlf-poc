package ltlf;

public interface StateChecker {
	static StateChecker fact(String a) {
		return new Fact(a);
	}

	static StateChecker not(StateChecker checker) {
		return state -> !checker.check(state);
	}

	boolean check(LTLState state);

	class Fact implements StateChecker {
		private final String atom;

		public Fact(String atom) {
			this.atom = atom.trim().toLowerCase();
		}

		@Override
		public boolean check(LTLState state) {
			return state.contains(atom);
		}
	}
}
