package scheduler_pack;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class WriteFile {

    //constructors
    public WriteFile(ArrayList<String> output, String outName) {
        try {
            File outFile = new File(outName);
            if (!outFile.exists()) {
                outFile.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        try {
            FileWriter writer = new FileWriter(outName);
            for (String out: output) {
                writer.write(out);
                writer.write("\n");
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}