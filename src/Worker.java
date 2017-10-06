/**
 * Worker Class that makes a new worker thread that can do its own work
 * independent of any other worker as long as there are oranges to be worked
 * with on the conveyer belt ( Stack )
 * 
 * @author Kyle
 *
 */
public class Worker implements Runnable {
	public AssemblyLine in;
	public AssemblyLine out;
	public int role;
	public boolean timeToWork = false;

	/**
	 * constructor that sets the in AssemblyLine, the out AssemblyLine and the role
	 * that each worker is to do.
	 * 
	 * @param in
	 * @param out
	 * @param role
	 */
	public Worker(AssemblyLine in, AssemblyLine out, int role) {
		this();
		this.role = role;
		this.in = in;
		this.out = out;
	}

	public final Thread thread;

	/**
	 * Constructor that starts the new worker thread
	 */
	Worker() {
		thread = new Thread(this, "slave");
	}

	/**
	 * Start The Worker
	 */
	public void startWorker() {
		timeToWork = true;
		// System.out.println("This has started( should be 10 )");
		// thread.start();
	}

	/**
	 * Stop the worker
	 */
	public void stopWorker() {
		// System.out.println("This has stopped( should be 10 )");
		timeToWork = false;
	}

	/**
	 * Joins all worker threads and waits for them to stop
	 */
	public void waitToStop() {
		try {
			thread.join();
			// System.out.println("This has died( should be 10 )");
		} catch (InterruptedException e) {
			System.err.println(thread.getName() + " stop malfunction");
		}
	}

	/**
	 * decides which job this worker is to do.
	 */
	public void run() {
		// System.out.println("The " + Thread.currentThread().getName() + " is
		// Processing oranges");
		while (timeToWork) {
			if (role == 0) {
				fetchOrange();
			} else if (role == 1) {
				peelOrange();
			} else if (role == 2) {
				squeezeOrange();
			} else if (role == 3) {
				bottleOrange();
			} else if (role == 4) {
				processOrange();
			}
		}
	}

	/**
	 * fetch a new orange, increment oranges provided send orange to next worker
	 */
	public void fetchOrange() {
		Orange o = new Orange();
		Plant.orangesProvided[0]++;
		out.addOrange(o);
	}

	/**
	 * grab an orange from stack checks to make sure 100% that the orange fetched
	 * was worked on previously do work on the orange send orange to next worker
	 */
	public void peelOrange() {
		Orange o = in.getOrange();
		if (o.getState() == Orange.State.Fetched) {
			o.runProcess();
			out.addOrange(o);
		}
	}

	/**
	 * grab an orange from stack checks to make sure 100% that the orange fetched
	 * was worked on previously do work on the orange send orange to next worker
	 */
	public void squeezeOrange() {
		Orange o = in.getOrange();
		if (o.getState() == Orange.State.Peeled) {
			o.runProcess();
			out.addOrange(o);
		}
	}

	/**
	 * grab an orange from stack checks to make sure 100% that the orange fetched
	 * was worked on previously do work on the orange send orange to next worker
	 */
	public void bottleOrange() {
		Orange o = in.getOrange();
		if (o.getState() == Orange.State.Squeezed) {
			o.runProcess();
			out.addOrange(o);
		}
	}

	/**
	 * grab an orange from stack checks to make sure 100% that the orange fetched
	 * was worked on previously do work on the orange Increment orangesProcessed
	 * Throw newly processed Orange Juice away
	 */
	public void processOrange() {
		Orange o = in.getOrange();
		if (o.getState() == Orange.State.Bottled) {
			o.runProcess();
			Plant.orangesProcessed[0]++;

		}
	}
}
