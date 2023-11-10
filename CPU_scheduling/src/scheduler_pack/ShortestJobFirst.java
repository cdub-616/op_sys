package scheduler_pack;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class ShortestJobFirst {

    //fields
    ArrayList<String> output = new ArrayList<>();

    //constructors
    public ShortestJobFirst(String firstLine, int quantumLength, 
        int numberOfProcesses, LinkedList<Process> processes) {
    
        int time = 0, waitingTime = 0;
        PriorityQueue<Process> readyQueue = new PriorityQueue<>();
        boolean[] isFinished = new boolean[numberOfProcesses];
        int[] waitingTimeArr = new int[numberOfProcesses];
    
        //add first line to output file
        output.add(firstLine);
        
        
    
        //simulate scheduling and CPU
        
    }
    
    //methods
    public ArrayList<String> getOutput() {
        return output;
    }
}
