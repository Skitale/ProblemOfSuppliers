package com.traning.task4.genalg;

import com.traning.task4.AlgFordaFalc;
import com.traning.task4.GraphNet;
import com.traning.task4.GraphUtils;
import com.traning.task4.structures.Model;
import com.traning.task4.structures.Solution;

import java.util.HashMap;

public class FitnessFunction {
    private GraphNet g;
    private Model m;
    private int maxStorage;
    private HashMap<String, Integer> mapValue;

    public FitnessFunction(Model m) {
        this.m = m;
        this.maxStorage = m.getSumA();
        g = GraphUtils.getBasicWithStorageGraphStructureFromModel(m, 0);
        mapValue = new HashMap<>();
    }

    public int getValueForGenome(Genome genome) {
        if (genome.getSize() != m.getM()) throw new UnsupportedOperationException();

        if (getHashValueFor(genome) != -1) {
            return getHashValueFor(genome);
        }

        for (int i = 0; i < m.getM(); i++) {
            if (genome.getGen(i)) {
                GraphUtils.addStorageBy(g, m, i, maxStorage);
            } else {
                GraphUtils.deleteStorageBy(g, m, i);
            }
        }

        Solution sol = new AlgFordaFalc(g).solve();
        int value = getFunValueForSolution(sol, genome);
        setHashFor(genome, value);
        return value;
    }

    public Solution getSolutionForGenome(Genome genome){
        for (int i = 0; i < m.getM(); i++) {
            if (genome.getGen(i)) {
                GraphUtils.addStorageBy(g, m, i, maxStorage);
            } else {
                GraphUtils.deleteStorageBy(g, m, i);
            }
        }
        return new AlgFordaFalc(g).solve();
    }

    private int getFunValueForSolution(Solution solution, Genome genome) {
        if (solution.getMaxFlow() != solution.getUpperBoundForMaxFlow()) return Integer.MAX_VALUE;
        return getNumStorage(genome);
    }

    private int getNumStorage(Genome genome) {
        int size = 0;
        for (int i = 0; i < genome.getSize(); i++) {
            if (genome.getGen(i)) {
                size++;
            }
        }
        return size;
    }

    private void setHashFor(Genome genome, int valueFunc) {
        String genomeString = genome.toString();
        mapValue.put(genomeString, valueFunc);
    }

    private int getHashValueFor(Genome genome) {
        String genomeString = genome.toString();
        return mapValue.getOrDefault(genomeString, -1);
    }
}
