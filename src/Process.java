import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
public class Process {
	 PCB pcb;
	 String name;
	 List<String> instructions;
	 int id;

	 
	 public Process(int id) throws FileNotFoundException {
		 	this.id = id;
	        read(name);
	    }
	 
	 	public String getName() {
	        return name;
	    }

	    

	    
	    
	    public String toString() {
	    	return "Process Id: " + this.id + " Process Name: " + this.name;	
	    }
	    
	    public void requestSystemCall(SystemCallTypes type) {
			//checks mutex for given type, if avail return true, if not return false

	    }
	    
	   public void run() {
		   Parser parser = new Parser();
		   //request system call
		   
		   for(int i =0; i<instructions.size(); i++) {
			   parser.interpret(instructions.get(i));
		   }
		   
	   }
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
