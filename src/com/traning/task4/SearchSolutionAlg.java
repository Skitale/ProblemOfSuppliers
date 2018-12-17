package com.traning.task4;

import com.traning.task4.genalg.FitnessFunction;
import com.traning.task4.genalg.GenAlg;
import com.traning.task4.genalg.Genome;
import com.traning.task4.structures.Model;
import com.traning.task4.structures.Solution;

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
        do {
            int curMin = fitnessFunction.getValueForGenome(g);
            if(curMin < min){
                min = curMin;
                minGenome = cloneGenome(g);
            }
        } while (nextOrderTrueFalse(g));

        return GenAlg.getSolutionForAlg(fitnessFunction, minGenome);
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
