package com.traning.task4;

import java.util.ArrayList;
import java.util.List;

public class AlgFordaFalc {
    private GraphNet graphNet;

    public AlgFordaFalc(GraphNet graphNet) {
        this.graphNet = graphNet;
    }

    public int solve() {
        GraphNet modGraph = GraphUtils.getModGraphWithInverseEdge(graphNet);
        while (true) {
            List<Vertex> vertexList = modGraph.getUpFlow();
            if (vertexList.size() == 1 && vertexList.contains(modGraph.getT())) {
                break;
            }

            List<Edge> chainEdges = getChainEdges(vertexList);
            int chainBandWidth = GraphUtils.getChainBandWidth(vertexList);
            for (Edge e : chainEdges) {
                if (e.isDirectEdge()) {
                    fillForDirectEdge(e, chainBandWidth);
                } else {
                    fillForReverseEdge(e, chainBandWidth);
                }
                //System.out.println(e);
            }
        }

        List<Edge> incision = getIncisionGraph(modGraph);
        return getBandwidthIncision(incision);
    }

    private List<Edge> getIncisionGraph(GraphNet graphNet){
        List<Edge> result = new ArrayList<>();
        List<Vertex> markedVertexes = graphNet.getMarkedVertexes();
        List<Vertex> unmarkedVertexes = graphNet.getUnmarkedVertexes();
        for(Vertex mV : markedVertexes){
            for(Vertex umV : unmarkedVertexes){
                Edge edge = mV.getEdgeWithTo(umV);
                if(edge != null) result.add(edge);
            }
        }
        return result;
    }

    private int getBandwidthIncision(List<Edge> incision){
        int sum = 0;
        for(Edge e : incision){
            sum += e.getBandwidth();
        }
        return sum;
    }

    private void fillForDirectEdge(Edge e, int d){
        e.setFlowRate(e.getFlowRate() + d);
        e.setCanPull(e.getBandwidth() - e.getFlowRate());
        Edge altE = e.getAlternativeEdge();
        if(altE == null) throw new UnsupportedOperationException();
        altE.setCanPull(e.getFlowRate());
    }

    private void fillForReverseEdge(Edge e, int d){
        //e.setFlowRate(e.getFlowRate() - d);
        e.setCanPull(e.getCanPull() - d);
        Edge altE = e.getAlternativeEdge();
        if(altE == null) throw new UnsupportedOperationException();
        altE.setFlowRate(e.getCanPull());
        altE.setCanPull(altE.getBandwidth() - altE.getFlowRate());
    }

    private List<Edge> getChainEdges(List<Vertex> vertices) {
        if (vertices == null || vertices.isEmpty()) throw new UnsupportedOperationException();
        List<Edge> result = new ArrayList<>();
        for (int i = 0; i < vertices.size() - 1; i++) {
            Vertex vFrom = vertices.get(i);
            Vertex vTo = vertices.get(i + 1);
            Edge fromToEdge = vFrom.getEdgeWithTo(vTo);
            result.add(fromToEdge);
        }
        return result;
    }

    public GraphNet getGraphNet() {
        return graphNet;
    }

    public void setGraphNet(GraphNet graphNet) {
        this.graphNet = graphNet;
    }
}
