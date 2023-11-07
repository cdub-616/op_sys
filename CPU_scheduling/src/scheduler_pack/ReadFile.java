package scheduler_pack;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class ReadFile {

    //fields
    ArrayList<String> lines = new ArrayList<>();
    
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
    public ArrayList<String> getLines(){
        return lines;
    }
}   