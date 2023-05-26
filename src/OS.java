import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Hashtable;
import java.util.LinkedList;


//TODO system calls, scheduler algorithm
//TODO deprecate process class
//TODO queues
//TODO mutex
//TODO swap mem to disk
public class OS {
	static Mutex fileMutex;
	static Mutex inputMutex;
	static Mutex outputMutex;
	static List<String> processes; 
	static List<PCB> pcbs;
	static MemoryManager memoryManager;
	static Scheduler scheduler;
	static Queue<Process> readyQ;
	static Queue<Process> blockedQ;

    
	public static void main(String[] args) throws Exception {
		fileMutex = new FileMutex();
		inputMutex = new InputMutex();
		outputMutex = new OutputMutex();
		processes = new ArrayList<>();
		pcbs = new ArrayList<>();

		memoryManager = MemoryManager.getInstance();
		scheduler = Scheduler.getInstance();
		
		
		readPrograms();
		
		readyQ = new LinkedList<>();
		blockedQ = new LinkedList<>();
		//scheduler schedules inside the queue and runs them according to the robin algo
		
	}
	
	//archeticture to be corretcetd
	//aka to be moved
	public static void readPrograms() throws Exception {
		String path1 = new String ("Program_1.txt");
		PCB p1 = new PCB("p1");
		
		String path2 = new String ("Program_2.txt");
		PCB p2 = new PCB("p2");

		
		String path3 = new String ("Program_3.txt");
		PCB p3 = new PCB("p3");
		
		readProgram(path1);
		readProgram(path2);
		readProgram(path3);
		
			
	   }
	
	//archeticture to be modified
	public static  void readProgram(String path) throws Exception {
		List<String> instructions = new ArrayList<>();
		File file = new File(path);
		Scanner sc;
		try {
			sc = new Scanner(file);
		}catch(Exception e){
			throw new FileNotFoundException();
			
		}
		while(sc.hasNextLine()) {
			instructions.add(sc.nextLine());
		}
		//write process to memory
		PCB pcb = new PCB("p1");
		int[] bounds = MemoryManager.insertProcess(instructions, pcb);

		
		processes.add(pcb.pid);
		pcbs.add(pcb);
		
		//write pcb to memory
		pcb.setBounds(bounds);
		memoryManager.insertPCB(pcb);

		
		
	}
	
	

	
	
	
	

}
