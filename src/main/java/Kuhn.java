import java.util.HashMap;

public class Kuhn {
    /**private volatile boolean running = true;
    private volatile boolean paused = true;
    private volatile boolean finished = false;*/

    private final Object pauseLock = new Object();
    BipartiteGraph graph; // исходный граф
    HashMap<Vertex, Vertex> matching; // текущее паросочетание

    public Kuhn(BipartiteGraph g) {
        graph = g;
    }

    public String getMaxMatching() { //запуск потока
        matching = new HashMap<>();
        if (graph != null) {
            // для каждой вершины 1-й доли пытаемся найти увеличивающуюся цепь
            for (Vertex v : graph.getPart1Vertices()) {
                graph.reset();
                DFS(v, 0);
            }
        }
        return getMatching();
    }

    private boolean DFS(Vertex root, int depth) {
        if (root.visited) {
            return false;
        }
        root.visited = true; // вершина становится посещенной
        for (Vertex to : graph.getNeighbours(root)) {
            if (!matching.containsKey(to) || DFS(matching.get(to), depth + 1)) {
                // если удалось найти увеличивающуюся цепь, добавляем ребро в текущее паросочетание
                matching.put(to, root);
                return true;
            }
        }
        return false;
    }

    public String getMatching() { //текущее паросочетание
        String s = "";
        for (Vertex v1 : graph.getVertices()) {
            if (matching.containsKey(v1)) {
                Vertex src = v1, dst = matching.get(v1);
                if (src.compareTo(dst) > 0) {
                    src = dst;
                    dst = v1;
                }
                s += (src + " " + dst + System.lineSeparator());
            }
        }
        return s;
    }
}

