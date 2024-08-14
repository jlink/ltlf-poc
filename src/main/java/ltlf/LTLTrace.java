package ltlf;

import java.util.*;
import java.util.function.*;

public record LTLTrace<S>(List<LTLState<S>> states) {


	public static <S> LTLTrace<S> with(S initialState) {
		return of(new LTLState<>(initialState));
	}

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

	public LTLTrace<S> next(Function<S, S> transition) {
		List<LTLState<S>> nextStates = new ArrayList<>(states);
		nextStates.add(new LTLState<>(transition.apply(getLast().state())));
		return new LTLTrace<>(nextStates);
	}
}
