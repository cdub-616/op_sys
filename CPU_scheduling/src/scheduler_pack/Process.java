package scheduler_pack;

public class Process {
    
    //fields
    int processNumber, arrivalTime, cpuBurstTime, priority;

    //constructors
    public Process(int processNumber, int arrivalTime, int cpuBurstTime, 
        int priority) {
        this.processNumber = processNumber;
        this.arrivalTime = arrivalTime;
        this.cpuBurstTime = cpuBurstTime;
        this.priority = priority;
    }
    
    public int getProcessNumber() {
        return processNumber;
    }
    public int getArrivalTime() {
        return arrivalTime;
    }
    public int getCpuBurstTime() {
        return cpuBurstTime;
    }
    public int getPriority() {
        return priority;
    }
    public void setCpuBurstTime(int newBurstTime) {
        this.cpuBurstTime = newBurstTime;
    }
}
