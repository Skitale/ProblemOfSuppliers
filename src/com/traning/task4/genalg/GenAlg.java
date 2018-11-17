package com.traning.task4.genalg;

import com.traning.task4.structures.Model;
import com.traning.task4.structures.Solution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GenAlg {
    private int populationSize;
    private int individualsSizeInPopulation;
    private int genomeSize;
    private List<Genome> curPopulation;
    private List<Genome> kidsPopulation;
    private Model m;
    private FitnessFunction fitnessFunction;
    private int populationCount;
    private double mutationProc;
    private Random random;

    public GenAlg(int populationSize, int individualsSizeInPopulation, int genomeSize, double mutationProc) {
        this.populationSize = populationSize;
        this.individualsSizeInPopulation = individualsSizeInPopulation;
        this.genomeSize = genomeSize;
        this.mutationProc = mutationProc;
        this.curPopulation = Collections.synchronizedList(new ArrayList<>());;
        this.kidsPopulation = Collections.synchronizedList(new ArrayList<>());
        random = new Random();
        random.setSeed(System.currentTimeMillis());
    }

    public void setModel(Model m) {
        this.m = m;
        fitnessFunction = new FitnessFunction(m);
    }

    public Genome solve() {
        if (m == null) return null;
        generateInitPopulation();
        Genome oldBestGenome = null;
        Genome bestGenome = null;
        int countEqual = 0;
        while ((populationCount <= populationSize && countEqual < 4) || bestGenome == null || isInfinSolution(bestGenome)){
            //calcFitnessFuncForGenomes();
            selection();
            crossing();
            mutation();

            curPopulation.clear();
            curPopulation.addAll(kidsPopulation);
            kidsPopulation.clear();
            //calcFitnessFuncForGenomes();

            Genome tmp = null;
            if(bestGenome != null) tmp = bestGenome.clone();
            oldBestGenome = tmp;
            bestGenome = getBestGenomeOrNull(oldBestGenome);
            if(getCountIsTrue(oldBestGenome) == getCountIsTrue(bestGenome)){
                countEqual++;
            } else {
                countEqual = 0;
            }
            populationCount++;
        }
       return bestGenome;
    }

    private int getCountIsTrue(Genome genome) {
        if(genome == null) return -1;
        int s = 0;
        for (int i = 0; i < genome.getSize(); i++) {
            s += genome.getGen(i) ? 1 : 0;
        }
        return s;
    }

    private boolean isInfinSolution(Genome genome){
        if(genome == null) return true;
        Solution solution = fitnessFunction.getSolutionForGenome(genome);
        int sol = solution.getMaxFlow();
        int bound = solution.getUpperBoundForMaxFlow();
        return sol != bound;
    }

    private void calcFitnessFuncForGenomes(){
        for (int i = 0; i < curPopulation.size(); i ++) {
            fitnessFunction.getValueForGenome(curPopulation.get(i));
        }
    }


    private Genome getBestGenomeOrNull(Genome oldBestGenome){
        if(curPopulation.isEmpty()) return null;
        Genome bestGenome = curPopulation.get(0);
        int minValue = Integer.MAX_VALUE;
        for(Genome genome : curPopulation){
            int value = fitnessFunction.getValueForGenome(genome);
            if(minValue > value) {
                minValue = value;
                bestGenome = genome;
            }
        }
        int valueForCurrentBestGenome = fitnessFunction.getValueForGenome(oldBestGenome);
        if(valueForCurrentBestGenome < minValue){
            bestGenome = oldBestGenome;
        }
        return bestGenome;
    }

    private Genome generateRandomGenome() {
        Genome g = new Genome(genomeSize);
        for (int i = 0; i < genomeSize; i++) {
            int val = random.nextInt(2);
            if (val == 1) {
                g.setGen(i);
            } else {
                g.unsetGen(i);
            }
        }
        return g;
    }

    private void generateInitPopulation() {

        //int i1 = (int)Math.floor(Math.random() * 10) + 1;
        for (int i = 0; i < individualsSizeInPopulation; i++) {
            Genome g = generateRandomGenome();
            curPopulation.add(g);
        }
        //System.out.println();
    }

    private void selection() {
        for (int i = 0; i < individualsSizeInPopulation; i++) {
            int index1 = random.nextInt(individualsSizeInPopulation);
            int index2 = random.nextInt(individualsSizeInPopulation);

            int f1 = fitnessFunction.getValueForGenome(curPopulation.get(index1));
            int f2 = fitnessFunction.getValueForGenome(curPopulation.get(index2));

            Genome result = f1 < f2 ? curPopulation.get(index1) : curPopulation.get(index2);
            kidsPopulation.add(result.clone());
        }
        //curPopulation.addAll(kidsPopulation);
    }

    private void crossing() {
        for (int i = 0; i < individualsSizeInPopulation - 1; i += 2) {
            cross(kidsPopulation.get(i), kidsPopulation.get(i + 1));
        }
    }

    private void cross(Genome g1, Genome g2){
        Genome etalon = generateRandomGenome();
        for(int i = 0; i < genomeSize; i++){
            if(etalon.getGen(i)){
                swapForGenome(g1, i);
                swapForGenome(g2, i);
            }
        }
    }

    private void swapForGenome(Genome g, int i){
        if(g.getGen(i)){
            g.unsetGen(i);
        } else {
            g.setGen(i);
        }
    }

    private void mutation(){
        for(Genome g : kidsPopulation){
            if(random.nextDouble() <= mutationProc){
                mutate(g);
            }
        }
    }

    private void mutate(Genome g){
        int index = random.nextInt(genomeSize);
        if(g.getGen(index)){
            g.unsetGen(index);
        } else {
            g.setGen(index);
        }
    }

    public FitnessFunction getFitnessFunction() {
        return fitnessFunction;
    }

    public static Solution getSolutionForAlg(FitnessFunction f, Genome genome){
        Solution solution = f.getSolutionForGenome(genome);
        if (solution.getMaxFlow() != solution.getUpperBoundForMaxFlow()) throw new UnsupportedOperationException("wrong solution");
        List<Integer> listConsumers = getConsumersForStorage(genome);
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

    public static List<Integer> getConsumersForStorage(Genome genome){
        List<Integer> result = new ArrayList<>();
        for(int i = 0; i < genome.getSize(); i++){
            if(genome.getGen(i)){
                result.add(i);
            }
        }
        return result;
    }
}
