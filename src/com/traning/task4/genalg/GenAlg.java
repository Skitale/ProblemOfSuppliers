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
            /*long stCF = System.currentTimeMillis();*/
            //calcFitnessFuncForGenomes();
            /*long endCF = System.currentTimeMillis();
            if(endCF - stCF != 0) {
                System.out.println(populationCount + ".I)CALCULATION FITNESS FUNCTION : perform for " + (endCF - stCF));
            }*/
            /*long stS = System.currentTimeMillis();*/
            selection();
            /*long endS = System.currentTimeMillis();
            if(endS - stS != 0) {
                System.out.println("SELECTION : perform for " + (endS - stS));
            }
            long stC = System.currentTimeMillis();*/
            crossing();
            /*long endC = System.currentTimeMillis();
            if(endC - stC != 0) {
                System.out.println("CROSSING : perform for " + (endC - stC));
            }*/
            mutation();

            curPopulation.clear();
            curPopulation.addAll(kidsPopulation);
            kidsPopulation.clear();
            /*stCF = System.currentTimeMillis();*/
            //calcFitnessFuncForGenomes();
            /*endCF = System.currentTimeMillis();
            if(endCF - stCF != 0) {
                System.out.println(populationCount + ".II)CALCULATION FITNESS FUNCTION : perform for " + (endCF - stCF));
            }*/

            Genome tmp = null;
            if(bestGenome != null) tmp = bestGenome.clone();
            oldBestGenome = tmp;
            /*long stB = System.currentTimeMillis();*/
            bestGenome = getBestGenomeOrNull(oldBestGenome);
            /*long endB = System.currentTimeMillis();
            System.out.println( populationCount + ") current best record = " + getCountIsTrue(bestGenome));*/
            if(getCountIsTrue(oldBestGenome) == getCountIsTrue(bestGenome)){
                countEqual++;
            } else {
                countEqual = 0;
            }


            /*if(endB - stB != 0) {
                System.out.println(populationCount + ") SEARCH THE BEST : perform for " + (endB - stB));
            }*/

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
}
