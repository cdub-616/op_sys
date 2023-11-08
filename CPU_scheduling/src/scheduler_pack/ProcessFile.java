package scheduler_pack;

import java.util.ArrayList;

public class ProcessFile {

    //fields
    final int P_DATA = 4;
    int[][] intProcesses;
    String firstLine, scheduleType;
    int quantumLength, numberOfProcesses;
    
    //constructors
    public ProcessFile(ArrayList<String> lines) {
        String[] processes;
        
        firstLine = lines.get(0);
        String[] firstLineTokens = firstLine.split(" ");
        scheduleType = firstLineTokens[0];
        quantumLength = Integer.parseInt(firstLineTokens[1]);
        numberOfProcesses = Integer.parseInt(lines.get(1));
        processes = new String[numberOfProcesses];
        for (int i = 2; i < lines.size(); i++) { //string line for each process
            processes[i - 2] = lines.get(i);
        }
        intProcesses = new int[numberOfProcesses][P_DATA];
        for (int i = 0; i < numberOfProcesses; i++) { //split each line
            String[] processStrTokens = processes[i].split(" ");
            for (int j = 0; j < P_DATA; j++) {   
                intProcesses[i][j] = Integer.parseInt(processStrTokens[j]);
            }
        }
    }
    
    //methods
    public int[][] getProcessArray() {
        return intProcesses;
    }
    public String getFirstLine() {
        return firstLine;
    }
    public String getScheduleType() {
        return scheduleType;
    }
    public int getQuantumLength() {
        return quantumLength;
    }
    public int getNumberOfProcesses() {
        return numberOfProcesses;
    }
}