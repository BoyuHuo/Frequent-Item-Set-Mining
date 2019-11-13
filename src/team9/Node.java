package team9;

import java.util.ArrayList;
import java.util.List;

public class Node {
    String idName;
    List<Node> children;
    Node parent;
    Node next;
    long count;

    public Node() {
        this.idName = null;
        this.count = -1;
        children = new ArrayList<Node>();
        next = null;
        parent = null;
    }

    public Node(String idName) {
        this.idName = idName;
        this.count = 1;
        children = new ArrayList<Node>();
        next = null;
        parent = null;
    }

    public Node(String idName, long count) {
        this.idName = idName;
        this.count = count;
        children = new ArrayList<Node>();
        next = null;
        parent = null;
    }

    public void addChild(Node child) {
        children.add(child);
    }

    public void addCount(int count) {
        this.count += count;
    }

    public void addCount() {
        this.count += 1;
    }

    public void setNextNode(Node next) {
        this.next = next;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getChilde(int index) {
        return children.get(index);
    }

    public int hasChild(String idName) {
        for (int i = 0; i < children.size(); i++){
            if (children.get(i).idName.equals(idName))
                return i;
        }
        return -1;
    }

    public String toString() {
        return "id: " + idName + " count: " + count + " children size: "
                + children.size();
    }



}
