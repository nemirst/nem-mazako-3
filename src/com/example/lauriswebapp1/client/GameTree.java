package com.example.lauriswebapp1.client;

import java.util.HashMap;

public class GameTree {
    private HashMap<String, TreeNode> nodes;

    public GameTree() {
    	this.nodes = new HashMap<String, TreeNode>();
    }

    public HashMap<String, TreeNode> getNodes() {
        return nodes;
    }

    public TreeNode addNode(String identifier) {
        return this.addNode(identifier, null);
    }

    public TreeNode addNode(String identifier, String parent) {
        TreeNode node = new TreeNode(identifier);
        nodes.put(identifier, node);

        if (parent != null) {
            nodes.get(parent).addChild(identifier);
        }

        return node;
    }
}
