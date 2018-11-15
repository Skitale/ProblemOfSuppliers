package com.traning.task4;

import com.traning.task4.structures.Model;
import com.traning.task4.structures.Solution;

public class AlgFoundMinStorage {
    private Model m;

    public AlgFoundMinStorage(Model m) {
        this.m = m;
    }

    public Solution solve() {
        int rightBound = m.getSumA();
        int leftBound = 0;
        int mid = 0;
        int key = m.getSumS();
        GraphNet g = GraphUtils.getBasicWithStorageGraphStructureFromModel(m, rightBound);
        Solution result = new AlgFordaFalc(g).solve();
        while (!(leftBound >= rightBound)) {
            mid = leftBound + (int)((double)(rightBound - leftBound) / 2d);
            GraphUtils.changeStotageForAllConsumers(m, g, mid);
            Solution tmpSolution = new AlgFordaFalc(g).solve();
            if(tmpSolution.getMaxFlow() == key){
                tmpSolution.addParam("minE", mid);
                result = tmpSolution;
                rightBound = mid;
            } else {
                leftBound = mid + 1;
            }
        }
        return result;
    }
}
