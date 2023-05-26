import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
public class Process {
	 PCB pcb;
	 String name;
	 List<String> instructions;
	 String id;

	 
	 public Process(String id) throws FileNotFoundException {
		 	this.id = id;
	        read(name);
	        this.pcb = new PCB(id);
	    }
	 
	 	public String getName() {
	        return name;
	    }
 
	    
	    public String toString() {
	    	String process ="";
	    	for(String ins: instructions) {
	    		process+= "ins:"+ins;
	    	}
	    	process+="var:null";
	    	process+="var:null";
	    	process+="var:null";
	    	
	    	return process;
	    	}
	    
	    //wrong arch. to be moved
	    //executes all instructions
	    //should only executes 2 ins
	   public void runAll() {
		   Parser parser = new Parser();
		   //request system call
		   //fetch process instructions of memory
		   List<String> instructions = new ArrayList<>();
		   for(int i =pcb.bounds[0]; i<pcb.bounds[1]; i++) {
			   String[] statement = MemoryManager.memory[i].split(":");
			   if(statement[0].equals("ins")) {
				   instructions.add(statement[1]);
			   }
		   }
		   for(String ins: instructions) {
			   parser.interpret(ins, id);
		   }
		   
		   
		   
		   
	   }
	   
	   
//	   public void executeInstruction() {
//		   //check for program counter
//		   //run next instruction
//		   int pcbIndex = MemoryManager.getPCBIndex(this.id+"");
//		   int pCounter = pcbIndex+2;
//		   int[] processBounds = MemoryManager.getProcessBounds(pid);
//		   String instruction = MemoryManager.memory[processBounds[0]+pCounter].split(":")[1];
//		   if(!MemoryManager.memory[processBounds[0]+pCounter].split(":")[0].equals("ins")) {
//			   MemoryManager.memory[pcbIndex + 3] = ProcessState.FINISHED.toString();
//			   return
//		   }
//		   Parser.interpret(instruction);
//	   }
	   
	   
	   public void read(String path) throws FileNotFoundException {
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
	   }
}
