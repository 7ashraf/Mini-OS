import java.io.File;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Parser {

	

	
	
	
	
	public static void main(String[] args) throws Exception {
		String proc1 = "";
		String proc2 = "";
		String proc3 = "";
		SystemCall call = new SystemCall();
		File file1 = new File("Program_1.txt");
		File file2 = new File("Program_2.txt");
		File file3 = new File("Program_3.txt");
		//Reading 1st program and assigning it to process 1
		Scanner sc1 = new Scanner(file1);
		
		
		while(sc1.hasNextLine()) {
			proc1 += sc1.nextLine() + "\n";
		}
		sc1.close();
		//Reading 2nd program and assigning it to process 2
		Scanner sc2 = new Scanner(file2);
		
		
		while(sc2.hasNextLine()) {
			proc2 += sc2.nextLine() + "\n";
		}
		sc2.close();
		//Reading 3rd program and assigning it to process 3
		Scanner sc3 = new Scanner(file1);
		
		
		while(sc3.hasNextLine()) {
			proc3 += sc3.nextLine() + "\n";
		}
		sc3.close();
	}
	


//	    public static void main(String[] args) {
//	        interpret("print Hello, World!");
//	        interpret("assign x 10");
//	        interpret("assign y input");
//	        interpret("print x");
//	        interpret("print y");
//	        interpret("writeFile output.txt Hello, World!");
//	        interpret("readFile output.txt");
//	        interpret("printFromTo 1 5");
//	    }


	    public static void interpret(String statement) {
	        String[] tokens = statement.split(" ");

	        if (tokens[0].equals("print")) {
	            print(tokens[1]);
	        } else if (tokens[0].equals("assign")) {
	            assign(tokens[1], tokens[2]);
	        } else if (tokens[0].equals("writeFile")) {
	            writeFile(tokens[1], tokens[2]);
	        } else if (tokens[0].equals("readFile")) {
	            readFile(tokens[1]);
	        } else if (tokens[0].equals("printFromTo")) {
	            printFromTo(tokens[1], tokens[2]);
	        }else if(tokens[0].equals("semWait")) {
	        	semWait(tokens[1]);
	        }else if(tokens[0].equals("semSignal")) {
	        	semSignal(tokens[1]);
	        }
	    }

	    



		public static void print(String value) {
	        System.out.println(value);
	    }

	    public static void assign(String variable, String value) {
	        if (value.equals("input")) {
	            System.out.println("Please enter a value:");
	            Scanner scanner = new Scanner(System.in);
	            String inputValue = scanner.nextLine();
	            //system call to rewrite to memory
	            SystemCall.WriteToMem(variable, value);
	        } else {
	          //  variables.put(variable, value);
	        }
	    }

	    public static void writeFile(String filename, String data) {
	        try {
	            FileWriter writer = new FileWriter(filename);
	            writer.write(data);
	            writer.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    public static void readFile(String filename) {
	        try {
	            File file = new File(filename);
	            Scanner scanner = new Scanner(file);
	            while (scanner.hasNextLine()) {
	                String line = scanner.nextLine();
	                System.out.println(line);
	            }
	            scanner.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    public static void printFromTo(String start, String end) {
	        int startNum = Integer.parseInt(start);
	        int endNum = Integer.parseInt(end);
	        for (int i = startNum; i <= endNum; i++) {
	            System.out.print(i + " ");
	        }
	        System.out.println();
	    }
	    public static void semWait(String mutexType) {
	    	Mutex mutex;
	    	if(mutexType.equals("userInput")) {
		    	OS.inputMutex.semWait();
	    	}else if(mutexType.equals("file")) {
		    	OS.fileMutex.semWait();
	    	}else if(mutexType.equals("userOutput")) {
		    	OS.outputMutex.semWait();
	    	}else {
	    		mutex = new Mutex();
	    	}
	    	
	    	
	    }
	    private static void semSignal(String mutexType) {
	    	Mutex mutex;
	    	if(mutexType.equals("userInput")) {
	    		mutex = new InputMutex();
	    	}else if(mutexType.equals("file")) {
	    		mutex = new FileMutex();
	    	}else if(mutexType.equals("userOutput")) {
	    		mutex = new OutputMutex();
	    	}else {
	    		mutex = new Mutex();
	    	}
	    	mutex.semSignal();	    	
	    }

}
