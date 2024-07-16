package ltlf;

import org.assertj.core.api.*;

import net.jqwik.api.*;

class LTLTests {

	@Example
	void test() {
		LTL ltl = new LTL();
		boolean check = ltl.check();

		Assertions.assertThat(check).isTrue();
	}
}
