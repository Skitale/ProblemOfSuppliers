package com.traning.task4.utils;

public class Validator {
    public static void validateIndex(int i, int size){
        if(i < 0 || i >= size) throw new UnsupportedOperationException("index " + i + " more than size = " + size);
    }
}
