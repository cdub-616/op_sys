package scheduler_pack;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Priority_noPreemption {

    //fields
    LinkedList<String> output = new LinkedList<>();

    //constructors
    public Priority_noPreemption(String firstLine, int quantumLength, 
            int numberOfProcesses, LinkedList<Process> processes) {
    
        int time = 0, waitingTime = 0;
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(new ProcessComparator());
        Iterator<Process> iterator = processes.iterator();
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
        
            //reinitialize iterator at each iteration because decreasing list size
            iterator = processes.iterator(); 
            while (iterator.hasNext()) {
                Process process = iterator.next();
                if (process.getArrivalTime() <= time) {
                    readyQueue.add(process);
                    iterator.remove();
                }
            }
            if (!readyQueue.isEmpty()) {
                Process currentProcess = readyQueue.poll();
            
                //add process and time to output
                String timeStr = Integer.toString(time);
                String procStr = Integer.toString(currentProcess.getProcessNumber());
                output.add(timeStr + " " + procStr);
                time += currentProcess.getCpuBurstTime();
            
                //add final time to waiting times
                int currentProcessIndex = currentProcess.getProcessNumber() - 1;
                waitingTimeArr[currentProcessIndex] += time;
            } else {
                time = processes.getFirst().getArrivalTime();
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
    static class ProcessComparator implements Comparator<Process> {
        @Override
        public int compare(Process p1, Process p2) {
            if (p1.priority != p2.priority) {
                return p1.priority - p2.priority;
            } else {
                return p1.processNumber - p2.processNumber;
            }
        }
    }
    public LinkedList<String> getOutput() {
        return output;
    }
}