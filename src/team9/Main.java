package team9;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner cin = new Scanner(System.in);
        String path  = cin.nextLine();
        String output = cin.nextLine();
        FPTree.frequentOut = new FileWriter(output);

        List<String[]> matrix = FileReader.readData(path, ",", "utf-8");
        System.out.println("Total basket:" + FPTree.basketNum);
        System.out.println("Min Support:" + FPTree.minSupport);
        final long startTime = System.nanoTime();

        Map<String, Integer> frequentMap = new LinkedHashMap<String, Integer>();
        Map<String, Node> header = FPTree.getHeader(matrix, frequentMap);
        Node root = FPTree.getFpTree(matrix, header, frequentMap);
        FPTree.fpGrowth(root, header, null);
      /*  for (Map.Entry<Set<Node>, Long> fre : FPTree.frequentSet.entrySet()) {
            for (Node node : fre.getKey())
                System.out.print(node.idName + " ");
            System.out.println("\t" + fre.getValue());
        }*/
        FPTree.frequentOut.close();
        final long duration = System.nanoTime() - startTime;
        System.out.println("Frequent-set size: " + FPTree.frequentOut.toatlTuples);
        System.out.println("Total time: " + TimeUnit.NANOSECONDS.toMillis(duration) + " ms");

        //FPTree.printTreeBranch(header,root,"1","39");

    }

}
