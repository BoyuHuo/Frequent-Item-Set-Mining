package team9;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileReader {

    public static List<String[]> readData(String path, String regex, String encoding) {
        List<String[]> data = new ArrayList<String[]>();
        File file = new File(path);
        try {
            FileInputStream inStream = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inStream, encoding));
            String line = new String();
            //get basket num and min support
            line = reader.readLine();
            String[] tempStr = line.split(" ");
            FPTree.basketNum = Integer.parseInt(tempStr[0]);
            FPTree.minSupport = Integer.parseInt(tempStr[1]);
            //get basket data
            while ((line = reader.readLine()) != null) {
                String[] temps =line.replace("{", "").replace("}", "").split(regex);
                data.add(temps);

            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

}
