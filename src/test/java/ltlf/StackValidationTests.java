package ltlf;

import java.util.*;

import net.jqwik.api.*;
import net.jqwik.api.lifecycle.*;

import static org.assertj.core.api.Assertions.*;

class StackValidationTests {

	class ImmutableStack {

		private final List<Integer> elements;

		private ImmutableStack(List<Integer> elements) {
			this.elements = Collections.unmodifiableList(elements);
		}

		public ImmutableStack() {
			this.elements = Collections.emptyList();
		}

		public ImmutableStack push(int value) {
			List<Integer> newElements = new LinkedList<>(elements);
			newElements.add(0, value);
			return new ImmutableStack(newElements);
		}

		public ImmutableStack pop() {
			if (elements.isEmpty()) {
				throw new IllegalStateException("Cannot pop from an empty stack");
			}
			List<Integer> newElements = new LinkedList<>(elements);
			newElements.remove(0);
			return new ImmutableStack(newElements);
		}

		public int top() {
			if (elements.isEmpty()) {
				throw new IllegalStateException("Stack is empty");
			}
			return elements.get(0);
		}

		public boolean isEmpty() {
			return elements.isEmpty();
		}

		public int size() {
			return elements.size();
		}
	}

	LTL<ImmutableStack> ltl;

	@BeforeExample
	void setup() {
		ltl = new LTL<>();
		ltl.addFormula(stackIsInitiallyEmpty());
		ltl.addFormula(stackSizeAlwaysBetween0and2());
	}

	private LTLFormula<ImmutableStack> stackSizeAlwaysBetween0and2() {
		return LTLFormula.always(
			(LTLFormula.Fact<ImmutableStack>) stack -> stack.size() >= 0 && stack.size() <= 2
		);
	}

	private LTLFormula.Fact<ImmutableStack> stackIsInitiallyEmpty() {
		return ImmutableStack::isEmpty;
	}

	@Example
	void initialMatching() {
		assertThat(ltl.matches(LTLTrace.of(
			new ImmutableStack(),
			new ImmutableStack().push(1),
			new ImmutableStack().push(1).pop()
		))).isTrue();

		assertThat(ltl.matches(LTLTrace.of(
			new ImmutableStack().push(1)
		))).isFalse();
	}

	@Example
	void alwaysMatching() {
		assertThat(ltl.matches(LTLTrace.of(
			new ImmutableStack(),
			new ImmutableStack().push(1),
			new ImmutableStack().push(1).pop()
		))).isTrue();

		assertThat(ltl.matches(LTLTrace.of(
			new ImmutableStack(),
			new ImmutableStack().push(1),
			new ImmutableStack().push(1).push(2)
		))).isTrue();

		assertThat(ltl.matches(LTLTrace.of(
			new ImmutableStack(),
			new ImmutableStack().push(1),
			new ImmutableStack().push(1).push(2),
			new ImmutableStack().push(1).push(2).push(3)
		))).isFalse();
	}
}
