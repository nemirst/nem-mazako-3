package com.example.lauriswebapp1.client;

import java.util.ArrayList;

public class TreeNode {

    private String identifier;
    private int minMaxValue;
    private ArrayList<String> children;

    public TreeNode(String identifier) {
    	this(identifier, -2);
    }
    
    public TreeNode(String identifier, int minMaxValue) {
        this.identifier = identifier;
        this.minMaxValue = minMaxValue;
        children = new ArrayList<String>();
    }

    public String getIdentifier() {
        return identifier;
    }

    public ArrayList<String> getChildren() {
        return children;
    }
    
    public void setMinMaxValue(int minMaxValue) {
    	this.minMaxValue = minMaxValue;
    }
    
    public int getMinMaxValue() {
    	return this.minMaxValue;
    }

    public void addChild(String identifier) {
        children.add(identifier);
    }
}