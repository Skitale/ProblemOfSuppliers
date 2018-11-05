package com.traning.task4;

import java.util.ArrayList;
import java.util.List;

public class Vertex {
    private boolean traversalLabel;
    private List<Edge> containIn;
    private int num;
    private int bandwidth = -1;
    private Vertex prevVertex = null;

    public Vertex(int num) {
        this.num = num;
        this.traversalLabel = false;
        containIn = new ArrayList<>();
    }

    public void addEdge(Edge e){
        if(!containIn.contains(e)) {
            containIn.add(e);
        }
    }

    private boolean isExistInEdge(Edge e){
        return true;
    }

    public int getNum() {
        return num;
    }

    public boolean isTraversalLabel() {
        return traversalLabel;
    }

    public void setTraversalLabel(boolean traversalLabel) {
        this.traversalLabel = traversalLabel;
    }

    public int getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(int bandwidth) {
        this.bandwidth = bandwidth;
    }

    public Vertex getPrevVertex() {
        return prevVertex;
    }

    public void setPrevVertex(Vertex prevVertex) {
        this.prevVertex = prevVertex;
    }

    public List<Vertex> getAllToAdjacentVertices(){
        List<Vertex> result = new ArrayList<>();
        for(Edge e : containIn){
            if(e.getFromVertex().equals(this)){
                result.add(e.getToVertex());
            }
        }
        return result;
    }

    public Edge getEdgeWithTo(Vertex v){
        for(Edge e : containIn){
            if(e.getToVertex().equals(v)){
                return e;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "tl=" + traversalLabel +
                ", num=" + num +
                '}';
    }
}
