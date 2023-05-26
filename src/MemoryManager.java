import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;


/*
 * PCB memory structure:
 * 
 * [0]:pid
 * [1]:int,int
 * [2]counter
 * [3]:state
 * 
 */

/*
 * Process memory Structure
 * ins:<instruction>
 * var:<name>:<data>
 */

/*
 * Disk structure
 * sart index = 40
 * 
 */
//TODO , empty space + finished process
//unhandled elses
public class MemoryManager {
    public static String[] memory = new String[40];
    static final int KERNEL_END = 15;
    static final int USER_START = 16;
    static final int USER_END = 39;

    static final int PCB_SIZE =4;
    static final int DISK_START = 40;
    //0-> 15 kernel
    //16-39 user
    public static final MemoryManager memoryManager = new MemoryManager();
    private MemoryManager() {
    	
    }
    
    public static MemoryManager getInstance() {
    	return memoryManager;
    }
    
    public void insertPCB(PCB pcb) {
    
			for(int i=0; i<KERNEL_END-4 +1 ; i++) {
				for(int j =0; j<PCB_SIZE; j++) {
					/*
	    			 * sliding window algorith chech for contiguos empty slots inside memory
	    			 */
					if(memory[i+j] != null)break;//increment i
					
					if (j== PCB_SIZE -1) {
						
						memory[i] = new String(pcb.pid+"");
						memory[i+1] = new String(pcb.bounds[0]+","+ pcb.bounds[1]);
						memory[i+2] = new String(pcb.counter+"");
						memory[i+3] = new String(pcb.state.toString());
						return;
						
						
					}
					
				}
		}
    }
    
    public static int[] insertProcess(List<String> instructions, PCB pcb) throws Exception {
    	//should set bounds for pcb
    	int processSize = calculateNeededSpace(instructions);
    	//first case: check user memory space
    	for(int i=USER_START; i<USER_END-processSize + 1; i++) {
    		for(int j =0; j<processSize; j++) {
    			/*
    			 * sliding window algorith chech for contiguos empty slots inside memory
    			 */
    			if(memory[i+j] != null)break;//increment i
    			
    			if(j == processSize-1) {
    				
    				//found empty space
    				//insert instructions
    				int c=i;
    				for(String ins: instructions) {
    					
    					memory[c++]= new String("ins:"+ins);
    				}
    				memory[c] = new String("var:");
    				memory[c++] = new String("var:");
    				memory[c++] = new String("var:");
    				//modify pcb boundaries after process insertions
    				
    				
    				return new int[] {i, c};
    				
    			}
    		}
    	}
    	//case2: check for finished process then check again for available space
    	if(foundFinished()[0] != -1 ) {
    		//get bounds of finished process
    		int [] finishedBounds = foundFinished();
    		//best case
    		if(calculateNeededSpace(instructions) <= calculateCurrentSpace(pcb.pid)) {
    			//overwrite
    			int start = finishedBounds[0];
    			int c=start;
				for(String ins: instructions) {
					
					memory[c++]= new String("ins:"+ins);
				}
				memory[c] = new String("var:");
				memory[c+1] = new String("var:");
				memory[c+2] = new String("var:");
				//nullify rest of finished process words
				if(c<finishedBounds[1]) {
					for(int i =c; i<finishedBounds[1]; i++) {
						memory[i] = null;
					}
				}
				
				return new int[] {finishedBounds[0], c};
    			
    		
    		
	    	}
    	} else {
    		//swap memory to disk
    		//get pcb and instructions to swap
    		//wrong archeticture to be modified
    		PCB pInMemory = OS.pcbs.get(0);
    		List<String> insInMemory = getProcessInstructions(pInMemory.pid);
    		return swapProcessToDisk(pInMemory, insInMemory, instructions, pcb);
    	}
    	
			//write into disk
			return new int [] {-1, -1};
		
    	
    	
    	
    	
    }
    
    // no need to overwrite the pcb
    public static int[] swapProcessToDisk(PCB pInMemory, List<String> insInMemory, List<String> insToMemory, PCB pToMemory)throws Exception {
    	int[] inMemoryBounds = pInMemory.bounds;
    	int inMemeoryPcbIndex = getPCBIndex(pInMemory.pid);
    	int[] toMemoryBounds = new int[] {-1, -1};
//    	//write the pcb to disk
//    	for(int i =inMemeoryPcbIndex; i<PCB_SIZE; i++) {
//    			//if write is not busy else add to queue
//			FileWriter writer = new FileWriter("Disk.txt",true);
//	        writer.write(memory[i] + "\n");
//	        writer.close();
//    	}
    	//write instuctions on disk
    	for(int i =inMemoryBounds[0]; i<inMemoryBounds[1]; i++) {
			//if write is not busy else add to queue
		FileWriter writer = new FileWriter("Disk.txt",false);
        writer.write(memory[i] + "\n");
        writer.close();
    	}
    	
    	//overwrite instruction
    	int start = inMemoryBounds[0];
		int c=start;
		for(String ins: insToMemory) {
			
			memory[c++]= new String("ins:"+ins);
		}
		memory[c++] = new String("var:");
		memory[c++] = new String("var:");
		memory[c++] = new String("var:");
		//nullify rest of finished process words
		if(c<inMemoryBounds[1]) {
			for(int i =c; i<inMemoryBounds[1]; i++) {
				memory[i] = null;
			}
		}
		toMemoryBounds = new int[] {inMemoryBounds[0], c};
		
		//overwrite pcb
//		memory[inMemeoryPcbIndex] = new String(pToMemory+"");
//		memory[inMemeoryPcbIndex+1] = new String(toMemoryBounds[0]+","+ toMemoryBounds[1]);
//		memory[inMemeoryPcbIndex+2] = new String(0+"");
//		memory[inMemeoryPcbIndex+3] = new String(ProcessState.READY.toString());
		
		//modidy swapped pcb bounds
		pInMemory.bounds[0] = DISK_START;
		pInMemory.bounds[1] = DISK_START + (-USER_START + pInMemory.bounds[1]);
		String boundsS = pInMemory.bounds[0] + "," + pInMemory.bounds[1];
		memory[1] = boundsS;
		return toMemoryBounds = new int[] {inMemoryBounds[0], c};


    	
    	
    	
    	
    	
    }
    public static void swapDiskToProcess(PCB pInDisk)throws Exception {
    	PCB pInMemory = OS.pcbs.get(0);
    	int[] inMemoryBounds = pInMemory.bounds;
    	int inMemeoryPcbIndex = getPCBIndex(pInMemory.pid);
    	int[] toMemoryBounds = new int[] {-1, -1};

    	
		List<String> insInMemory = getProcessInstructions(pInMemory.pid);
    	List<String> tmp = insInMemory;
    	//overwrite instruction on memory
    	List<String> insDisk = loadInstructionsFromDisk(inMemoryBounds);
    	
    	//write ins on memory from disk
    	//need bounds
    	for(int i =inMemoryBounds[0]; i<insDisk.size(); i++) {
    		memory[i] = insDisk.get(i);
    	}
    	//modify pcb of process was on disk
    	int inDiskPcbIndex = getPCBIndex(pInDisk.pid);
    	String boundsS = inMemoryBounds[0] + "," + insDisk.size();
    	pInDisk.bounds = new int[] {Integer.parseInt(boundsS.split(",")[0]), Integer.parseInt(boundsS.split(",")[1])};
    	memory[inDiskPcbIndex+1] = boundsS;
    	
    	
    	
    	//write instuctions on disk from memory
    	for(int i =0; i<tmp.size(); i++) {
			//if write is not busy else add to queue
		FileWriter writer = new FileWriter("Disk.txt",false);
        writer.write(tmp.get(i) + "\n");
        writer.close();
    	}
    	//modify pcb was in memory to new bounds on disk
    	pInMemory.bounds[0] = DISK_START;
		pInMemory.bounds[1] = DISK_START + (-USER_START + pInMemory.bounds[1]);
		boundsS = pInMemory.bounds[0] + "," + pInMemory.bounds[1];
		memory[inMemeoryPcbIndex+1] = boundsS;
    	
 
    	
    	
    	
    	
    }
    private static List<String> loadInstructionsFromDisk(int[] bounds) {
    	List<String> ins = new ArrayList<>();
    	try {
    	      File disk = new File("Disk.txt");
    	      Scanner myReader = new Scanner(disk);
    	      while (myReader.hasNextLine()) {
    	        String data = myReader.nextLine();
    	        ins.add(data);
    	      }
    	      myReader.close();
    	    } catch (FileNotFoundException e) {
    	      System.out.println("An error occurred.");
    	      e.printStackTrace();
    	    }
		return ins;
	}

	public static List<String> getProcessInstructions(String pid){
    	List<String> instructions = new ArrayList<>();
    	int[] bounds = getProcessBounds(pid);
    	
    	for(int i =bounds[0]; i<bounds[1]; i++) {
    		instructions.add(memory[i]);
    	}
    	return instructions;
    }
    public static int[] getProcessBounds(String pid) {

    	String boundString = "";
    	int[] bounds = new int[] {-1, -1};
		for(int i =0;i<KERNEL_END;i++) {
			if(MemoryManager.memory[i].equals(pid)) {
				boundString = MemoryManager.memory[i+1];
				break;
			}
		}
		String[] fromTo = boundString.split(",");
		bounds[0] = Integer.parseInt(fromTo[0]);
		bounds[1] = Integer.parseInt(fromTo[1]);
		return bounds;
    }
    
    public static int[] foundFinished() {
    	for(int i =0; i<KERNEL_END; i = i+4) {
    		if(memory[i+3] != null&&memory[i+3].equals(ProcessState.FINISHED.toString())) {
    			//found finished process
    			//return its bounds
    			int[] bounds = new int[] {-1, -1};
    			String[] fromTo = memory[1].split(",");
    			bounds[0] = Integer.parseInt(fromTo[0]);
    			bounds[1] = Integer.parseInt(fromTo[1]);
    			
    			
    			return bounds;
    		}
    		
    		
    	}
    	return new int[] {-1, -1};
    }
    	public static boolean isFinished(String pid) {
        	for(int i =0; i<KERNEL_END; i = i+4) {
        		
        		if(memory[i] != null && memory[i].equals(pid)) {
		    		if(memory[i+3].equals(ProcessState.FINISHED.toString())) {
		    			//found finished process
		    			return true;
		    		}
        		}
        	}
    	return false;
    }
    
    public static int calculateNeededSpace(List<String> instructions) {
    	
    	return instructions.size() + 3;
    	
    }
    
    public static int calculateCurrentSpace(String pid) {
    	int[] bounds = getProcessBounds(pid);
    	return bounds[1]-bounds[0];
    	
    }
    public static int getPCBIndex(String pid) {
    	for(int i =0; i<KERNEL_END; i++) {
	    		if(memory[i].equals(pid)) {
    			return i;
    		}
    	}
    	return -1;
    }
    
    
}

