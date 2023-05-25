import java.util.concurrent.Semaphore;

public class Mutex {
	private Semaphore semaphore;
	
	public Mutex() {
        semaphore = new Semaphore(1);
        
    }

	public void semWait() {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            // Handle the interrupted exception
            e.printStackTrace();
        }
    }

    public void semSignal() {
        semaphore.release();
    }

}
