package com.traning.task4.structures;

import com.traning.task4.utils.Validator;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private int n;
    private int m;
    private int T;
    private int[] a;
    private int[][] b;
    private int[][] c;
    private List<List<Integer>> d;
    private String name;

    public Model(int n, int m, int t, String name) {
        this.name = name;
        this.n = n;
        this.m = m;
        this.T = t;
        a = new int[n];
        b = new int[n][t];
        c = new int[m][t];
        d = new ArrayList<>();
        for(int i = 0; i < m; i++){
            d.add(new ArrayList<>());
        }
    }

    public void setA(int i, int value){
        Validator.validateIndex(i, n);
        a[i] = value;
    }

    public void setB(int i, int t, int value){
        Validator.validateIndex(i, n);
        Validator.validateIndex(t, T);
        b[i][t] = value;
    }

    public void setC(int i, int t, int value){
        Validator.validateIndex(i, m);
        Validator.validateIndex(t, T);
        c[i][t] = value;
    }

    public void addDFor(int i, int value){
        Validator.validateIndex(i, m);
        d.get(i).add(value);
    }

    public int getA(int i) {
        Validator.validateIndex(i, n);
        return a[i];
    }

    public int getB(int i, int t) {
        Validator.validateIndex(i, n);
        Validator.validateIndex(t, T);
        return b[i][t];
    }

    public int getC(int i, int t) {
        Validator.validateIndex(i, m);
        Validator.validateIndex(t, T);
        return c[i][t];
    }

    public List<Integer> getD(int i) {
        Validator.validateIndex(i, m);
        return d.get(i);
    }

    public int getN() {
        return n;
    }

    public int getM() {
        return m;
    }

    public int getT() {
        return T;
    }

    public String getName() {
        return name;
    }

    public int getSumA(){
        int sum = 0;
        for (int anA : a) {
            sum += anA;
        }
        return sum;
    }

    public int getSumS(){
        int sum = 0;
        for(int i = 0; i < c.length; i++){
            for(int j = 0; j < c[i].length; j++){
                sum += c[i][j];
            }
        }
        return sum;
    }
}
