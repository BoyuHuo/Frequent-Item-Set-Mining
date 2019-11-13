package team9;

import java.io.IOException;
import java.util.*;

public class FPTree {
    public static long minSupport = 0;
    public static long basketNum = 0;
    public static Map<Set<Node>, Long> frequentSet = new HashMap<Set<Node>, Long>();
    public static FileWriter frequentOut;

    public static void printTreeBranch(Map<String, Node> header, String id, String has) {
        System.out.println("Scanning...");
        Node n = header.get(id);
        while (n.next != null) {
            n = n.next;
            if (n.hasChild(has) != -1) {
                System.out.println("Yes:" + n.hasChild(has));
                Node p = n;
                while (p.children.size()>0){
                    System.out.print(p.children.get(0).idName+" -> ");
                    p=p.children.get(0);
                }
            }
        }


        System.out.println("Done!");

    }


    public static void fpGrowth(Node root, Map<String, Node> header, Set<Node> currentFre) throws IOException {
        Set<String> keys = header.keySet();
        String[] keysArray = keys.toArray(new String[0]);

        if (root.children.isEmpty()) {
            return;
        }

        //Start
        for (int i = keysArray.length - 1; i >= 0; i--) {
            if (currentFre == null) {
                currentFre = new HashSet<>();
            }
            Set<Node> baseFre = deepCopySet(currentFre);


            //create sub tree for the leaf node
            String key = keysArray[i];

            List<Node> leafs = new ArrayList<Node>();
            Node link = header.get(key);

            while (link != null) {
                leafs.add(link);
                link = link.next;
            }
            Map<List<String>, Long> paths = new HashMap<List<String>, Long>();

            Long leafCount = 0L;
            boolean cleanCurFreFlag = false;
            List<Node> noParentNodes = new ArrayList<>();

            for (Node leaf : leafs) {
                List<String> path = new ArrayList<String>();
                Node node = leaf;
                while (node.parent.idName != null) {
                    path.add(node.parent.idName);
                    node = node.parent;
                }
                leafCount += leaf.count;
                paths.put(path, leaf.count);

                if (path.size() <= 0) {
                    noParentNodes.add(leaf);
                }


            }

            if (leafCount >= minSupport) {

                addToFrequent(header.get(key).idName, leafCount, baseFre);

            }


            State holder = getConditionFpTree(paths);
            if (holder.header.size() != 0) {
                fpGrowth(holder.root, holder.header, baseFre);
            }
        }

        return;

    }

    public static void addToFrequent(String idName, Long leafCount, Set<Node> currentFre) throws IOException {
        //adding new records into frequent set
        currentFre.add(new Node(idName, leafCount));
        Long min = Long.MAX_VALUE;

        if (currentFre.size() <= 1) {
            return;
        }

        for (Node n : currentFre) {
            if (n.count < min) {
                min = n.count;
            }
        }
        //frequentSet.put(deepCopySet(currentFre), min);
        frequentOut.add(min, deepCopySet(currentFre));
    }

    public static Set<Node> deepCopySet(Set<Node> node) {
        Set<Node> result = new HashSet<>();
        for (Node n : node) {
            result.add(new Node(n.idName, n.count));
        }
        return result;
    }


    public static State getConditionFpTree(Map<List<String>, Long> paths) {
        List<String[]> matrix = new ArrayList<String[]>();
        for (Map.Entry<List<String>, Long> entry : paths.entrySet()) {
            for (long i = 0; i < entry.getValue(); i++) {
                matrix.add(entry.getKey().toArray(new String[0]));
            }
        }
        Map<String, Integer> frequentMap = new LinkedHashMap<String, Integer>();
        Map<String, Node> cHeader = getHeader(matrix, frequentMap);
        Node cRoot = getFpTree(matrix, cHeader, frequentMap);
        return new State(cRoot, cHeader);
    }


    public static Node getFpTree(List<String[]> matrix,
                                 Map<String, Node> header, Map<String, Integer> frequentMap) {
        Node root = new Node();
        int count = 0;
        for (String[] line : matrix) {
            String[] orderLine = getOrderLine(line, frequentMap);
            Node parent = root;
            for (String idName : orderLine) {
                int index = parent.hasChild(idName);
                if (index != -1) {
                    parent = parent.getChilde(index);
                    parent.addCount();
                } else {
                    Node node = new Node(idName);
                    parent.addChild(node);
                    node.setParent(parent);
                    Node nextNode = header.get(idName);
                    if (nextNode == null) {
                        header.put(idName, node);
                    } else {
                        while (nextNode.next != null) {
                            nextNode = nextNode.next;
                        }
                        nextNode.next = node;
                    }
                    parent = node;
                }
            }
        }
        return root;
    }

    public static String[] getOrderLine(String[] line, Map<String, Integer> frequentMap) {
        Map<String, Integer> countMap = new HashMap<String, Integer>();
        for (String id : line) {
            if (frequentMap.containsKey(id)) {
                countMap.put(id, frequentMap.get(id));
            }
        }
        List<Map.Entry<String, Integer>> mapList = new ArrayList<Map.Entry<String, Integer>>(
                countMap.entrySet());
        Collections.sort(mapList, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> v1,
                               Map.Entry<String, Integer> v2) {
                return v2.getValue() - v1.getValue();
            }
        });
        String[] orderLine = new String[countMap.size()];
        int i = 0;
        for (Map.Entry<String, Integer> entry : mapList) {
            orderLine[i] = entry.getKey();
            i++;
        }
        return orderLine;
    }

    public static Map<String, Node> getHeader(List<String[]> matrix, Map<String, Integer> frequentMap) {
        Map<String, Integer> countMap = new HashMap<String, Integer>();
        for (String[] line : matrix) {
            for (String idName : line) {
                if (countMap.containsKey(idName)) {
                    countMap.put(idName, countMap.get(idName) + 1);
                } else {
                    countMap.put(idName, 1);
                }
            }
        }
        for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
            if (entry.getValue() >= minSupport)
                frequentMap.put(entry.getKey(), entry.getValue());
        }
        List<Map.Entry<String, Integer>> mapList = new ArrayList<Map.Entry<String, Integer>>(
                frequentMap.entrySet());
        Collections.sort(mapList, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> v1,
                               Map.Entry<String, Integer> v2) {
                return v2.getValue() - v1.getValue();
            }
        });
        frequentMap.clear();
        Map<String, Node> header = new LinkedHashMap<String, Node>();
        for (Map.Entry<String, Integer> entry : mapList) {
            header.put(entry.getKey(), null);
            frequentMap.put(entry.getKey(), entry.getValue());
        }
        return header;
    }


}

