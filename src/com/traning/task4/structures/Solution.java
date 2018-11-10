package com.traning.task4.structures;

import java.util.HashMap;
import java.util.Map;

public class Solution {
    private int maxFlow;
    private int upperBoundForMaxFlow;
    private Map<String, String> params;

    public Solution(int maxFlow, int upperBoundForMaxFlow) {
        this.maxFlow = maxFlow;
        this.upperBoundForMaxFlow = upperBoundForMaxFlow;
        params = new HashMap<>();
    }

    public int getMaxFlow() {
        return maxFlow;
    }

    public void addParam(String key, String value){
        params.put(key, value);
    }

    public void addParam(String key, Integer value){
        params.put(key, value.toString());
    }

    public String getParamByKey(String key){
        return params.get(key);
    }

    public void setMaxFlow(int maxFlow) {
        this.maxFlow = maxFlow;
    }

    public int getUpperBoundForMaxFlow() {
        return upperBoundForMaxFlow;
    }

    public void setUpperBoundForMaxFlow(int upperBoundForMaxFlow) {
        this.upperBoundForMaxFlow = upperBoundForMaxFlow;
    }
}
