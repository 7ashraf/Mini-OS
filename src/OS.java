import java.util.ArrayList;
import java.util.List;
import java.util.Hashtable;


//TODO system calls, scheduler algorithm
public class OS {
	static Mutex fileMutex;
	static Mutex inputMutex;
	static Mutex outputMutex;
	static List<Process> processes; 
    static Hashtable<String, Process> pro;
    public String[] memory= new String[40];//memory to be

    
	public static void main(String[] args) {
		fileMutex = new FileMutex();
		inputMutex = new InputMutex();
		outputMutex = new OutputMutex();
		processes = new ArrayList<>();
		pro = new Hashtable<>();
		
		//
		
		
	}
	
	public boolean requestService() {
		//checks mutex for given type, if avail return true, if not return false
		return false;
	}
	
	public static Process getProcessByName(String processName) {
		return pro.get(processName);
	}

}
