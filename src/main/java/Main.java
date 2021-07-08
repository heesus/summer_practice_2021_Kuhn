import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try {
            Graph g = readGraph();
            if (g.isEmpty())
                throw new Exception("Введите данные.");
            BipartiteGraph bg = new BipartiteGraph(g);
            System.out.print(bg);
            System.out.println('\n');
            Kuhn k = new Kuhn(bg,true);
            k.run();
            System.out.println(k.getMatching());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static Graph readGraph() throws Exception { //считывание графа
        Graph g = new Graph();
        Scanner in  = new Scanner(System.in);
        String[] edge;
        while (in.hasNextLine()) {
            var line = in.nextLine();
            if (line.isEmpty())
                break;
            edge = line.split(" +");
            if (edge.length != 2)
                throw new Exception("Неправильный ввод данных.");
            g.addEdge(edge[0], edge[1]);
        }
        return g;
    }
}