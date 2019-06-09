package utility;

import java.util.*;

public class DiGraph<T> {
    private Map<T, Integer> vertices;
    private Map<T, Integer> inDeg;
    private List <Set<T>> adj;

    private void dfs (T v, Set<T> visited) {
        if (visited.contains(v)) return;

        visited.add(v);

        for (T u : adj.get(vertices.get(v))) {
            dfs(u, visited);
        }
    }

    public DiGraph() {
        vertices = new HashMap<>();
        inDeg = new HashMap<>();
        adj = new ArrayList<>();
    }

    public void addVertex(T v) {
        if (vertices.containsKey(v)) return;
        vertices.put(v, adj.size());
        inDeg.put(v, 0);
        adj.add(new HashSet<>());
    }

    public void removeVertex(T v) {
        if (!vertices.containsKey(v)) return;
        for (Set<T> neigh : adj) neigh.remove(v);
        adj.remove((int) vertices.get(v));
        vertices.remove(v);
    }

    public void addEdge (T v, T u) {
        addVertex(v);
        addVertex(u);
        int oldInDeg = inDeg.get(u);
        inDeg.put(u, oldInDeg+1);

        adj.get(vertices.get(v)).add(u);
    }

    public void removeEdge (T v, T u) {
        if (!vertices.containsKey(v) || !vertices.containsKey(u) || !adj.get(vertices.get(v)).contains(u)) return;
        int oldInDeg = inDeg.get(u);
        inDeg.put(u, oldInDeg-1);
        adj.get(vertices.get(v)).remove(u);
    }

    public T getRoot () {    // if there are multiple valid roots, I claim no responsibility for user's stupidity
        T ret = null;
        for (Map.Entry<T, Integer> e : inDeg.entrySet()) {
            if (e.getValue().equals(0)) ret = e.getKey();
        }
        return ret;
    }

    public Set<T> getReachable (T v) {
        Set<T> ret = new HashSet<>();
        dfs(v, ret);
        return ret;
    }

    public List<T> getVertices () {
        return new ArrayList <> (vertices.keySet());
    }
}
