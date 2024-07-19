package ltlf;

import java.util.*;

public record LTLTrace(List<LTLState> states) {
	public static LTLTrace of(LTLState ... states) {
		return new LTLTrace(List.of(states));
	}
}
