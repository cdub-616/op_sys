package scheduler_pack;

import java.util.ArrayList;

public class ProcessFile {

    //fields
    String[] processes;
    String firstLine, scheduleType;
    int quantumLength, numberOfProcesses;
    
    //constructors
    public ProcessFile(ArrayList<String> lines) {
        firstLine = lines.get(0);
        String[] firstLineTokens = firstLine.split(" ");
        scheduleType = firstLineTokens[0];
        quantumLength = Integer.parseInt(firstLineTokens[1]);
        numberOfProcesses = Integer.parseInt(lines.get(1));
        processes = new String[numberOfProcesses];
        for (int i = 2; i < lines.size(); i++) {
            processes[i - 2] = lines.get(i);
        }
    }
    
    //methods
    public String[] getProcessArray() {
        return processes;
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