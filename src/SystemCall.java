import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class SystemCall {
	private SystemCallTypes type;
	
	
	public static String ReadFromDisk(String name) throws Exception {
		File inputFile = new File("Disk.txt");
		boolean endFile = false;
		boolean endProc = false;
		String ret = "";
		String currLine = "";
		File newFile = new File("Temp.txt");
		FileWriter writer = new FileWriter("Temp.txt",true);
		//if read is not busy else add to queue
		Scanner sc = new Scanner(inputFile);
		while(sc.hasNextLine()) {
			currLine = sc.nextLine();
			if(currLine.equals(name)) {
				endFile = true;
				while(!(currLine.equals("$")) && sc.hasNextLine()) {
					ret += currLine + "\n";
					currLine = sc.nextLine();
				}
			}
			if(sc.hasNextLine()&& endFile==true) {
				currLine = sc.nextLine();
				endFile = false;
				
			}
			else {
				if(!sc.hasNextLine()&& currLine.equals("$") ) {
					currLine = "";
				}
			}
				
				writer.write(currLine + "\n");
			
			}
		inputFile.delete();
		
		newFile.renameTo(inputFile);
		
		sc.close();
		writer.close();
		return ret;
	}
	public static void WriteToDisk(String name, String data) throws Exception {
		//if write is not busy else add to queue
		FileWriter writer = new FileWriter("Disk.txt",true);
        writer.write(name + "\n");
        writer.write(data + "\n" + "$" + "\n");
        writer.close();
	}
	public void printScreen(String data) {
		//if printer not busy else add to queue
		System.out.println(data);
	}
	public String inputHandler(String inputRequest,String inputType) {
		//if input is not busy else add to queue
		Scanner sc = new Scanner(System.in);
		System.out.println(inputRequest);
		String ret = sc.nextLine();
		return ret;
		
	}
	public String ReadFromMem(int startBoundry,int endBoundry) {
		String ret = "";
		for(int i =startBoundry;i<endBoundry;i++) {
			//if instruction
			ret += MemoryManager.memory[i].split(":");
		}
		
		return ret;
	}
	
	public static String getBoundries(String pid) {
		String bound = "";
		for(int i =0;i<16;i++) {
			if(MemoryManager.memory[i].equals(pid)) {
				bound = MemoryManager.memory[i+1];
			}
			
			
		}
		return bound;
	}
	
	public static void WriteToMem(String data,String pid) {
		
		String bound = getBoundries(pid);
		String[] fromTo = bound.split(",");
		int from = Integer.parseInt(fromTo[0]);
		int to = Integer.parseInt(fromTo[1]);
		
		for(int i = from; i<to;i++) {
			
			if(MemoryManager.memory[i].contains("var")) {
				String[] check = MemoryManager.memory[i].split(":");
				if(check[1].equals("null")) {
					MemoryManager.memory[i] = "var:" + data;
				}
			}
			
		}
	
		
		
	}
	
	public static void main(String[] args) throws Exception {
		String name = "proc2";
		String data = "1line";
		ReadFromDisk(name);
		//WriteToDisk(name,data);
	}
	
}