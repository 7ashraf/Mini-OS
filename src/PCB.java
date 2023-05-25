import java.util.List;

public class PCB {
	private String name;
	 private int burstTime;
	 int programCounter;
	 public int id;
	 public int[] memoryBoundaries;
	 int counter;
	 ProcessState state;
	 //to be saved to mem: id, bouds, state, pCounter
	 
	 public PCB(int id, ProcessState state, int couter, int[] memoryBoundaries) {
		 this.id = id;
		 this.state = state;
		 this.counter = counter;
		 this.memoryBoundaries = memoryBoundaries;
	 }

}
