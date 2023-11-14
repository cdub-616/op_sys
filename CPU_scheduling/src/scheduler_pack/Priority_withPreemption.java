package scheduler_pack;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Priority_withPreemption {

    //fields
    LinkedList<String> output = new LinkedList<>();

    //constructors
    public Priority_withPreemption(String firstLine, int numberOfProcesses,
        LinkedList<Process> processes) {
    
        int time = 0, waitingTime = 0;
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(new ProcessComparator());
        PriorityQueue<Process> arrivals = new PriorityQueue<>(new ArrivalComparator());
        int[] waitingTimeArr = new int[numberOfProcesses];
        
        //subtract running time and arrival time from waiting times
        for (int  i = 0; i < numberOfProcesses; i++) {
            waitingTimeArr[i] -= processes.get(i).getCpuBurstTime();
            waitingTimeArr[i] -= processes.get(i).getArrivalTime();
        }

        //add processes to arrivals queue to sort by arrival time
        for (Process p: processes) {
            arrivals.add(p);
        }
        
        //add first line to output file
        output.add(firstLine);
    
        //simulate scheduling and CPU
        while (!arrivals.isEmpty() || !readyQueue.isEmpty()) {
            
            if (!readyQueue.isEmpty()) {
                while (!arrivals.isEmpty() && arrivals.peek().getArrivalTime() <= time) {
                    readyQueue.add(arrivals.poll());
                }      
                Process currentProcess = readyQueue.poll();
                
                //add next process and time
                output.add(time + " " + currentProcess.getProcessNumber());
                int currentProcessIndex = currentProcess.getProcessNumber() - 1;
                if (!arrivals.isEmpty()) {
                    int nextArrivalTime = arrivals.peek().getArrivalTime();
                    int burstTime = currentProcess.getCpuBurstTime();
                    
                    //not finished
                    if (nextArrivalTime < burstTime + time) { 
                        int totalTime = nextArrivalTime - time;
                        currentProcess.setCpuBurstTime(burstTime - (totalTime));
                        readyQueue.add(currentProcess);
                        int currentProcessNumber = currentProcess.getProcessNumber();
                        readyQueue.add(arrivals.poll());
                        int nextProcessNumber = readyQueue.peek().getProcessNumber();
                        if (currentProcessNumber == nextProcessNumber && !arrivals.isEmpty()) {
                            int initialTime = time;
                            if (currentProcess.getPriority() > arrivals.peek().getPriority()) {
                                time = arrivals.peek().getArrivalTime();
                            } else {
                                time += currentProcess.getCpuBurstTime();
                            }
                            int timeInterval = time - initialTime;
                            currentProcess.setCpuBurstTime(burstTime - timeInterval);
                        } else if (currentProcessNumber == nextProcessNumber) {
                            time += burstTime;
                            readyQueue.poll();
                            waitingTimeArr[currentProcessIndex] += time;
                        } else {
                        time += totalTime;
                        }
                        
                    //finished
                    } else {
                        time += burstTime;
                        waitingTimeArr[currentProcessIndex] += time;
                    }
                    
                //no more arriving processes
                } else { 
                    time += currentProcess.getCpuBurstTime();
                    waitingTimeArr[currentProcessIndex] += time;
                }
            } else if (!arrivals.isEmpty()) {
                readyQueue.add(arrivals.poll());
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
        public int compare(Process p1, Process p2) {
            if (p1.getPriority() != p2.getPriority()) {
                return p1.getPriority() - p2.getPriority();
            } else {
                return p1.getProcessNumber() - p2.getProcessNumber();
            }
        }
    }
    static class ArrivalComparator implements Comparator<Process>{
        public int compare(Process p1, Process p2) {
            return p1.getArrivalTime() - p2.getArrivalTime();
        }
    }
    public LinkedList<String> getOutput() {
        return output;
    }
}