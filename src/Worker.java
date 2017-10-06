
public class Worker implements Runnable {
	public AssemblyLine in;
	public AssemblyLine out;
	public int role;
	public boolean timeToWork = false;

	public Worker(AssemblyLine in, AssemblyLine out, int role) {
		this();
		this.role = role;
		this.in = in;
		this.out = out;
	}

	public final Thread thread;

	Worker() {
		thread = new Thread(this, "slave");
	}

	public void startWorker() {
		timeToWork = true;
		//System.out.println("This has started( should be 10 )");
		//thread.start();
	}

	public void stopWorker() {
		//System.out.println("This has stopped( should be 10 )");
		timeToWork = false;
	}
	
	public void waitToStop() {
		try {
			thread.join();
			//System.out.println("This has died( should be 10 )");
		} catch (InterruptedException e) {
			System.err.println(thread.getName() + " stop malfunction");
		}
	}

	public void run() {
		//System.out.println("The " + Thread.currentThread().getName() + " is Processing oranges");
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

	public void fetchOrange() {
		Orange o = new Orange();
		Plant.orangesProvided[0]++;
		out.addOrange(o);
	}

	public void peelOrange() {
		Orange o = in.getOrange();
		if (o.getState() == Orange.State.Fetched) {
			o.runProcess();
			out.addOrange(o);
		}
	}

	public void squeezeOrange() {
		Orange o = in.getOrange();
		if (o.getState() == Orange.State.Peeled) {
			o.runProcess();
			out.addOrange(o);
		}
	}

	public void bottleOrange() {
		Orange o = in.getOrange();
		if (o.getState() == Orange.State.Squeezed) {
			o.runProcess();
			out.addOrange(o);
		}
	}

	public void processOrange() {
		Orange o = in.getOrange();
		if (o.getState() == Orange.State.Bottled) {
			o.runProcess();
			Plant.orangesProcessed[0]++;

		}
	}
}
