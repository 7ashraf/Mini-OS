import java.util.List;

public class PCB {
	private String name;
	 private int burstTime;
	 int programCounter;
	 public String pid;
	 public int[] bounds;
	 int counter;
	 ProcessState state;
	 //to be saved to mem: id, bouds, state, pCounter
	 
	 public PCB(String pid) {
		 this.pid = pid;
		 this.state = ProcessState.BLOCKED;
		 this.counter =0;
		 this.bounds = new int[] {-1, -1};
	 }
	 
	 public void setBounds(int[] bounds) {
		 this.bounds = bounds;
	 }

}
