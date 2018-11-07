package com.traning.task4;

import com.traning.task4.structures.Model;

import java.util.List;

public class GraphUtils {
    public static GraphNet getModGraphWithInverseEdge(GraphNet oldGraphNet) {
        Vertex s = new Vertex(oldGraphNet.getS().getName());
        Vertex t = new Vertex(oldGraphNet.getT().getName());
        GraphNet newGraphNet = new GraphNet(s, t);
        for (Edge e : oldGraphNet.getEdgeList()) {
            if(!e.isDirectEdge()) continue;
            String nameFrom = e.getFromVertex().getName();
            String nameTo = e.getToVertex().getName();
            newGraphNet.addVertex(nameFrom);
            newGraphNet.addVertex(nameTo);
            newGraphNet.addEdge(nameFrom, nameTo, e.getBandwidth() - e.getFlowRate(), e.getFlowRate(), true);
            newGraphNet.addEdge(nameTo, nameFrom, e.getFlowRate(), e.getFlowRate(), false);
        }
        return newGraphNet;
    }

    public static int getChainBandWidth(List<Vertex> vertexList){
        int min = Integer.MAX_VALUE;
        for(Vertex v : vertexList){
            if(v.getBandwidth() < min){
                min = v.getBandwidth();
            }
        }
        return min;
    }

    public static GraphNet getBasicGraphStructureFromModel(Model m){
        Vertex s = new Vertex("s");
        Vertex t = new Vertex("t");
        GraphNet graph = new GraphNet(s, t);
        for(int i = 0; i < m.getN(); i++){
            String v = Integer.toString(i);
            graph.addVertex(v);
            graph.addEdge("s", v, m.getA(i));
            for(int j = 0; j < m.getT(); j++){
                String v1 = v + "," + Integer.toString(j) + "b";
                graph.addVertex(v1);
                graph.addEdge(v, v1, m.getB(i, j));
            }
        }

        for(int i = 0; i < m.getM(); i++){
            for(int j = 0; j < m.getT(); j++){
                String v = Integer.toString(i) + "," + Integer.toString(j) + "c";
                graph.addVertex(v);
                graph.addEdge(v, "t", m.getC(i, j));
            }
            for(Integer prod : m.getD(i)){
                for(int j = 0; j < m.getT(); j++){
                    String vProd = Integer.toString(prod - 1) + "," + Integer.toString(j) + "b";
                    String vCons = Integer.toString(i) + "," + Integer.toString(j) + "c";
                    graph.addEdge(vProd, vCons, Integer.MAX_VALUE/*m.getB(prod - 1, j)*/);
                }
            }
        }


        return graph;
    }
}
