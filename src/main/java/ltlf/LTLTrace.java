package ltlf;

import java.util.*;

public record LTLTrace<S>(List<LTLState<S>> states) {

	@SafeVarargs
	public static <S> LTLTrace<S> of(S ... states) {
		List<LTLState<S>> listOfStates = new ArrayList<>();
		for (S state : states) {
			listOfStates.add(new LTLState<>(state));
		}
		return new LTLTrace<>(listOfStates);
	}

	@SafeVarargs
	public static <S> LTLTrace<S> of(LTLState<S> ... states) {
		return new LTLTrace<>(List.of(states));
	}

	public LTLTrace<S> rest() {
		return new LTLTrace<>(states().subList(1, states().size()));
	}

	public boolean isEmpty() {
		return states().isEmpty();
	}

	public LTLState<S> getLast() {
		return states.getLast();
	}
}
