package ltlf;

public interface StateChecker {
	static StateChecker fact(String a) {
		return new Fact(a);
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
