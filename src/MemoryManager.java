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
//TODO not enough space for process
public class MemoryManager {
    String[] memory = new String[40];
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
    
    public void insertProcess(Process process) {
    	int processSize = calculateNeededSpace(process);
    	//first case: check user memory space
    	for(int i=USER_START; i<USER_END-processSize + 1; i++) {
    		for(int j =0; j<processSize; j++) {
    			if(j == processSize-1) {
    				//found empty space
    				//insert instructions
    				int c=i;
    				for(String ins: process.instructions) {
    					
    					memory[c++]= new String("ins:"+ins);
    				}
    				memory[c] = new String("var:");
    				memory[c+1] = new String("var:");
    				memory[c+2] = new String("var:");

    			}
    		}
    	}
    	
    	
    }
    
    public int calculateNeededSpace(Process process) {
    	
    	return process.instructions.size() + 3;
    	
    }
    
}
