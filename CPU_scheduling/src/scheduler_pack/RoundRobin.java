package scheduler_pack;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class RoundRobin {

    //fields
    ArrayList<String> output = new ArrayList<>();

    //constructors
    public RoundRobin(String firstLine, int quantumLength, 
       int numberOfProcesses, String[] processArray) {

        //convert process data strings to 2D array
        final int P_DATA = 4, procNumIndex = 0, procArTimeIndex = 1, 
            burstIndex = 2;  
        int[][] processes = new int[numberOfProcesses][P_DATA];
        for (int i = 0; i < numberOfProcesses; i++) {
            String[] processStrTokens = processArray[i].split(" ");
            for (int j = 0; j < P_DATA; j++) {
                processes[i][j] = Integer.parseInt(processStrTokens[j]);
            }
        }
        Queue<Integer> fifo = new LinkedList<>();  
        LinkedList<Integer> procNotStarted = new LinkedList<>();
        /*for (int i = 0; i < numberOfProcesses; i++) {
            procNotStarted.add(processes[i][procNumIndex]);
        }*/
        output.add(firstLine);
        
        //sort processes for queue
        int timerSort = 0, counter = 0;
        while (counter < numberOfProcesses) {  
            for (int i = 0; i < numberOfProcesses; i++) {
                int arrivalTime = processes[i][procArTimeIndex];
                if (arrivalTime == timerSort) {
                    procNotStarted.add(processes[i][procNumIndex]);
                    counter++;                     
                }
            }
            timerSort++;
        }
        for (int k: procNotStarted)
            System.out.println("list" + k);
        //simulate scheduling and CPU
        int time = 0;
        //boolean[] hasStarted = new boolean[numberOfProcesses];
        
        //add processes to queue at arrival time 0
        /*for (int i = 0; i < numberOfProcesses; i++) {
            int arrivalTime = processes[i][procArTimeIndex];
            if (arrivalTime == time) {
                fifo.add(processes[i][procNumIndex]);
                hasStarted[i] = true;
            }
        }*/
        for (int k: fifo)
            System.out.println("queue: " + k);
        int[] waitingTimeArr = new int[numberOfProcesses];
        boolean[] isFinished = new boolean[numberOfProcesses];
        while (!fifo.isEmpty() && !procNotStarted.isEmpty()) {
        
            //add processes to queue time
            for (int i = 0; i < numberOfProcesses; i++) {
                int arrivalTime = processes[i][procArTimeIndex];
                if (arrivalTime <= time) {
                    int procNum = processes[i][procNumIndex];
                    fifo.add(procNum);
                    for (int j = 0; j < procNotStarted.size(); j++) {
                        if (procNotStarted.get(j) == procNum) {
                            procNotStarted.remove(j);
                        }
                    }
                    //hasStarted[i] = true;
                }
            }
            for (int k: fifo)
                System.out.println("queue: " + k);
            int processNumber = fifo.poll(), processIndex = processNumber - 1;
            int cpuBurst = processes[processIndex][burstIndex];
            String timeStr = Integer.toString(time);
            String pNumStr = Integer.toString(processNumber);
            String cpuLine = timeStr + " " + pNumStr;
            output.add(cpuLine);
            if (processes[processIndex][burstIndex] > quantumLength) {  //not finished
                processes[processIndex][burstIndex] = cpuBurst - quantumLength;
                fifo.add(processNumber);
                time += quantumLength;
            }
            else {                                             //finished
                time += cpuBurst;
                isFinished[processIndex] = true;
            }
            
            //update waiting times
            for (int i = 0; i < numberOfProcesses; i++) {
                int waitTimeSoFar = waitingTimeArr[i];
                if (isFinished[i] == false && processNumber != i + 1) {
                    if (quantumLength >= cpuBurst) {
                        waitingTimeArr[i] = waitTimeSoFar + cpuBurst;
                    }
                    else {
                        waitingTimeArr[i] = waitTimeSoFar + quantumLength;
                    }
                }
            }
        }
        double avg = averageWaitTime(waitingTimeArr);
        String lastLine = String.format("AVG Waiting Time: %.2f", avg);
        output.add(lastLine);
    }

    //methods
    private double averageWaitTime(int[] wait) {
        double sum = 0;
        for (int i = 0; i < wait.length; i++) {
            sum += wait[i];
        }
        return sum / wait.length;
    }
    public ArrayList<String> getOutput() {
        return output;
    }
}