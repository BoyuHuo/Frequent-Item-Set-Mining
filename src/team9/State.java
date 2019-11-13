package team9;

import java.util.Map;

class State {
    public final Node root;
    public final Map<String, Node> header;

    public State(Node root, Map<String, Node> header) {
        this.root = root;
        this.header = header;
    }
}