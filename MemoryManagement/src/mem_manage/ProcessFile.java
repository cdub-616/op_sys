package mem_manage;

import java.util.LinkedList;

public class ProcessFile {

    //fields
    int numberOfPages, numberOfFrames, numberOfPageAccessRequests;
    LinkedList<Integer> pageAccessRequests = new LinkedList<Integer>();

    //constructors
    public ProcessFile(LinkedList<String> lines) {
    
        String firstLine = lines.get(0);
        String[] firstLineTokens = firstLine.split(" ");
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