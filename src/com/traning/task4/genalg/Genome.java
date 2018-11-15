package com.traning.task4.genalg;

import com.traning.task4.utils.Validator;

import java.util.Arrays;

public class Genome implements Cloneable {
    private boolean[] array;

    public Genome(int size){
        array = new boolean[size];
    }

    public void setGen(int i){
        Validator.validateIndex(i, array.length);
        array[i] = true;
    }

    public void unsetGen(int i){
        Validator.validateIndex(i, array.length);
        array[i] = false;
    }

    public boolean getGen(int i){
        Validator.validateIndex(i, array.length);
        return array[i];
    }

    public int getSize(){
        return array.length;
    }

    @Override
    protected Genome clone() {
        Genome genome = new Genome(array.length);
        for(int i = 0; i < array.length; i++){
            if(array[i]){
                genome.setGen(i);
            }
        }
        return genome;
    }

    @Override
    public String toString() {
        return "Genome{" +
                "array=" + Arrays.toString(array) +
                '}';
    }
}
