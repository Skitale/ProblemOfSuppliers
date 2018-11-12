package com.traning.task4;

import com.traning.task4.structures.Model;
import com.traning.task4.structures.Solution;

import java.util.ArrayList;
import java.util.List;

public class AlgFoundCosumersWithStorage {
    private Model m;

    public AlgFoundCosumersWithStorage(Model m) {
        this.m = m;
    }

    public Solution solve() {
        int maxStorage = m.getSumA();
        GraphNet g = GraphUtils.getBasicWithStorageGraphStructureFromModel(m, maxStorage);
        Solution solution = null;
        List<Integer> listConsumers = new ArrayList<>();
        for(int i = 0; i < m.getM(); i++){
            GraphUtils.deleteStorageBy(g, m, i);
            solution = new AlgFordaFalc(g).solve();
            if(m.getSumS() != solution.getMaxFlow()){
                GraphUtils.addStorageBy(g, m, i, maxStorage);
                listConsumers.add(i);
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append(listConsumers.size()).append(" : {");
        for(Integer i : listConsumers){
            sb.append(i + 1);
            if(!listConsumers.get(listConsumers.size() - 1).equals(i)){
                sb.append(", ");
            }
        }
        sb.append("}");
        if (solution != null) {
            solution.addParam("numConsumers", sb.toString());
        }
        return solution;
    }
}
