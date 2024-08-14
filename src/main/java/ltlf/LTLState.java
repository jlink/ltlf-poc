package ltlf;

public record LTLState<S>(S state) {

	@Override
	public String toString() {
		return state.toString();
	}
}
