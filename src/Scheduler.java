import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Scheduler {
	/* For this project, each process executes 2 instructions in its time slice.
	Processes arrive in this order: Process 1 arrives at time 0, Process 2 arrives at
	time 1, and Process 3 arrives at time 4. */
	public final static int timeQuantum = 2;
	Queue<Process> readyQ;
	Queue<Process> blockedQ;
	List<String> processNames;
	
	public Scheduler() {
		readyQ = new LinkedList<>();
		blockedQ = new LinkedList<>();
		processNames = new ArrayList<String>();
		
	}
	
	public void addProcess(String processName) {
		processNames.add(processName);
	}
	
	public Process getProcess(String processName) {
		return OS.getProcessByName(processName);
	}
	

    public  void scheduleProcesses(Queue<Process> processes) {
        while (!processes.isEmpty()) {
        	//get first process
            Process currentProcess = processes.poll();
            //execute process
            System.out.println("Executing process: " + currentProcess.getName());
            currentProcess.run();
            //currentProcess.executeTwoInstructions
            
            //check if process completed or not
            if (currentProcess.getBurstTime() > timeQuantum) {
                // Process still needs more time
                currentProcess.setBurstTime(currentProcess.getBurstTime() - timeQuantum);
                System.out.println("Time quantum expired. Process " + currentProcess.getName() + " needs more time.");

                // Add the process back to the queue
                processes.offer(currentProcess);
            } else {
                // Process has finished execution
                System.out.println("Process " + currentProcess.getName() + " completed.");
            }
        }
    }



}
