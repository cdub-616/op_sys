package scheduler_pack;

import java.util.LinkedList;
import java.util.Queue;

public class RoundRobin {

    //fields
    LinkedList<String> output = new LinkedList<>();

    //constructors
    public RoundRobin(String firstLine, int quantumLength, 
       int numberOfProcesses, LinkedList<Process> processes) {
        
        int time = 0, waitingTime = 0;
        Queue<Process> readyQueue = new LinkedList<>();
        int[] waitingTimeArr = new int[numberOfProcesses];
        
        //subtract running time and arrival time from waiting times
        for (int  i = 0; i < numberOfProcesses; i++) {
            waitingTimeArr[i] -= processes.get(i).getCpuBurstTime();
            waitingTimeArr[i] -= processes.get(i).getArrivalTime();
        }
        
        //add first line to output file
        output.add(firstLine);
        
        //simulate scheduling and CPU
        while (!processes.isEmpty() || !readyQueue.isEmpty()) {
        
            //add arriving processes to queue
            while (!processes.isEmpty() && processes.peek().getArrivalTime() <= time) {
                readyQueue.add(processes.poll());
            }
            if (readyQueue.isEmpty()) {
            
                //no processes in queue, move to next arrival
                time = processes.peek().getArrivalTime();
            } else {
                
                //add process and time to output
                Process currentProcess = readyQueue.poll();
                String timeStr = Integer.toString(time);
                String procStr = Integer.toString(currentProcess.getProcessNumber());
                output.add(timeStr + " " + procStr);
                
                int currentProcessIndex = currentProcess.getProcessNumber() - 1;
                
                //make decisions
                if (currentProcess.getCpuBurstTime() <= quantumLength) {
                
                    //process finished
                    time += currentProcess.getCpuBurstTime();
                    
                    //add final time to waiting times
                    waitingTimeArr[currentProcessIndex] += time;
                } else {
                    time += quantumLength;
                   
                    //process not finished and check for new arrivals
                    while (!processes.isEmpty() && processes.peek().getArrivalTime() <= time) {
                        Process newProcess = processes.poll();
                        readyQueue.add(newProcess);
                    }
                    readyQueue.add(currentProcess);
                    int burstTime = currentProcess.getCpuBurstTime();
                    currentProcess.setCpuBurstTime(burstTime - quantumLength);
                }
            }
            
        }
        
        //calculate and write average waiting time
        for (int proc: waitingTimeArr) {
            waitingTime += proc;
        }
        double averageWaitingTime = (double) waitingTime / numberOfProcesses;
        String lastLine = String.format("AVG Waiting Time: %.2f", averageWaitingTime);
        output.add(lastLine);
    }

    //methods
    public LinkedList<String> getOutput() {
        return output;
    }
}