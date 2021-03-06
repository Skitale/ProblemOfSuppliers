package com.traning.task4;

import com.traning.task4.genalg.GenAlg;
import com.traning.task4.genalg.Genome;
import com.traning.task4.structures.Model;
import com.traning.task4.structures.Solution;

import java.util.ArrayList;
import java.util.List;

public class AlgGenFoundConsumersWithStorage {
    private Model m;

    public AlgGenFoundConsumersWithStorage(Model m) {
        this.m = m;
    }
    public Solution solve() {
        GraphNet g = GraphUtils.getBasicWithStorageGraphStructureFromModel(m, m.getSumA());
        Solution sol = new AlgFordaFalc(g).solve();
        if(sol.getUpperBoundForMaxFlow() != sol.getMaxFlow()) return null;
        int generationSize;
        int populationSize;
        if(m.getN() < 10 && m.getM() < 10){
            generationSize = m.getN();
            populationSize = m.getM();
        } else {
            generationSize = Math.abs(m.getM() - m.getN());
            populationSize = m.getM() > m.getN() ? m.getM() / m.getN() : m.getN() / m.getM();
        }
        GenAlg ga = new GenAlg(generationSize, populationSize , m.getM(), 0.02d);
        ga.setModel(m);
        Genome genome = ga.solve();
        return GenAlg.getSolutionForAlg(ga.getFitnessFunction(), genome);
    }

    private List<Integer> getConsumersForStorage(Genome genome){
        List<Integer> result = new ArrayList<>();
        for(int i = 0; i < genome.getSize(); i++){
            if(genome.getGen(i)){
                result.add(i);
            }
        }
        return result;
    }

}
