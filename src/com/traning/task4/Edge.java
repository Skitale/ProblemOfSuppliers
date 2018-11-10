package com.traning.task4;

public class Edge {
    private boolean isDirectEdge;
    private Vertex from;
    private Vertex to;
    private int bandwidth;
    private int flowRate;
    private int canPull;
    private Edge alternativeEdge;

    public Edge(Vertex from, Vertex to, int bandwidth, int flowRate, Edge reverseEdge) {
        this(from, to, bandwidth, flowRate, reverseEdge, true);
        canPull = bandwidth;
    }

    public Edge(Vertex from, Vertex to, int bandwidth, int flowRate, Edge alternativeEdge, boolean isDirectEdge) {
        this.from = from;
        this.to = to;
        this.isDirectEdge = isDirectEdge;
        this.bandwidth = bandwidth;
        this.flowRate = flowRate;
        this.alternativeEdge = alternativeEdge;
        canPull = bandwidth;
        addEdgeInVertex(from);
        addEdgeInVertex(to);
    }

    public void reset(){
        alternativeEdge = null;
        flowRate = 0;
        canPull = 0;
    }

    public int getCanPull() {
        return canPull;
    }

    public void setCanPull(int canPull) {
        this.canPull = canPull;
    }

    private void addEdgeInVertex(Vertex v) {
        v.addEdge(this);
    }

    public Edge getAlternativeEdge() {
        return alternativeEdge;
    }

    public void setAlternativeEdge(Edge alternativeEdge) {
        this.alternativeEdge = alternativeEdge;
    }

    public Vertex getFromVertex() {
        return from;
    }

    public Vertex getToVertex() {
        return to;
    }

    public int getBandwidth() {
        return bandwidth;
    }

    public int getFlowRate() {
        return flowRate;
    }

    public boolean isDirectEdge() {
        return isDirectEdge;
    }

    public void setBandwidth(int bandwidth) {
        this.bandwidth = bandwidth;
    }

    public void setFlowRate(int flowRate) {
        this.flowRate = flowRate;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "isDEdge=" + isDirectEdge +
                ", from=" + from +
                ", to=" + to +
                ", bw=" + bandwidth +
                ", fr=" + flowRate +
                '}';
    }
}
