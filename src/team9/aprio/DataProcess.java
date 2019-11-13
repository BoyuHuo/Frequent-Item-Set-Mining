package team9.aprio;


import java.io.*;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataProcess {
    private String DataPath;
    private Vector<Vector<Integer>> DataSet;
    public static int TransNum;
    public static int MinSup;


    public DataProcess(String path) throws IOException {
        SetDataPath(path);
        this.DataSet = new Vector<Vector<Integer>>();
        ReadDate();
    }

    private void SetDataPath(String path) {
        this.DataPath = path;
    }

    private void ReadDate() throws IOException {
        File file = new File(this.DataPath);
        try {
            FileReader reader = new FileReader(file);
            BufferedReader br = new BufferedReader(reader);
            String str = null;
            String[] tempStr = br.readLine().split(" ");
            TransNum = Integer.parseInt(tempStr[0]);
            MinSup = Integer.parseInt(tempStr[1]);

            while ((str = br.readLine()) != null) {
                Str2Vector(str);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File doesn't exist!");
        }
    }

    private void Str2Vector(String str) {
        Pattern p = Pattern.compile("\\d{1,}");
        Matcher m = p.matcher(str);
        Vector<Integer> list = new Vector<Integer>();
        while (m.find()) {
            list.add(new Integer(m.group()));
        }
        this.DataSet.add(list);
    }


    public Vector<Vector<Integer>> GetDataSet() {
        return this.DataSet;
    }

}
