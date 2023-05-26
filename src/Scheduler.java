import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Scheduler {
	List<String> processIds;

	private static final Scheduler scheduler = new Scheduler();
	private Scheduler() {
		
		processIds = new ArrayList<String>();
	}
	public static Scheduler getInstance() {
		return scheduler;
	}
	/* For this project, each process executes 2 instructions in its time slice.
	Processes arrive in this order: Process 1 arrives at time 0, Process 2 arrives at
	time 1, and Process 3 arrives at time 4. */
	public final static int timeQuantum = 2;
	
	
	
	
	public void addProcess(String pid) {
		processIds.add(pid);
	}
	
	

    public  void sceduleAndExecute(Queue<String> processes) throws Exception {
        while (!processes.isEmpty()) {
        	//execute all process 2 times 
        	//loop untill queue is empty
        	
        	String p1 = processes.poll();
        	String p2 = processes.poll();
        	String p3 = processes.poll();
        	
        	//execute two times
        	for(int i=0; i<2; i++) {
        		System.out.println("Executing process: " + p1);
                executeInstruction(p1);
                if(!MemoryManager.isFinished(p1)) {
                	processes.add(p1);
                }
                
                System.out.println("Executing process: " + p2);
                executeInstruction(p2);
                if(!MemoryManager.isFinished(p2)) {
                	processes.add(p2);
                }
                
                System.out.println("Executing process: " + p3);
                executeInstruction(p3);
                if(!MemoryManager.isFinished(p3)) {
                	processes.add(p3);
                }
        	}
        }
    }
    
    public static void executeInstruction(String pid) throws Exception {
		   //check for program counter
		   //run next instruction
		   int pcbIndex = MemoryManager.getPCBIndex(pid);
		   int pCounter = pcbIndex+2;
		   int[] processBounds = MemoryManager.getProcessBounds(pid);
		   String instruction;
		   if(processBounds[0]>49) {
			   MemoryManager.swapDiskToProcess(OS.pcbById.get(pid));
		   }
		   
		   instruction = MemoryManager.memory[processBounds[0]+pCounter].split(":")[1];
		   
		   if(!MemoryManager.memory[processBounds[0]+pCounter].split(":")[0].equals("ins")) {
			   MemoryManager.memory[pcbIndex + 3] = ProcessState.FINISHED.toString();
			   return;
			   
		   }
		   
		   Parser.interpret(instruction, pid);
		   //increment pcounter
		   MemoryManager.memory[pCounter] = ++pCounter+"";
	   }



}
