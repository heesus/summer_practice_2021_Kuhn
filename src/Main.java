import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        try {
            Graph g = readGraph();
            BipartiteGraph bg = new BipartiteGraph(g);
            System.out.print(bg);
            System.out.println('\n');
            Kuhn k = new Kuhn(bg,true);
            k.run();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    static Graph readGraph() throws Exception { //считывание графа (пока что такое)
        Graph g = new Graph();
        Scanner in  = new Scanner(System.in);
        String[] edge;
        while (in.hasNextLine()) {
            var line = in.nextLine();
            if (line.isEmpty())
                break;
            edge = line.split(" +");
            if (edge.length != 2)
                throw new Exception("Неверный формат ввода");
            g.addEdge(edge[0], edge[1]);
        }
        if (g.isEmpty())
            throw new Exception("Данные не введены");
        return g;
    }
}