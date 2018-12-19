package com.traning.task4;

import com.traning.task4.genalg.FitnessFunction;
import com.traning.task4.genalg.GenAlg;
import com.traning.task4.genalg.Genome;
import com.traning.task4.structures.Model;
import com.traning.task4.structures.Solution;

import java.util.ArrayList;
import java.util.List;

public class SearchSolutionAlg {
    private Model m;
    private FitnessFunction fitnessFunction;

    public SearchSolutionAlg(Model m) {
        this.m = m;
        this.fitnessFunction = new FitnessFunction(m);
        fitnessFunction.setEnableHash(false);
    }

    public Solution solve() {
        Genome g = new Genome(m.getM());
        int min = fitnessFunction.getValueForGenome(g);
        Genome minGenome = new Genome(m.getM());
        long stCF = System.currentTimeMillis();
        long endCF = -1;
        do {
            stCF = System.currentTimeMillis();
            int curMin = fitnessFunction.getValueForGenome(g);
            endCF = System.currentTimeMillis();
            System.out.print("perform for " + (endCF - stCF));
            if(curMin < min){
                min = curMin;
                minGenome = cloneGenome(g);
            }
        } while (nextOrderTrueFalse(g));

        return GenAlg.getSolutionForAlg(fitnessFunction, minGenome);
    }

    public Solution solveWithValue(int d){
        if(d <= 0) throw new UnsupportedOperationException();
        List<Integer> listPositions = new ArrayList<>();
        for(int i = 0; i < d; i++){
            listPositions.add(i);
        }
        Genome g = new Genome(m.getM());
        revertToGenome(listPositions, g);
        int min = fitnessFunction.getValueForGenome(g);
        Genome minGenome = cloneGenome(g);
        do {
            int curMin = fitnessFunction.getValueForGenome(g);
            if(curMin < min){
                min = curMin;
                minGenome = cloneGenome(g);
            }
        } while (nextOrderFromCountTrue(listPositions, g, d, m.getM()));

        return GenAlg.getSolutionForAlg(fitnessFunction, minGenome);
    }

    private boolean nextOrderFromCountTrue(List<Integer> positions, Genome genome, int d, int n){
        boolean res = nextPositionsFromCountTrue(positions, d, n - 1);
        if(res) revertToGenome(positions, genome);
        return res;
    }

    private boolean nextPositionsFromCountTrue(List<Integer> positions, int d, int n){
        for(int i = d - 1; i >= 0; i--){
            if(positions.get(i) < n - d + i + 1){
                positions.set(i, positions.get(i) + 1);
                for(int j = i + 1; j < d; j++) {
                    positions.set(j, positions.get(j - 1) + 1);
                }
                return true;
            }
        }
        return false;
    }

    private void revertToGenome(List<Integer> positions, Genome genome){
        genome.reset();

        for(Integer i : positions){
            genome.setGen(i);
        }
    }


    private boolean nextOrderTrueFalse(Genome genome){
        int posOne = -1;
        for(int i = genome.getSize() - 1; i >= 0; i--){
            if(!genome.getGen(i)){
                posOne = i;
                break;
            }
        }

        if(posOne == -1){
            return false;
        }

        genome.setGen(posOne);

        for(int i = posOne + 1; i < genome.getSize(); i++){
            if(genome.getGen(i)){
                genome.unsetGen(i);
            } else {
                genome.setGen(i);
            }
        }

        return true;
    }

    private Genome cloneGenome(Genome genome){
        Genome res = new Genome(genome.getSize());
        for(int i = 0; i < genome.getSize(); i++){
            if(genome.getGen(i)){
                res.setGen(i);
            }
        }
        return res;
    }
}
