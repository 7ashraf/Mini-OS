import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class DiskManager {
//	public static final Path path = new Path();
	public static final DiskManager diskManager = new DiskManager();
	
	static final int KERNEL_END = 49;
    static final int USER_START = 50;
    static final int USER_END = 99;

    static final int PCB_SIZE =4;
	
//	List<String> lines = Files.readAllLines(path);
//	for(String line : lines) {
//	 if (line.trim().isEmpty())
//	  continue;
//	 else if ( //etc. you can process each line however you want.
//	}
	
	private DiskManager() {
		
	}
	public DiskManager getInstance() {
		return this.diskManager;
	}
	
	
//	public int[] writeInstruction(List<String> instructions, PCB pcb) {
//		//no need to check for space as it will be large
//    	int processSize = calculateNeededSpace(instructions);
//
//		for(int i=USER_START; i<USER_END-processSize + 1; i++) {
//    		for(int j =0; j<processSize; j++) {
//    			/*
//    			 * sliding window algorith chech for contiguos empty slots inside memory
//    			 */
//    			if(memory[i+j] != null)break;//increment i
//    			
//    			if(j == processSize-1) {
//    				
//    				//found empty space
//    				//insert instructions
//    				int c=i;
//    				for(String ins: instructions) {
//    					
//    					memory[c++]= new String("ins:"+ins);
//    				}
//    				memory[c] = new String("var:");
//    				memory[c++] = new String("var:");
//    				memory[c++] = new String("var:");
//    				//modify pcb boundaries after process insertions
//    				
//    				
//    				return new int[] {i, c};
//    				
//    			}
//    		}
//    	}
//		
		
		
		
//	}
public static int calculateNeededSpace(List<String> instructions) {
    	
    	return instructions.size() + 3;
    	
    }

}
