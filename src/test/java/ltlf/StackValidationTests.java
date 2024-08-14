package ltlf;

import java.util.*;

import net.jqwik.api.*;
import net.jqwik.api.lifecycle.*;

import static org.assertj.core.api.Assertions.*;

class StackValidationTests {

	static class ImmutableStack {

		private final List<Integer> elements;

		private ImmutableStack(List<Integer> elements) {
			this.elements = Collections.unmodifiableList(elements);
		}

		public ImmutableStack() {
			this.elements = Collections.emptyList();
		}

		public ImmutableStack push(int value) {
			List<Integer> newElements = new LinkedList<>(elements);
			newElements.addFirst(value);
			return new ImmutableStack(newElements);
		}

		public ImmutableStack pop() {
			if (elements.isEmpty()) {
				throw new IllegalStateException("Cannot pop from an empty stack");
			}
			List<Integer> newElements = new LinkedList<>(elements);
			newElements.removeFirst();
			return new ImmutableStack(newElements);
		}

		public int top() {
			if (elements.isEmpty()) {
				throw new IllegalStateException("Stack is empty");
			}
			return elements.getFirst();
		}

		public boolean isEmpty() {
			return elements.isEmpty();
		}

		public int size() {
			return elements.size();
		}

		@Override
		public String toString() {
			return elements.toString();
		}
	}

	LTL<ImmutableStack> ltl;

	@BeforeExample
	void setup() {
		ltl = new LTL<>();
		ltl.addFormula(stackIsInitiallyEmpty());
		ltl.addFormula(stackSizeAlwaysBetween0and2());
		ltl.addFormula(isEmptyIsEquivalentToSizeIs0());
		ltl.addFormula(everyTopCanBePopped());
	}

	private LTLFormula<ImmutableStack> everyTopCanBePopped() {
		LTLFormula.Fact<ImmutableStack> isNotEmpty = stack -> !stack.isEmpty();
		LTLFormula.Fact<ImmutableStack> topCanBePopped = stack -> {
			int previousSize = stack.size();
			int top = stack.top();
			var newStack = stack.pop();
			return newStack.size() == previousSize - 1;
		};
		return LTLFormula.always(
			isNotEmpty.implies(topCanBePopped)
		);
	}

	private LTLFormula<ImmutableStack> isEmptyIsEquivalentToSizeIs0() {
		LTLFormula.Fact<ImmutableStack> isEmpty = stack -> stack.isEmpty();
		LTLFormula.Fact<ImmutableStack> sizeIs0 = stack -> stack.size() == 0;
		return LTLFormula.always(
			isEmpty.implies(sizeIs0).and(sizeIs0.implies(isEmpty))
		);
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
			new ImmutableStack().push(1).push(2)
		))).isFalse();
	}

	@Example
	void alwaysMatching() {
		assertThat(ltl.matches(
			LTLTrace.with(new ImmutableStack())
					.next(s -> s.push(1))
					.next(s -> s.push(2))
					.next(s -> s.pop())
		)).isTrue();

		assertThat(ltl.matches(
			LTLTrace.with(new ImmutableStack())
					.next(s -> s.push(1))
					.next(s -> s.push(2))
		)).isTrue();

		assertThat(ltl.matches(
			LTLTrace.with(new ImmutableStack())
					.next(s -> s.push(1))
					.next(s -> s.push(2))
					.next(s -> s.push(3))
		)).isFalse();
	}
}
