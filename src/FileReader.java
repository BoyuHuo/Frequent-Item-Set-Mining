import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileReader {

    public static String readFile(String path, String encoding) {
        File file = new File(path);
        try {
            FileInputStream inStream = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inStream, encoding));
            String line;
            String text = new String();
            while ((line = reader.readLine()) != null) {
                text += line;
            }
            reader.close();
            return text;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String[]> readData(String path, String regex, String encoding) {
        List<String[]> data = new ArrayList<String[]>();
        File file = new File(path);
        try {
            FileInputStream inStream = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inStream, encoding));
            String line = new String();
            while ((line = reader.readLine()) != null) {
                data.add(line.replace("{","").replace("}","").split(regex));
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

}
