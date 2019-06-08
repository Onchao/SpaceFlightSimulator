package utility;

import java.util.*;

public class Graph <T> {
    private Map<T, Integer> vertices;
    private List <Set<T>> adj;

    private boolean dfs (T v, int id, Map<T, Integer> visited) {
        if (visited.containsKey(v)) return false;

        visited.put(v, id);

        for (T u : adj.get(vertices.get(v))) {
            dfs(u, id, visited);
        }

        return true;
    }

    public Graph() {
        vertices = new HashMap<>();
        adj = new ArrayList<>();
    }

    public void addVertex(T v) {
        if (vertices.containsKey(v)) return;
        vertices.put(v, adj.size());
        adj.add(new HashSet<>());
    }

    public void addEdge (T v, T u) {
        addVertex(v);
        addVertex(u);

        adj.get(vertices.get(v)).add(u);
        adj.get(vertices.get(u)).add(v);
    }

    public List<List<T>> getCC () {
        Map <T, Integer> visited = new HashMap<>();

        int curId = 0;
        List <List<T>> ret = new ArrayList<>();

        for (T v : vertices.keySet()) {
            if (dfs(v, curId, visited)) {
                ret.add(new ArrayList<>());
                ++ curId;
            }
        }

        for (Map.Entry<T, Integer> e : visited.entrySet()) {
            ret.get(e.getValue()).add(e.getKey());
        }

        return ret;
    }
}
