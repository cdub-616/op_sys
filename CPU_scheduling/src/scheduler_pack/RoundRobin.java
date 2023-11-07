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
        final int P_DATA = 4;  
        int[][] processes = new int[numberOfProcesses][P_DATA];
        for (int i = 0; i < numberOfProcesses; i++) {
            String[] processStrTokens = processArray[i].split(" ");
            for (int j = 0; j < P_DATA; j++) {
                processes[i][j] = Integer.parseInt(processStrTokens[j]);
            }
        }
        Queue<Integer> fifo = new LinkedList<>();  
        output.add(firstLine);
        
        //sort processes for queue
        /*int timerSort = 0, counter = 0;
        while (counter < numberOfProcesses) {  
            for (int i = 0; i < numberOfProcesses; i++) {
                int arrivalTime = processes[i][1];
                if (arrivalTime == timerSort) {
                    fifo.add(processes[i][0]);
                    counter++;                     
                }
            }
            timerSort++;
        }*/
        //simulate scheduling and CPU
        int time = 0;
        boolean[] hasStarted = new boolean[numberOfProcesses];
        
        //add processes to queue at arrival time 0
        for (int i = 0; i < numberOfProcesses; i++) {
            int arrivalTime = processes[i][1];
            if (arrivalTime == time) {
                fifo.add(processes[i][0]);
                hasStarted[i] = true;
            }
        }
        for (int k: fifo)
            System.out.println("queue: " + k);
        int[] waitingTimeArr = new int[numberOfProcesses];
        boolean[] isFinished = new boolean[numberOfProcesses];
        while (!fifo.isEmpty()) {
        
            //add processes to queue at later arrival times
            for (int i = 0; i < numberOfProcesses; i++) {
                int arrivalTime = processes[i][1];
                if (arrivalTime <= time && hasStarted[i] == false) {
                    fifo.add(processes[i][0]);
                    hasStarted[i] = true;
                }
            }
            for (int k: fifo)
                System.out.println("queue: " + k);
            int processNumber = fifo.poll(), processIndex = processNumber - 1;
            int cpuBurst = processes[processIndex][2];
            String timeStr = Integer.toString(time);
            String pNumStr = Integer.toString(processNumber);
            String cpuLine = timeStr + " " + pNumStr;
            output.add(cpuLine);
            if (processes[processIndex][2] > quantumLength) {  //not finished
                processes[processIndex][2] = cpuBurst - quantumLength;
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