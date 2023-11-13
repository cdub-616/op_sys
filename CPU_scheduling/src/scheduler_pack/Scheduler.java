package scheduler_pack;

import java.util.LinkedList;

public class Scheduler {

    public static void main(String[] args) {
        
        //fields
        String fileNameIn = "input.txt", fileNameOut = "output.txt", scheduler;
        LinkedList<String> lines = new LinkedList<String>();
        LinkedList<String> outLines = new LinkedList<String>();
        
        //get lines from file
        ReadFile fileLines = new ReadFile(fileNameIn);
        lines = fileLines.getLines();
        
        //process lines from file
        ProcessFile fileData = new ProcessFile(lines);
        
        //simulate scheduling
        scheduler = fileData.getScheduleType(); 
        if (scheduler.equals("RR")){
            RoundRobin rr = new RoundRobin(fileData.getFirstLine(), 
                fileData.getQuantumLength(), fileData.getNumberOfProcesses(),
                fileData.getProcessList());
            outLines = rr.getOutput();  //get data for output file
        } else if (scheduler.equals("SJF")){
            ShortestJobFirst sjf = new ShortestJobFirst(fileData.getFirstLine(),
                fileData.getQuantumLength(), fileData.getNumberOfProcesses(),
                fileData.getProcessList());
             outLines = sjf.getOutput();   
        } else if (scheduler.equals("PR_noPREMP")) {
            Priority_noPreemption pnp = new Priority_noPreemption(
                fileData.getFirstLine(), fileData.getQuantumLength(), 
                fileData.getNumberOfProcesses(), fileData.getProcessList());
               outLines = pnp.getOutput();
        } else {
            System.out.println("Incorrect schedule format");
            System.exit(-1);
        }
        
        //write output file
        WriteFile outF = new WriteFile(outLines, fileNameOut);
        for (String out: outLines) {//for testing
            System.out.println(out);
        }
        System.exit(0);
    }
}
