package scheduler_pack;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class RoundRobin {

    //fields
    ArrayList<String> output = new ArrayList<>();

    //constructors
    public RoundRobin(String firstLine, int quantumLength, 
       int numberOfProcesses, int[][] processArray) {

        //convert process data strings to 2D array
        final int PROCNUM_INDEX = 0, PROCARTIME_INDEX = 1, 
            BURST_INDEX = 2;  
        
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
                int arrivalTime = processArray[i][PROCARTIME_INDEX];
                if (arrivalTime == timerSort) {
                    procNotStarted.add(processArray[i][PROCNUM_INDEX]);
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
        //put first process in queue
        int nextProcNum = procNotStarted.getFirst();
        fifo.add(processArray[nextProcNum][PROCNUM_INDEX]);
        procNotStarted.remove();
        for (int proc: procNotStarted)
        	System.out.println(proc);
        int[] waitingTimeArr = new int[numberOfProcesses];
        boolean[] isFinished = new boolean[numberOfProcesses];
        //while (!fifo.isEmpty() && !procNotStarted.isEmpty()) {
        while (!fifo.isEmpty()) {
        	for (int k: fifo)
                System.out.println("queue: " + k);

            //add processes to queue time
            /*for (int i = 0; i < numberOfProcesses; i++) {
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
            }*/
            for (int k: fifo)
                System.out.println("queue: " + k);
            int processNumber = fifo.poll(), processIndex = processNumber - 1;
            int cpuBurst = processArray[processIndex][BURST_INDEX];
            String timeStr = Integer.toString(time);
            String pNumStr = Integer.toString(processNumber);
            String cpuLine = timeStr + " " + pNumStr;
            output.add(cpuLine);
            if (processArray[processIndex][BURST_INDEX] > quantumLength) {  //not finished
                processArray[processIndex][BURST_INDEX] = cpuBurst - quantumLength;
                fifo.add(processNumber);
                time += quantumLength;
            }
            else {                                             //finished
                time += cpuBurst;
                isFinished[processIndex] = true;
            }
        	int nextProcessArTime = processArray[nextProcNum][PROCARTIME_INDEX];
        	if (nextProcessArTime <= time) {
        		fifo.add(nextProcNum);
        		procNotStarted.remove();
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