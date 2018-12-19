package com.traning.task4.genalg;

import com.traning.task4.AlgFordaFalc;
import com.traning.task4.GraphNet;
import com.traning.task4.GraphUtils;
import com.traning.task4.structures.Model;
import com.traning.task4.structures.Solution;
import javafx.util.Pair;

import java.util.HashMap;

public class FitnessFunction {
    private GraphNet g;
    private Model m;
    private int maxStorage;
    private HashMap<String, Pair<Integer, Solution>> mapValue;
    private AlgFordaFalc algFordaFalc;
    private boolean enableHash;

    public FitnessFunction(Model m) {
        this.m = m;
        this.maxStorage = m.getSumA();
        g = GraphUtils.getBasicWithStorageGraphStructureFromModel(m, 0);
        this.algFordaFalc = new AlgFordaFalc(g);
        mapValue = new HashMap<>();
        enableHash = true;
    }

    public int getValueForGenome(Genome genome) {
        if(genome == null) return Integer.MAX_VALUE;
        validateGenome(genome);

        if (enableHash && getHashValueFor(genome) != -1) {
            return getHashValueFor(genome);
        }

        Solution sol = getSolutionFromGenome(genome);
        int value = getFunValueForSolution(sol, genome);

        if(enableHash) {
            setHashFor(genome, sol, value);
        }
        return value;
    }

    public Solution getSolutionForGenome(Genome genome){
        if(enableHash) {
            return getHashSolutionFor(genome);
        } else {
            return getSolutionFromGenome(genome);
        }
    }

    private Solution getSolutionFromGenome(Genome genome){
        validateGenome(genome);
        setStorages(genome);
        return algFordaFalc.solve();
    }

    private void setStorages(Genome genome){
        for (int i = 0; i < m.getM(); i++) {
            if (genome.getGen(i)) {
                algFordaFalc.addOrChangeStorageToConsumer(m, i, maxStorage);
            } else {
                algFordaFalc.addOrChangeStorageToConsumer(m, i, 0);
            }
        }
    }

    private void validateGenome(Genome genome){
        if (genome.getSize() != m.getM()) throw new UnsupportedOperationException();
    }


    private int getFunValueForSolution(Solution solution, Genome genome) {
        if (solution.getMaxFlow() != m.getSumS()) return Integer.MAX_VALUE;
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

    private void setHashFor(Genome genome, Solution solution, int valueFunc) {
        String genomeString = genome.toString();
        Pair<Integer, Solution> pair = new Pair<>(valueFunc, solution);
        mapValue.put(genomeString, pair);
    }

    private int getHashValueFor(Genome genome) {
        String genomeString = genome.toString();
        Pair<Integer, Solution> pair = new Pair<>(-1, null);
        return mapValue.getOrDefault(genomeString, pair).getKey();
    }

    private Solution getHashSolutionFor(Genome genome){
        String genomeString = genome.toString();
        Pair<Integer, Solution> pair = new Pair<>(-1, null);
        return mapValue.getOrDefault(genomeString, pair).getValue();
    }

    public boolean isEnableHash() {
        return enableHash;
    }

    public void setEnableHash(boolean enableHash) {
        this.enableHash = enableHash;
    }
}
