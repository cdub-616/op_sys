package mem_manage;

import java.io.File;
import java.util.LinkedList;
import java.util.Scanner;

public class ReadFile {
    
    //fields
    LinkedList<String> lines = new LinkedList<>();

    //constructors
    public ReadFile(String filename) {
        try {
            File file = new File(filename);
            Scanner sc = new Scanner(file);
        
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                lines.add(line);
            }
            sc.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    //methods
    public LinkedList<String> getLines(){
        return lines;
    }
}