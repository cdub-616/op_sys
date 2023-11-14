package scheduler_pack;

import java.util.LinkedList;

public class ProcessFile {

    //fields
    final int P_DATA = 4;
    LinkedList<Process> processes = new LinkedList<>();
    String firstLine, scheduleType;
    int quantumLength = 0, numberOfProcesses = 0;
    
    //constructors
    public ProcessFile(LinkedList<String> lines) {
        String[] proc;
        
        firstLine = lines.get(0);
        String[] firstLineTokens = firstLine.split(" ");
        scheduleType = firstLineTokens[0];
        if (firstLineTokens.length > 1) {
            quantumLength = Integer.parseInt(firstLineTokens[1]);
        }
        numberOfProcesses = Integer.parseInt(lines.get(1));
        proc = new String[numberOfProcesses];
        for (int i = 2; i < lines.size(); i++) { //string line for each process
            proc[i - 2] = lines.get(i);
        }
        //intProcesses = new int[numberOfProcesses][P_DATA];
        for (int i = 0; i < numberOfProcesses; i++) { //split each line
            String[] processStrTokens = proc[i].split(" ");
            int name = Integer.parseInt(processStrTokens[0]);
            int arrival = Integer.parseInt(processStrTokens[1]);
            int burst = Integer.parseInt(processStrTokens[2]);
            int priority = Integer.parseInt(processStrTokens[3]);
            Process process = new Process(name, arrival, burst, priority);
            processes.add(process);
        }
    }
    
    //methods
    public LinkedList<Process> getProcessList() {
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