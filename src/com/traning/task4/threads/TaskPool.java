package com.traning.task4.threads;

import java.util.ArrayList;
import java.util.List;

public class TaskPool {
    private List<Thread> threadList;

    public TaskPool() {
        threadList = new ArrayList<>();
    }

    public void putNewTask(Runnable r){
        Thread t = new Thread(r);
        threadList.add(t);
    }

    public void startAllTask(){
        for(Thread t : threadList){
            t.start();
        }
    }

    public void stopAllTask(){
        boolean f = false;
        while (!f) {
            f = true;
            for (Thread t : threadList) {
                if(t.isAlive()){
                    System.out.println(t + "alive");
                    f = false;
                }
            }
        }
    }

    public int getSize(){
        return threadList.size();
    }
}
