import java.util.Hashtable;


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
public class MemoryManager {
    public static String[] memory = new String[40];
    static final int KERNEL_END = 15;
    static final int USER_START = 16;
    static final int USER_END = 39;

    static final int PCB_SIZE =4;
    //0-> 15 kernel
    //16-39 user
    private MemoryManager() {
    	
    }
    
    public MemoryManager getMemoryMangerInstance() {
    	return this;
    }
    
    public void insertPCB(PCB pcb) {
    
			for(int i=0; i<KERNEL_END-4 +1 ; i++) {
				for(int j =0; j<PCB_SIZE; j++) {
					if(memory[i+j] != null || memory[i+j].equals(ProcessState.FINISHED.toString()))/*check if state finished*/{
						if (j== PCB_SIZE -1) {
							
							memory[i] = new String(pcb.id+"");
							memory[i+1] = new String(pcb.memoryBoundaries[0]+","+ pcb.memoryBoundaries[1]);
							memory[i+2] = new String(pcb.counter+"");
							memory[i+3] = new String(pcb.state.toString());
							return;
							
						}
					}
					
				}
		}
    }
    
    public void insertProcess(Process process) throws Exception {
    	int processSize = calculateNeededSpace(process);
    	//first case: check user memory space
    	for(int i=USER_START; i<USER_END-processSize + 1; i++) {
    		for(int j =0; j<processSize; j++) {
    			if(j == processSize-1) {
    				if(memory[i+j] != null || memory[i+j].equals(ProcessState.FINISHED.toString()))/*check if state finished*/{
	    				//found empty space
	    				//insert instructions
	    				int c=i;
	    				for(String ins: process.instructions) {
	    					
	    					memory[c++]= new String("ins:"+ins);
	    				}
	    				memory[c] = new String("var:");
	    				memory[c+1] = new String("var:");
	    				memory[c+2] = new String("var:");
	    				return;
    				}
    			}
    		}
    	}
    	//case2: check for finished process then check again for available space
    	if(foundFinished()) {
    		int [] bounds = getBounds(process.id+"");
    		//best case
    		if(calculateNeededSpace(process) <= calculateCurrentSpace(process)) {
    			//overwrite
    			int start = bounds[0];
    			int c=start;
				for(String ins: process.instructions) {
					
					memory[c++]= new String("ins:"+ins);
				}
				memory[c] = new String("var:");
				memory[c+1] = new String("var:");
				memory[c+2] = new String("var:");
				//nullify rest of finished process words
				if(c<bounds[1]) {
					for(int i =c; i<bounds[1]; i++) {
						memory[i] = null;
					}
				}
				return;
    			
    		}else {
    			//write into disk
    			SystemCall.WriteToDisk(process.id+"", process.toString());
    			return;
    		}
    		
    	}
    	
    	
    	
    	
    }
    public int[] getBounds(String pid) {
    	
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
    
    public boolean foundFinished() {
    	for(int i =0; i<KERNEL_END; i = i+4) {
    		if(memory[i+3].equals(ProcessState.FINISHED.toString())) {
    			//found finished process
    			return true;
    		}
    		
    		
    	}
    	return false;
    }
    
    public int calculateNeededSpace(Process process) {
    	
    	return process.instructions.size() + 3;
    	
    }
    
    public int calculateCurrentSpace(Process process) {
    	int[] bounds = getBounds(process.id+"");
    	return bounds[1]-bounds[0];
    	
    }
    
}

