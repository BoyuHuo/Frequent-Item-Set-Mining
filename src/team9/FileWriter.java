package team9;

import java.io.*;
import java.util.Set;


public class FileWriter {
    private final BufferedWriter writer;
    public Long toatlTuples = 0L;

    public FileWriter(String fileName) throws IOException {
        FileOutputStream fos = new FileOutputStream(fileName);
        OutputStreamWriter isr = new OutputStreamWriter(fos, "UTF-8");
        writer = new BufferedWriter(isr);
    }

    public void add(Long support, Set<Node> itemSet) throws IOException {
        String items = "";
        items += "{";
        int size = itemSet.size();
        int count = 0;
        for (Node n : itemSet) {
            items += n.idName;
            count++;
            if (count < size) {
                items += ",";
            }
        }
        items += "}";


        writer.write(items);

        writer.write(" - " + support);
        writer.write("\n");
        toatlTuples++;
    }


    public void addInteger(Long support, Set<Integer> itemSet) throws IOException {
        String items = "";
        items += "{";
        int size = itemSet.size();
        int count = 0;
        for (Integer n : itemSet) {
            items += n;
            count++;
            if (count < size) {
                items += ",";
            }
        }
        items += "}";


        writer.write(items);

        writer.write(" - " + support);
        writer.write("\n");
        toatlTuples++;
    }

    public void close() throws IOException {
        writer.close();
    }
}
