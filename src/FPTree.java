import java.util.*;

public class FPTree {
    private long minSupport = 0;
    private long basketNum = 0;


    private static Map<Set<Node>, Long> fpGrowth(Node root, Map<String, Node> header, String idName) {
        Map<Set<Node>, Long> conditionFres = new HashMap<Set<Node>, Long>();
        Set<String> keys = header.keySet();
        String[] keysArray = keys.toArray(new String[0]);
        String firstIdName = keysArray[keysArray.length - 1];
        if (isSinglePath(header, firstIdName)) {// ֻ��һ��·��ʱ����·���ϵ�������ϼ��ɵõ�����Ƶ����
            if (idName == null)
                return conditionFres;
            Node leaf = header.get(firstIdName);
            List<Node> paths = new ArrayList<Node>();// �Զ����ϱ���·�����
            paths.add(leaf);
            Node node = leaf;
            while (node.parent.idName != null) {
                paths.add(node.parent);
                node = node.parent;
            }
            conditionFres = getCombinationPattern(paths, idName);
            Node tempNode = new Node(idName, -1L);
            conditionFres = addLeafToFrequent(tempNode, conditionFres);

        } else {
            for (int i = keysArray.length - 1; i >= 0; i--) {// �ݹ�����������Ƶ����
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
                    // if (idName != null)
                    // key = idName + " " + key;
                    Map<Set<Node>, Long> preFres = fpGrowth(holder.root, holder.header, key);
                    if (idName != null) {
                        Node tempNode = new Node(idName, leafCount);
                        preFres = addLeafToFrequent(tempNode, preFres);
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

    private static State getConditionFpTree(Map<List<String>, Long> paths) {
        List<String[]> matrix = new ArrayList<String[]>();
        for (Map.Entry<List<String>, Long> entry : paths.entrySet()) {
            for (long i = 0; i < entry.getValue(); i++) {
                matrix.add(entry.getKey().toArray(new String[0]));
            }
        }
        Map<String, Integer> frequentMap = new LinkedHashMap<String, Integer>();// һ��Ƶ����
        Map<String, Node> cHeader = getHeader(matrix, frequentMap);
        Node cRoot = getFpTree(matrix, cHeader, frequentMap);
        return new State(cRoot, cHeader);
    }

    private static Map<Set<Node>, Long> getCombinationPattern(
            List<Node> paths, String idName) {
        Map<Set<Node>, Long> conditionFres = new HashMap<Set<Node>, Long>();
        int size = paths.size();
        for (int mask = 1; mask < (1 << size); mask++) {// ��������ϣ���1��ʼ��ʾ���Կռ�
            Set<Node> set = new HashSet<Node>();
            // �ҳ�ÿ�ο��ܵ�ѡ��
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
//			count++;
//			if (count % 100000 == 0)
//				System.out.println(count);
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

        return null;
    }

    private static Map<String, Node> getHeader(List<String[]> matrix, Map<String, Integer> frequentMap) {

        return null;
    }

    private static Map<Set<Node>, Long> addLeafToFrequent(Node leaf, Map<Set<Node>, Long> conditionFres) {

        return null;
    }


}

