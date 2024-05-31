import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;


public class MemoryManagement { //main class

    //global variables
    public static final int TWO_VALUES = 2;
    public static final int IS_IN = 0;
    public static final int FRAME_INDEX = 1;

    public static void main(String[] args) {

        //fields
        String fileNameIn = "input.txt", fileNameOut = "output.txt";
        LinkedList<String> lines = new LinkedList<>();
        LinkedList<String> fifoLines = new LinkedList<>();
        LinkedList<String> optimalLines = new LinkedList<>();
        LinkedList<String> lruLines = new LinkedList<>();
        LinkedList<String> outLines = new LinkedList<>();

        //get lines from file
        ReadFile fileLines = new ReadFile(fileNameIn);
        lines = fileLines.getLines();

        //process lines from file
        ProcessFile fileData = new ProcessFile(lines);

        //simulate memory management FIFO
        FIFO fifo = new FIFO(fileData.getNumberOfPages(),
            fileData.getNumberOfFrames(),
            fileData.getNumberOfPageAccessRequests(),
            fileData.getPageAccessRequests());
        fifoLines = fifo.getOutput();

        //simulate memory management Optimal
        Optimal optimal = new Optimal(fileData.getNumberOfPages(),
            fileData.getNumberOfFrames(),
            fileData.getNumberOfPageAccessRequests(),
            fileData.getPageAccessRequests());
        optimalLines = optimal.getOutput();

        //simulate memory management LRU
        LRU lru = new LRU(fileData.getNumberOfPages(),
            fileData.getNumberOfFrames(),
            fileData.getNumberOfPageAccessRequests(),
            fileData.getPageAccessRequests());
        lruLines =lru.getOutput();

        //concatenate output lists
        for (String str: fifoLines)
            outLines.add(str);
        for (String str: optimalLines)
            outLines.add(str);
        for (String str: lruLines)
            outLines.add(str);

        //write output file
        WriteFile outFile = new WriteFile(outLines, fileNameOut);
    }
}

class ReadFile { //creates linked list from lines of input.txt

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

class ProcessFile { //processes lines read from input.txt

    //fields
    int numberOfPages, numberOfFrames, numberOfPageAccessRequests;
    LinkedList<Integer> pageAccessRequests = new LinkedList<>();

    //constructors
    public ProcessFile(LinkedList<String> lines) {

        String firstLine = lines.get(0);
        String[] firstLineTokens = firstLine.split("\\s+");
        numberOfPages = Integer.parseInt(firstLineTokens[0]);
        numberOfFrames = Integer.parseInt(firstLineTokens[1]);
        numberOfPageAccessRequests = Integer.parseInt(firstLineTokens[2]);
        for (int i = 1; i < lines.size(); i++) {
            int nextAccessReq = Integer.parseInt(lines.get(i));
            pageAccessRequests.add(nextAccessReq);
        }
    }

    //methods
    public int getNumberOfPages() {
        return numberOfPages;
    }
    public int getNumberOfFrames() {
        return numberOfFrames;
    }
    public int getNumberOfPageAccessRequests() {
        return numberOfPageAccessRequests;
    }
    public LinkedList<Integer> getPageAccessRequests() {
        return pageAccessRequests;
    }
}

class FIFO { //simulates FIFO

    //fields
    LinkedList<String> output = new LinkedList<>();

    //constructors
    public FIFO(int numberOfPages, int numberOfFrames,
        int numberOfPageAccessRequests, LinkedList<Integer> pageAccessRequests){

        int[] frames = new int[numberOfFrames];
        int frameNumber = 0, numberOfPageFaults = 0;

        //copy pageAccessRequests
        LinkedList<Integer> pageAccReqCopy = new LinkedList<>();
        for (int page: pageAccessRequests) {
            pageAccReqCopy.add(page);
        }

        //print replacement type
        output.add("FIFO");

        //simulate memory
        while (!pageAccReqCopy.isEmpty()) {

            //get next page
            int nextPage = pageAccReqCopy.poll();

            //check if next page already in frame
            int[] noFault = new int[MemoryManagement.TWO_VALUES];
            noFault = alreadyIn(frames, nextPage);

            //page already there
            if (noFault[MemoryManagement.IS_IN] != 0) {
                output.add("Page " + nextPage + " already in frame " +
                    noFault[MemoryManagement.FRAME_INDEX]);

            //empty frame
            } else if (frames[frameNumber] == 0) {
                frames[frameNumber] = nextPage;
                numberOfPageFaults++;
                output.add("Page " + nextPage + " loaded into frame " +
                    frameNumber);
                frameNumber++;
            }

            //replace full frame
            else {
                output.add("Page " + frames[frameNumber] + " unloaded from " +
                    "frame " + frameNumber + ", page " + nextPage + " loaded " +
                    "into frame " + frameNumber);
                frames[frameNumber] = nextPage;
                numberOfPageFaults++;
                frameNumber++;
            }


            //wrap index to beginning frame after last frame
            if (frameNumber >= numberOfFrames) {
                frameNumber = 0;
            }
        }

        //print number of page faults
        output.add(numberOfPageFaults + " page faults");
    }

    //methods
    private int[] alreadyIn(int[] frames, int nextPage) {

        int[] alreadyIn = new int[MemoryManagement.TWO_VALUES];
        for (int i = 0; i < frames.length; i++) {
            if (frames[i] == nextPage) {
                alreadyIn[0] = 1;
                alreadyIn[1] = i;
                break;
            }
        }
        return alreadyIn;
    }
    public LinkedList<String> getOutput(){
        return output;
    }
}

class Optimal { //simulates Optimal

    //fields
    LinkedList<String> output = new LinkedList<>();

    //constructors
    public Optimal(int numberOfPages, int numberOfFrames,
        int numberOfPageAccessRequests, LinkedList<Integer> pageAccessRequests){

        double infinite = Double.POSITIVE_INFINITY;
        int infinity = (int)infinite;
        int[] frames = new int[numberOfFrames];
        int frameNumber = 0, numberOfPageFaults = 0;

        //copy pageAccessRequests
        LinkedList<Integer> pageAccReqCopy = new LinkedList<>();
        for (int page: pageAccessRequests) {
            pageAccReqCopy.add(page);
        }

        //print replacement type
        output.add("");
        output.add("Optimal");

        //simulate memory
        while (!pageAccReqCopy.isEmpty()) {

            //get next page
            int nextPage = pageAccReqCopy.peek();

            //check if next page already in frame
            int[] noFault = new int[MemoryManagement.TWO_VALUES];
            noFault = alreadyIn(frames, nextPage);

            //page already there
            if (noFault[MemoryManagement.IS_IN] != 0) {
                output.add("Page " + nextPage + " already in frame " +
                    noFault[MemoryManagement.FRAME_INDEX]);

            //empty frame
            } else if (frames[frameNumber] == 0) {
                frames[frameNumber] = nextPage;
                numberOfPageFaults++;
                output.add("Page " + nextPage + " loaded into frame " +
                    frameNumber);
                frameNumber++;
            }

            //replace full frame
            else {
                Map<Integer, Integer> pageMap = new LinkedHashMap<>();
                for (int i = 0; i < frames.length; i++) {

                    //if page referenced
                    if (pageAccReqCopy.contains(frames[i])){
                        pageMap.put(frames[i], pageAccReqCopy.indexOf(frames[i]));
                    }

                    //if page not referenced
                    else {
                        pageMap.put(frames[i], infinity);
                    }
                }

                //find max value
                double max = Collections.max(pageMap.values());
                int maxNextUse = (int)max;
                int arrayIndex = 0, arrayValue = 0;

                //check for multiple infinite values
                int infiniteCount = 0;
                for (Map.Entry<Integer, Integer> me: pageMap.entrySet()) {
                    if (me.getValue().equals(infinity)) {
                        infiniteCount++;
                    }
                }

                //multiple infinite values
                if (infiniteCount >= 2) {
                    for (Map.Entry<Integer, Integer> me: pageMap.entrySet()) {
                        if (me.getValue().equals(infinity)) {
                            arrayValue = me.getKey();
                            break;
                        }
                    }
                    for (int index = 0; index < frames.length; index++) {
                        if (frames[index] == arrayValue) {
                            arrayIndex = index;
                            break;
                        }
                    }
                }

                //find array index with highest next-time use
                else {
                    for (Map.Entry<Integer, Integer> me: pageMap.entrySet()) {
                        if (me.getValue().equals(maxNextUse)) {
                            arrayValue = me.getKey();
                            break;
                        }
                    }
                    for (int index = 0; index < frames.length; index++) {
                        if (frames[index] == arrayValue){
                            arrayIndex = index;
                            break;
                        }
                    }
                }
                frameNumber = arrayIndex;
                output.add("Page " + frames[frameNumber] + " unloaded from " +
                    "frame " + frameNumber + ", page " + nextPage + " loaded " +
                    "into frame " + frameNumber);
                frames[frameNumber] = nextPage;
                numberOfPageFaults++;
                frameNumber++;
            }


            //wrap index to beginning frame after last frame
            if (frameNumber >= numberOfFrames) {
                frameNumber = 0;
            }

            //remove head of list
            pageAccReqCopy.poll();
        }

        //print number of page faults
        output.add(numberOfPageFaults + " page faults");
    }

    //methods
    private int[] alreadyIn(int[] frames, int nextPage) {

        int[] alreadyIn = new int[MemoryManagement.TWO_VALUES];
        for (int i = 0; i < frames.length; i++) {
            if (frames[i] == nextPage) {
                alreadyIn[0] = 1;
                alreadyIn[1] = i;
                break;
            }
        }
        return alreadyIn;
    }
    public LinkedList<String> getOutput(){
        return output;
    }
}

class LRU { //simulates LRU

    //fields
    LinkedList<String> output = new LinkedList<>();

    //constructors
    public LRU(int numberOfPages, int numberOfFrames,
        int numberOfPageAccessRequests, LinkedList<Integer> pageAccessRequests){

        int[] frames = new int[numberOfFrames];
        int frameNumber = 0, numberOfPageFaults = 0, time = 0;

        //copy pageAccessRequests
        LinkedList<Integer> pageAccReqCopy = new LinkedList<>();
        for (int page: pageAccessRequests) {
            pageAccReqCopy.add(page);
        }

        //print replacement type
        output.add("");
        output.add("LRU");

        //map of array values with time
        Map<Integer, Integer> pageMap = new LinkedHashMap<>();

        //simulate memory
        while (!pageAccReqCopy.isEmpty()) {

            //get next page
            int nextPage = pageAccReqCopy.peek();

            //check if next page already in frame
            int[] noFault = new int[MemoryManagement.TWO_VALUES];
            noFault = alreadyIn(frames, nextPage);

            //page already there
            if (noFault[MemoryManagement.IS_IN] != 0) {
                output.add("Page " + nextPage + " already in frame " +
                    noFault[MemoryManagement.FRAME_INDEX]);
                pageMap.replace(nextPage, time);
            }

            //empty frame
            else if (frames[frameNumber] == 0) {
                frames[frameNumber] = nextPage;
                numberOfPageFaults++;
                output.add("Page " + nextPage + " loaded into frame " +
                    frameNumber);
                pageMap.put(nextPage, time);
                frameNumber++;
            }

            //replace full frame
            else {

                //find array index with min time
                double min = Collections.min(pageMap.values());
                int minTime = (int)min;
                int arrayIndex = 0, arrayValue = 0;
                for (Map.Entry<Integer, Integer> me: pageMap.entrySet()) {
                    if (me.getValue().equals(minTime)) {
                        arrayValue = me.getKey();
                        break;
                    }
                }
                for (int index = 0; index < frames.length; index++) {
                    if (frames[index] == arrayValue) {
                        arrayIndex = index;
                        break;
                    }
                }
                frameNumber = arrayIndex;
                output.add("Page " + frames[frameNumber] + " unloaded from " +
                    "frame " + frameNumber + ", page " + nextPage + " loaded " +
                    "into frame " + frameNumber);
                pageMap.put(nextPage, time);
                pageMap.remove(arrayValue);
                frames[frameNumber] = nextPage;
                numberOfPageFaults++;
                frameNumber++;
            }

            //wrap index to beginning frame after last frame
            if (frameNumber >= numberOfFrames) {
                frameNumber = 0;
            }

            //remove head of list
            pageAccReqCopy.poll();

            //increment time
            time++;
        }

        //print number of page faults
        output.add(numberOfPageFaults + " page faults");
    }

    //methods
    private int[] alreadyIn(int[] frames, int nextPage) {

        int[] alreadyIn = new int[MemoryManagement.TWO_VALUES];
        for (int i = 0; i < frames.length; i++) {
            if (frames[i] == nextPage) {
                alreadyIn[0] = 1;
                alreadyIn[1] = i;
                break;
            }
        }
        return alreadyIn;
    }
    public LinkedList<String> getOutput(){
        return output;
    }
}

class WriteFile { //writes results to output.txt

    //constructors
    public WriteFile(LinkedList<String> output, String outName) {
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