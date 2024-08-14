package ltlf;

import java.util.*;

public record AtomicFact(String atom) implements LTLFormula.Fact<Set<String>> {

	public static LTLFormula fact(String atom) {
		return new AtomicFact(atom);
	}

	public AtomicFact(String atom) {
		this.atom = atom.trim().toLowerCase();
	}

	@Override
	public boolean check(LTLState<Set<String>> state) {
		return state.state().contains(atom);
	}
}
