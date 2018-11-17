package com.traning.task4;

import com.traning.task4.structures.Model;
import com.traning.task4.structures.Solution;

import java.util.ArrayList;
import java.util.List;

public class AlgBaseFoundConsumersWithStorage {
    private Model m;

    public AlgBaseFoundConsumersWithStorage(Model m) {
        this.m = m;
    }

    public Solution solve() {
        int maxStorage = m.getSumA();
        GraphNet g = GraphUtils.getBasicWithStorageGraphStructureFromModel(m, maxStorage);
        AlgFordaFalc alg = new AlgFordaFalc(g);
        Solution solution = alg.solve();
        if(solution.getUpperBoundForMaxFlow() != solution.getMaxFlow()) return null;
        List<Integer> listConsumers = new ArrayList<>();
        for(int i = 0; i < m.getM(); i++){
            alg.addOrChangeStorageToConsumer(m, i, 0);
            solution = alg.solve();
            if(m.getSumS() != solution.getMaxFlow()){
                alg.addOrChangeStorageToConsumer(m, i, maxStorage);
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
        solution.addParam("numConsumers", sb.toString());
        return solution;
    }
}
