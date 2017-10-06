/**
 * Plant Class that is the backbone of the entire project. each plant creates
 * and manages workers while the main method controls the timeline
 * 
 * @author Kyle
 *
 */
public class Plant implements Runnable {
	// How long do we want to run the juice processing
	public static final long PROCESSING_TIME = 5 * 1000;
	public static AssemblyLine[] lines = new AssemblyLine[4];
	private static final int NUM_PLANTS = 2;

	/**
	 * Main Thread creates an array of plants Initializes The Assembly Line used for
	 * all threads of plant and workers Initializes plants and starts their run.
	 * Runs for PROCESSING_TIME seconds after the time, it stops the workers and
	 * plants then outputs the results Then exits the system to clean up.
	 */
	public static void main(String[] args) {
		// Startup the plants
		Plant[] plants = new Plant[NUM_PLANTS];

		//start up the assembly line
		for (int i = 0; i < 4; i++) {
			lines[i] = new AssemblyLine();
		}

		for (int i = 0; i < NUM_PLANTS; i++) {
			plants[i] = new Plant();
			plants[i].startPlant();
		}

		// Give the plants time to do work
		delay(PROCESSING_TIME, "Plant malfunction");

		// Stop the plant, and wait for it to shutdown
		for (Plant p : plants) {
			p.stopPlant();
		}
		for (Plant p : plants) {
			p.waitToStop();
		}


		// Summarize the results
		int totalProvided = 0;
		int totalProcessed = 0;
		int totalBottles = 0;
		int totalWasted = 0;
		for (Plant p : plants) {
			totalProvided += p.getProvidedOranges();
			totalProcessed += p.getProcessedOranges();
			totalBottles += p.getBottles();
			totalWasted += p.getWaste();
		}
		System.out.println("Total provided/processed = " + totalProvided + "/" + totalProcessed);
		System.out.println("Created " + totalBottles + ", wasted " + totalWasted + " oranges");
		
		// Fix by Natalie to exit program
		//System.exit(1);
	}

	/**
	 * delay method to let the program run for n seconds
	 * 
	 * @param time
	 * @param errMsg
	 */
	private static void delay(long time, String errMsg) {
		long sleepTime = Math.max(1, time);
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			System.err.println(errMsg);
		}
	}

	public final int ORANGES_PER_BOTTLE = 3;
	public Worker[] worker = new Worker[5];
	private final Thread thread;
	public static int orangesProvided;
	public static int[] orangesProcessed = new int[1];

	/**
	 * constructor for Plant Resets Variables orangesProvided and orangesProcessed
	 * and initializes a new thread for each plant
	 */
	Plant() {
		orangesProvided = 0;
		orangesProcessed[0] = 0;
		thread = new Thread(this, "Master");
	}

	/**
	 * start the plant thread ( go to run )
	 * 
	 */
	public void startPlant() {

		thread.start();
	}

	/**
	 * Stops the worker threads inside the plant thread Then waits for all worker
	 * threads to finish
	 */
	public void stopPlant() {
		// Stop all Workers
		for (int i = 0; i < 5; i++) {
			worker[i].stopWorker();
		}
		
	}

	/**
	 * Waits for all plant threads to stop
	 */
	public void waitToStop() {
		try {
			thread.join();
		} catch (InterruptedException e) {
			System.err.println(thread.getName() + " stop malfunction");
		}
		// Wait for workers to stop
				for (int i = 0; i < 5; i++) {
					worker[i].waitToStop();
				}

	}

	/**
	 * Creates, initializes, and starts all workers
	 */
	public void run() {

		worker[0] = new Worker(null, lines[0], 0);
		worker[0].startWorker();
		for (int i = 1; i < 4; i++) {
			worker[i] = new Worker(lines[i - 1], lines[i], i);
			worker[i].startWorker();
		}
		worker[4] = new Worker(lines[3], null, 4);
		worker[4].startWorker();
	}

	/**
	 * 
	 * @return providedOranges
	 */
	public int getProvidedOranges() {

		return orangesProvided;
	}

	/**
	 * get the processedOranges
	 * 
	 * @return
	 */
	public int getProcessedOranges() {
		return orangesProcessed[0];
	}

	/**
	 * Get the bottles
	 * 
	 * @return
	 */
	public int getBottles() {
		return orangesProcessed[0] / ORANGES_PER_BOTTLE;
	}

	/**
	 * Get the waste
	 * 
	 * @return
	 */
	public int getWaste() {
		return orangesProcessed[0] % ORANGES_PER_BOTTLE;
	}
}
