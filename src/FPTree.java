import java.util.*;

public class FPTree {
    private long minSupport = 0;
    private long basketNum = 0;


    private Map<Set<Node>, Long> fpGrowth(Node root, Map<String, Node> header, String idName) {
        Map<Set<Node>, Long> conditionFres = new HashMap<Set<Node>, Long>();
        Set<String> keys = header.keySet();
        String[] keysArray = keys.toArray(new String[0]);
        String firstIdName = keysArray[keysArray.length - 1];
        if (isSinglePath(header, firstIdName)) {
            if (idName == null)
                return conditionFres;
            Node leaf = header.get(firstIdName);
            List<Node> paths = new ArrayList<Node>();
            paths.add(leaf);
            Node node = leaf;
            while (node.parent.idName != null) {
                paths.add(node.parent);
                node = node.parent;
            }
            conditionFres = getCombinationPattern(paths, idName);
            Node tempNode = new Node(idName, -1L);
            conditionFres = addLeafToFrequentSet(tempNode, conditionFres);

        } else {
            for (int i = keysArray.length - 1; i >= 0; i--) {
                String key = keysArray[i];
                List<Node> leafs = new ArrayList<Node>();
                Node link = header.get(key);
                while (link != null) {
                    leafs.add(link);
                    link = link.next;
                }
                Map<List<String>, Long> paths = new HashMap<List<String>, Long>();
                Long leafCount = 0L;
                Node noParentNode = null;
                for (Node leaf : leafs) {
                    List<String> path = new ArrayList<String>();
                    Node node = leaf;
                    while (node.parent.idName != null) {
                        path.add(node.parent.idName);
                        node = node.parent;
                    }
                    leafCount += leaf.count;
                    if (path.size() > 0)
                        paths.put(path, leaf.count);
                    else {// û�и����
                        noParentNode = leaf;
                    }
                }
                if (noParentNode != null) {
                    Set<Node> oneItem = new HashSet<Node>();
                    oneItem.add(noParentNode);
                    if (idName != null)
                        oneItem.add(new Node(idName, -2));
                    conditionFres.put(oneItem, leafCount);
                }
                State holder = getConditionFpTree(paths);
                if (holder.header.size() != 0) {
                    Map<Set<Node>, Long> preFres = fpGrowth(holder.root, holder.header, key);
                    if (idName != null) {
                        Node tempNode = new Node(idName, leafCount);
                        preFres = addLeafToFrequentSet(tempNode, preFres);
                    }
                    conditionFres.putAll(preFres);
                }
            }
        }
        return conditionFres;

    }

    private static boolean isSinglePath(Map<String, Node> header,
                                        String tableLink) {
        if (header.size() == 1 && header.get(tableLink).next == null)
            return true;
        return false;
    }

    private State getConditionFpTree(Map<List<String>, Long> paths) {
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

    private static Map<Set<Node>, Long> getCombinationPattern(
            List<Node> paths, String idName) {
        Map<Set<Node>, Long> conditionFres = new HashMap<Set<Node>, Long>();
        int size = paths.size();
        for (int mask = 1; mask < (1 << size); mask++) {
            Set<Node> set = new HashSet<Node>();
            for (int i = 0; i < paths.size(); i++) {
                if ((mask & (1 << i)) > 0) {
                    set.add(paths.get(i));
                }
            }
            long minValue = Long.MAX_VALUE;
            for (Node node : set) {
                if (node.count < minValue)
                    minValue = node.count;
            }
            conditionFres.put(set, minValue);
        }
        return conditionFres;
    }

    private static Node getFpTree(List<String[]> matrix,
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

    private static String[] getOrderLine(String[] line, Map<String, Integer> frequentMap) {
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

    private Map<String, Node> getHeader(List<String[]> matrix, Map<String, Integer> frequentMap) {
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
            if (entry.getValue() >= this.minSupport)
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

    private static Map<Set<Node>, Long> addLeafToFrequentSet(Node leaf, Map<Set<Node>, Long> conditionFres) {
        if (conditionFres.size() == 0) {
            Set<Node> set = new HashSet<Node>();
            set.add(leaf);
            conditionFres.put(set, leaf.count);
        } else {
            Set<Set<Node>> keys = new HashSet<Set<Node>>(
                    conditionFres.keySet());
            for (Set<Node> set : keys) {
                Long count = conditionFres.get(set);
                conditionFres.remove(set);
                set.add(leaf);
                conditionFres.put(set, count);
            }
        }
        return conditionFres;
    }


}

