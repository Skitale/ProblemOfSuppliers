package com.traning.task4;

import java.util.List;

public class GraphUtils {
    public static GraphNet getModGraphWithInverseEdge(GraphNet oldGraphNet) {
        Vertex s = new Vertex(oldGraphNet.getS().getNum());
        Vertex t = new Vertex(oldGraphNet.getT().getNum());
        GraphNet newGraphNet = new GraphNet(s, t);
        for (Edge e : oldGraphNet.getEdgeList()) {
            if(!e.isDirectEdge()) continue;
            int numFrom = e.getFromVertex().getNum();
            int numTo = e.getToVertex().getNum();
            newGraphNet.addVertex(numFrom);
            newGraphNet.addVertex(numTo);
            newGraphNet.addEdge(numFrom, numTo, e.getBandwidth() - e.getFlowRate(), e.getFlowRate(), true);
            newGraphNet.addEdge(numTo, numFrom, e.getFlowRate(), e.getFlowRate(), false);
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
}
