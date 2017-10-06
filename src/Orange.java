/**
 * Orange class provided by Nate Williams, no editing done by myself.
 * 
 * @author Nate Williams
 *
 */
public class Orange {

	/**
	 * Initialize the states and the times to complete each step add a getNext
	 * method to move to the next state
	 *
	 */
	public enum State {
		Fetched(15), Peeled(38), Squeezed(29), Bottled(17), Processed(1);

		private static final int finalIndex = State.values().length - 1;

		final int timeToComplete;

		State(int timeToComplete) {
			this.timeToComplete = timeToComplete;
		}

		State getNext() {
			int currIndex = this.ordinal();
			if (currIndex >= finalIndex) {
				throw new IllegalStateException("Already at final state");
			}
			return State.values()[currIndex + 1];
		}
	}

	private State state;

	/**
	 * Orange constructor initializes the state of the orange works for time alloted
	 */
	public Orange() {
		state = State.Fetched;
		doWork();
	}

	/**
	 * returns the state of the orange
	 * 
	 * @return
	 */
	public State getState() {
		return state;
	}

	/**
	 * Don't attempt to process an already completed orange
	 */
	public void runProcess() {

		if (state == State.Processed) {
			throw new IllegalStateException("This orange has already been processed");
		}
		state = state.getNext();
		doWork();

	}

	/**
	 * Sleep for the amount of time necessary to do the work
	 */
	private void doWork() {

		try {
			Thread.sleep(state.timeToComplete);
		} catch (InterruptedException e) {
			System.err.println("Incomplete orange processing, juice may be bad");
		}
	}
}