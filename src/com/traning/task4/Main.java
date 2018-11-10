package com.traning.task4;

import com.traning.task4.parsers.Parser;
import com.traning.task4.structures.Model;
import com.traning.task4.structures.Solution;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static final String PATH_TO_RESOURCES = "./resources/";

    public static void main(String[] args) {
        List<Model> models = Parser.parseFolderWithFiles(PATH_TO_RESOURCES);
        List<GraphNet> basicGraphNets = new ArrayList<>();
        /*for(Model m : models){
            GraphNet g = GraphUtils.getBasicGraphStructureFromModel(m);
            basicGraphNets.add(g);
        }
        for(GraphNet g : basicGraphNets){
            Solution solution = new AlgFordaFalc(g).solve();
            System.out.println("Max flow = " + solution.getMaxFlow());
            System.out.println("Upper bound for max flow = " + solution.getUpperBoundForMaxFlow());
        }*/
        for(Model m : models){
            Solution solution = new AlgFoundCosumersWithStorage(m).solve();
            System.out.println(solution.getParamByKey("numConsumers"));
        }


        Vertex s = new Vertex("-1");
        Vertex t = new Vertex("-2");
        GraphNet graphNet = new GraphNet(s, t);
        /*graphNet.addVertex(1); // sol = 10
        graphNet.addVertex(2);
        graphNet.addVertex(3);
        graphNet.addVertex(4);
        graphNet.addVertex(5);
        graphNet.addEdge(-1, 1, 5);
        graphNet.addEdge(-1, 2, 4);
        graphNet.addEdge(-1, 3, 2);
        graphNet.addEdge(2, 4, 4);
        graphNet.addEdge(2, 5, 1);
        graphNet.addEdge(1, 4, 5);
        graphNet.addEdge(1, 2, 1);
        graphNet.addEdge(1, 5, 3);
        graphNet.addEdge(3, 2, 2);
        graphNet.addEdge(3, 5, 1);
        graphNet.addEdge(4, -2, 5);
        graphNet.addEdge(5, -2, 8);*/

        /*graphNet.addVertex(1); // sol = 128
        graphNet.addVertex(2);
        graphNet.addVertex(3);
        graphNet.addVertex(4);
        graphNet.addVertex(5);
        graphNet.addVertex(6);
        graphNet.addEdge(-1, 1, 32);
        graphNet.addEdge(-1, 2, 95);
        graphNet.addEdge(-1, 3, 75);
        graphNet.addEdge(-1, 4, 57);
        graphNet.addEdge(1, 2, 5);
        graphNet.addEdge(1, 4, 23);
        graphNet.addEdge(1, -2, 16);
        graphNet.addEdge(2, 5, 6);
        graphNet.addEdge(2, 3, 18);
        graphNet.addEdge(3, 4, 24);
        graphNet.addEdge(3, 5, 9);
        graphNet.addEdge(4, 6, 20);
        graphNet.addEdge(4, -2, 94);
        graphNet.addEdge(5, 4, 11);
        graphNet.addEdge(5, 6, 7);
        graphNet.addEdge(6, -2, 81);*/

        graphNet.addVertex("1"); // sol = 7
        graphNet.addVertex("2");
        graphNet.addVertex("3");
        graphNet.addVertex("4");
        graphNet.addEdge("-1", "1", 5);
        graphNet.addEdge("-1", "2", 3);
        graphNet.addEdge("1", "3", 2);
        graphNet.addEdge("1", "2", 4);
        graphNet.addEdge("2", "4", 5);
        graphNet.addEdge("3", "-2", 7);
        graphNet.addEdge("4", "3", 6);
        graphNet.addEdge("4", "-2", 5);
        /*AlgFordaFalc alg = new AlgFordaFalc(graphNet);
        int d = alg.solve();
        System.out.println(" max flow = " + d);*/
    }
}
