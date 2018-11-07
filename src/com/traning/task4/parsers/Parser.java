package com.traning.task4.parsers;

import com.traning.task4.structures.Model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Parser {
    public static Model parse(File file){
        Model model = null;
        try {
            Reader r = new InputStreamReader(new FileInputStream(file), "UTF-8");
            Scanner s = new Scanner(r).useDelimiter("(\r\n)");
            int n = -1;
            int m = -1;
            int T = -1;
            if(s.hasNextInt()){
                n = s.nextInt();
            }
            if(s.hasNextInt()){
                m = s.nextInt();
            }
            if(s.hasNextInt()){
                T = s.nextInt();
            }
            model = new Model(n, m, T, "test");
            s.next();
            s.useDelimiter("(\r\n)|( )");
            for(int i = 0; i < n; i++){
                //System.out.print(s.next());
                if(s.hasNextInt()){
                    model.setA(i, s.nextInt());
                }
            }
            s.next();
            s.useDelimiter("(\r\n)|( )|(\n)");
            for(int i = 0; i < n; i++){
                for(int t = 0; t < T; t++){
                    if(s.hasNextInt()){
                        model.setB(i, t, s.nextInt());
                    }
                }
                s.next();
            }
            s.next();
            for(int i = 0; i < m; i++){
                for(int t = 0; t < T; t++){
                    if(s.hasNextInt()){
                        model.setC(i, t, s.nextInt());
                    }
                }
                s.next();
            }
            s.next();
            s.useDelimiter("()");
            s.next();
            s.next();
            s.useDelimiter("( \n)");
            for(int i = 0; i < m; i++){
                String[] arr = s.next().split(" ");
                for(String item : arr){
                    model.addDFor(i, Integer.parseInt(item));
                }
            }
            r.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return model;
    }

    public static List<Model> parseFolderWithFiles(String pathToFolder) {
        File folder = new File(pathToFolder);
        File[] files = folder.listFiles();
        List<Model> modelList = new ArrayList<>();
        assert files != null;
        for (File file : files) {
            modelList.add(parse(file));
        }
        return modelList;
    }
}
