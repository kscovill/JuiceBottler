
/**
 * Assembly Line Stack class provided by Nate Williams
 * I changed a return value to elminate the need for a new
 * method to remove the orange from the stack.
 */
import java.util.ArrayList;
import java.util.List;

public class AssemblyLine extends Thread {
	private final List<Orange> oranges;

	/**
	 * Initialize the arraylist as a new arraylist for each conveyor belt.
	 */
	public AssemblyLine() {
		oranges = new ArrayList<Orange>();
	}

	/**
	 * Add an orange to the ArrayList
	 * 
	 * @param o
	 */
	public synchronized void addOrange(Orange o) {
		oranges.add(o);
		if (countOranges() == 1) {
			notifyAll();
		}
	}

	/**
	 * Gets the first orange in the sequence
	 * 
	 * @return
	 */
	public synchronized Orange getOrange() {
		while (countOranges() == 0) {
			try {
				wait();
			} catch (InterruptedException ignored) {
			}
		}
		// Fix for needing a removeOrange() method provided by Cordell Anderson
		return oranges.remove(0);
	}

	/**
	 * Count the total number of oranges in the ArrayList
	 * 
	 * @return
	 */
	public synchronized int countOranges() {
		return oranges.size();
	}
}
