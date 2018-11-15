package com.traning.task4.genalg;

import com.traning.task4.GraphNet;
import com.traning.task4.structures.Model;
import com.traning.task4.threads.TaskPool;

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
        Genome bestGenome = null;
        while (populationCount <= populationSize || bestGenome == null){
            calcFitnessFuncForGenomes();
            long stS = System.currentTimeMillis();
            selection();
            long endS = System.currentTimeMillis();
            if(endS - stS != 0) {
                System.out.println("SELECTION : perform for " + (endS - stS));
            }
            long stC = System.currentTimeMillis();
            crossing();
            long endC = System.currentTimeMillis();
            if(endC - stC != 0) {
                System.out.println("CROSSING : perform for " + (endC - stC));
            }
            mutation();

            curPopulation.clear();
            curPopulation.addAll(kidsPopulation);
            //curPopulation = kidsPopulation;
            kidsPopulation.clear();
            long stCF = System.currentTimeMillis();
            calcFitnessFuncForGenomes();
            long endCF = System.currentTimeMillis();
            if(endCF - stCF != 0) {
                System.out.println(populationCount + ")CALCULATION FITNESS FUNCTION : perform for " + (endCF - stCF));
            }

            long stB = System.currentTimeMillis();
            bestGenome = getBestGenomeOrNull(bestGenome);
            long endB = System.currentTimeMillis();
            if(endB - stB != 0) {
                System.out.println(populationCount + ") SEARCH THE BEST : perform for " + (endB - stB));
            }

            populationCount++;
        }


       return bestGenome;
    }

    private void calcFitnessFuncForGenomes(){
        TaskPool taskPool = new TaskPool();
        int globDivided = m.getN() / 5 + 2;
        int div = curPopulation.size() / globDivided + 1;
        for (int i = 0; i < curPopulation.size() - div; i += div) {
            int finalI = i;
            taskPool.putNewTask(new Runnable() {
                @Override
                public void run() {
                    int dFrom = finalI;
                    int dTo = finalI + div;
                    for(int j = dFrom; j < dTo; j++){
                        fitnessFunction.getValueForGenome(curPopulation.get(j));
                    }
                }
            });
        }

        if ((globDivided * taskPool.getSize()) != curPopulation.size()) {
            taskPool.putNewTask(new Runnable() {
                @Override
                public void run() {
                    int dFrom = 4 * taskPool.getSize();
                    int dTo = curPopulation.size();
                    for(int j = dFrom; j < dTo; j++){
                        fitnessFunction.getValueForGenome(curPopulation.get(j));
                    }
                }
            });
        }

        taskPool.startAllTask();
        taskPool.stopAllTask();
    }

    private Genome getBestGenomeOrNull(Genome oldBestGenome){
        Genome bestGenome = null;
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
