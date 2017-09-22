
public class AssemblyLine {
	private final List<Orange> oranges;

	public AssemblyLine() {
		oranges = new ArrayList<Orange>();
	}

	public synchronized void addOrange(Orange o) {
		oranges.add(o);
		if(countOranges() == 1) {
			try {
				notifyAll();
			} catch (InterruptedException ignored) {
	
			}
		}
	}

	public synchronized Orange getOrange() {
		while (countOranges() == 0) {
			try {
				wait();
			} catch (InterruptedException ignored) {

			}
		}

		return oranges.get(0);
	}
}
