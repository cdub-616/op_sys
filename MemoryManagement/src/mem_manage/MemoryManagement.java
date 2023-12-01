package mem_manage;

import java.util.LinkedList;

public class MemoryManagement {
    
    public static void main(String[] args) {
    
        //fields
        String fileNameIn = "input.txt", fileNameOut = "output.txt";
        LinkedList<String> lines = new LinkedList<String>();
        
        //get lines from file
        ReadFile fileLines = new ReadFile(fileNameIn);
        lines = fileLines.getLines();
        
        //process lines from file
        ProcessFile fileData = new ProcessFile(lines);
        
        //simulate memory management
        
    }


}
