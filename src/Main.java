public class Main {

    public static void main(String[] args) {
        try {
            Graph g = readGraph();
            BipartiteGraph bg = new BipartiteGraph(g);
            System.out.print(bg);
            System.out.println('\n');
            Kuhn k = new Kuhn(bg);
            k.run();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    static Graph readGraph() { //считывание графа (пока что такое)
        Graph g = new Graph();
        g.addEdge("А", "В");
        g.addEdge("В", "Б");
        g.addEdge("Б", "А");
        g.addVertex("F");
        return g;
    }
}