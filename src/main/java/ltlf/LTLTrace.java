package ltlf;

import java.util.*;

public record LTLTrace(List<LTLState> states) {
	public static LTLTrace of(LTLState ... states) {
		return new LTLTrace(List.of(states));
	}

	public LTLTrace rest() {
		return new LTLTrace(states().subList(1, states().size()));
	}

	public boolean isEmpty() {
		return states().isEmpty();
	}

	public LTLState getLast() {
		return states.getLast();
	}
}
