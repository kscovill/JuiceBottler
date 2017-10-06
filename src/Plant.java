public class Plant implements Runnable {
	// How long do we want to run the juice processing
	public static final long PROCESSING_TIME = 5 * 1000;
	public static AssemblyLine[] lines = new AssemblyLine[4];
	private static final int NUM_PLANTS = 2;

	/**
	 * Main Thread
	 * @param args
	 */
	public static void main(String[] args) {
		// Startup the plants
		Plant[] plants = new Plant[NUM_PLANTS];
		
		for (int i = 0; i < 4; i++) {
			lines[i] = new AssemblyLine();
		}
		
		for (int i = 0; i < NUM_PLANTS; i++) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
		
		for(int i = 0; i < 5; i++) {
			worker[i].waitToStop();
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
		System.exit(1);
	}

	private static void delay(long time, String errMsg) {
		long sleepTime = Math.max(1, time);
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			System.err.println(errMsg);
		}
	}

	public final int ORANGES_PER_BOTTLE = 3;
	public static Worker[] worker = new Worker[5];
	private final Thread thread;
	public static int[] orangesProvided = new int[1];
	public static int[] orangesProcessed = new int[1];


	Plant() {
		orangesProvided[0] = 0;
		orangesProcessed[0] = 0;
		thread = new Thread(this, "Master");
	}

	public void startPlant() {
		
		thread.start();
	}

	public void stopPlant() {
		
		for(int i = 0; i < 5; i++) {
			worker[i].stopWorker();
		}
		
		for(int i = 0; i < 5; i++) {
			worker[i].waitToStop();
		}
		
	}

	public void waitToStop() {
		try {
			thread.join();
			//System.out.println("Plant has died( should be 2 )");
		} catch (InterruptedException e) {
			System.err.println(thread.getName() + " stop malfunction");
		}
	}

	public void run() {
		//System.out.println("The " + Thread.currentThread().getName() + " has begun to buy slaves");

		worker[0] = new Worker(null, lines[0], 0);
		worker[0].startWorker();
		worker[0].thread.start();
		for (int i = 1; i < 4; i++) {
			worker[i] = new Worker(lines[i - 1], lines[i], i);
			worker[i].startWorker();
			worker[i].thread.start();
		}
		worker[4] = new Worker(lines[3], null, 4);
		worker[4].startWorker();
		worker[4].thread.start();
	}

	public int getProvidedOranges() {

		return orangesProvided[0];
	}

	public int getProcessedOranges() {
		return orangesProcessed[0];
	}

	public int getBottles() {
		return orangesProcessed[0] / ORANGES_PER_BOTTLE;
	}

	public int getWaste() {
		return orangesProcessed[0] % ORANGES_PER_BOTTLE;
	}
}
