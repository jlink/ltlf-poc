package ltlf;

import net.jqwik.api.*;

import static ltlf.LTLState.*;
import static org.assertj.core.api.Assertions.*;

class LTLTests {

	@Example
	void checkEmptyTrace() {
		LTL ltl = new LTL();
		boolean check = ltl.check();

		assertThat(check).isTrue();
	}

	@Example
	void checkNoFormulae() {
		LTL ltl = new LTL();

		boolean check = ltl.check(
				facts("a", "b", "c"),
				facts("a", "b"),
				facts("a")
		);

		assertThat(check).isTrue();
	}

}
