import java.util.Hashtable;
import java.util.List;


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
//TODO , empty space + finished process
//unhandled elses
public class MemoryManager {
    public static String[] memory = new String[40];
    static final int KERNEL_END = 15;
    static final int USER_START = 16;
    static final int USER_END = 39;

    static final int PCB_SIZE =4;
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
					if(memory[i+j] != null || memory[i+j].equals(ProcessState.FINISHED.toString()))/*check if state finished*/{
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
    }
    
    public static int[] insertProcess(List<String> instructions, PCB pcb) throws Exception {
    	//should set bounds for pcb
    	int processSize = calculateNeededSpace(instructions);
    	//first case: check user memory space
    	for(int i=USER_START; i<USER_END-processSize + 1; i++) {
    		for(int j =0; j<processSize; j++) {
    			if(j == processSize-1) {
    				if(memory[i+j] != null || memory[i+j].equals(ProcessState.FINISHED.toString()))/*check if state finished*/{
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
    			
    		
    		
	    	}else {
	    		//swapp
	    	}
    	} 
    	
			//write into disk
			String data = instructions.toString() + "var:null \n"+ "var:null \n"+ "var:null \n";
			SystemCall.WriteToDisk(pcb.pid+"", instructions.toString());
			//
			return new int [] {-1, -1};
		
    	
    	
    	
    	
    }
    public static int[] getProcessBounds(String pid) {

    	String boundString = "";
    	int[] bounds = new int[1];
		for(int i =0;i<16;i++) {
			if(MemoryManager.memory[i].equals(pid)) {
				boundString = MemoryManager.memory[i+1];
			}
		}
		String[] fromTo = boundString.split(",");
		bounds[0] = Integer.parseInt(fromTo[0]);
		bounds[1] = Integer.parseInt(fromTo[1]);
		return bounds;
    }
    
    public static int[] foundFinished() {
    	for(int i =0; i<KERNEL_END; i = i+4) {
    		if(memory[i+3].equals(ProcessState.FINISHED.toString())) {
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
        		
        		if(memory[i].equals(pid)) {
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

