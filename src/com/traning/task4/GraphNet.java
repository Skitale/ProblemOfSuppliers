package com.traning.task4;

import java.util.*;

public class GraphNet {
    private List<Edge> edgeList;
    private List<Vertex> vertices;
    private Vertex s;
    private Vertex t;

    public GraphNet(Vertex s, Vertex t) {
        this.s = s;
        this.t = t;
        this.edgeList = new ArrayList<>();
        this.vertices = new ArrayList<>();
        vertices.add(s);
        vertices.add(t);
    }

    public void addEdge(int from, int to, int bandwidth, int flowRate, boolean isDirectEdge) {
        Vertex vFrom = tryFindVertexByNum(from);
        Vertex vTo = tryFindVertexByNum(to);
        if (vFrom != null && vTo != null) {
            Edge alternativeEdge = searchAlternativeEdge(from, to);
            Edge edge = new Edge(vFrom, vTo, bandwidth, flowRate, alternativeEdge, isDirectEdge);
            if(alternativeEdge != null) alternativeEdge.setAlternativeEdge(edge);
            edgeList.add(edge);
        } else {
            System.out.println("From or To not found");
        }
    }

    public void addEdge(int from, int to, int bandwidth) {
        this.addEdge(from, to, bandwidth, 0, true);
    }

    private Edge searchAlternativeEdge(int from, int to){
        for(Edge e : edgeList){
            if(e.getToVertex().getNum() == from
                    && e.getFromVertex().getNum() == to){
                return e;
            }
        }
        return null;
    }

    public void addVertex(int num) {
        Vertex v = tryFindVertexByNum(num);
        if (v == null) {
            v = new Vertex(num);
            vertices.add(v);
        }/* else {
            System.out.println("Vertex is already exist");
        }*/
    }

    private Vertex tryFindVertexByNum(int num) {
        for (Vertex v : vertices) {
            if (v.getNum() == num) {
                return v;
            }
        }
        return null;
    }

    public Vertex getS() {
        return s;
    }

    public Vertex getT() {
        return t;
    }

    public List<Edge> getEdgeList() {
        return edgeList;
    }

    public List<Vertex> getUpFlow() {
        Queue<Vertex> queue = new LinkedList<>();
        s.setBandwidth(Integer.MAX_VALUE);
        for (Vertex v : vertices) {
            if (!v.equals(s)) {
                v.setBandwidth(0);
            }
            v.setPrevVertex(null);
        }
        queue.add(s);
        while (!queue.isEmpty()) {
            Vertex v = queue.remove();
            List<Vertex> listVertices = v.getAllToAdjacentVertices();
            for (Vertex lv : listVertices) {
                Edge edgeWith = v.getEdgeWithTo(lv);
                if (lv.getBandwidth() == 0 && edgeWith.getCanPull() > 0) {
                    lv.setBandwidth(Math.min(v.getBandwidth(), edgeWith.getCanPull()));
                    lv.setPrevVertex(v);
                    queue.add(lv);
                }

                if (lv.equals(t)) {
                    break;
                }
            }
        }
        List<Vertex> result = getChain();
        Collections.reverse(result);
        return result;
    }

    public List<Vertex> getMarkedVertexes(){
        List<Vertex> result = new ArrayList<>();
        for(Vertex v : vertices){
            if(v.getBandwidth() > 0){
                result.add(v);
            }
        }
        return result;
    }

    public List<Vertex> getUnmarkedVertexes(){
        List<Vertex> result = new ArrayList<>();
        for(Vertex v : vertices){
            if(v.getBandwidth() == 0){
                result.add(v);
            }
        }
        return result;
    }

    private List<Vertex> getChain(){
        Vertex tmp = t;
        List<Vertex> result = new ArrayList<>();
        while (tmp != null){
            result.add(tmp);
            tmp = tmp.getPrevVertex();
        }
        return result;
    }
}
