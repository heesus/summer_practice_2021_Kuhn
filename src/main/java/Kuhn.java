//Класс реализации алгоритма Куна для поиска наибольшего паросочетания в графе

import java.util.HashMap;

public class Kuhn implements Runnable {
    protected static final String NEWLINE = System.getProperty("line.separator");

    private volatile boolean running = true;
    private volatile boolean paused = true;
    private volatile boolean finished = false;
    private final boolean continuous;

    private final Object pauseLock = new Object();
    BipartiteGraph graph; // исходный граф
    HashMap<Vertex, Vertex> matching; // текущее паросочетание

    public Kuhn(BipartiteGraph g, boolean continuous) {
        graph = g;
        this.continuous = continuous;
    }

    @Override
    public void run() { //запуск потока
        System.out.println("Алгоритм запущен");
        matching = new HashMap<>();
        if (graph != null) {
            step();
            // для каждой вершины 1-й доли пытаемся найти увеличивающуюся цепь
            for (Vertex v : graph.getPart1Vertices()) {
                if (running) {
                    System.out.println("Запущен DFS из вершины " + v);
                    graph.reset();
                    if (DFS(v, 0)) {
                        System.out.println("Текущее паросочетание: ");
                        System.out.println(getMatching());
                    }
                } else {
                    break;
                }
            }
        }
        System.out.println(NEWLINE + "_Результат алгоритма_");
        System.out.println(getMatching());
        System.out.println("Алгоритм завершен");
        stop();
    }

    private boolean DFS(Vertex root, int depth) {
        if (root.visited) {
            System.out.println("Вершина " + root + " не рассматривается, так как она уже была посещена");
            return false;
        }
        System.out.println("Рассматриваем вершину " + root);
        root.visited = true; // вершина становится посещенной
        for (Vertex to : graph.getNeighbours(root)) {
            System.out.println("Поиск увеличивающейся цепи через вершину " + to);
            System.out.println("Текущее ребро: " + root + ' ' + to);
            if (!matching.containsKey(to) || DFS(matching.get(to), depth + 1)) {
                // если удалось найти увеличивающуюся цепь, добавляем ребро в текущее паросочетание
                System.out.println("Увеличивающаяся цепь найдена");
                System.out.println("Добавляем ребро " + root + ' ' + to + " в паросочетание");
                matching.put(to, root);
                return true;
            } else {
                System.out.println("Увеличивающаяся цепь не найдена");
                System.out.println("Ребро " + root + ' ' + to + " в паросочетание не добавлено");
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

    public boolean isFinished() { //алгоритм закончил свою работу или нет
        return finished;
    }

    private void checkPaused() { //приостановка пошагового выполенения алгоритма
        if (!continuous) {
            synchronized (pauseLock) {
                if (paused) {
                    try {
                        pauseLock.wait();
                    } catch (InterruptedException e) {

                    }
                }
            }
        } else {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void step() {  //выполенение одного шага алгоритма
        synchronized (pauseLock) {
            paused = true;
            pauseLock.notify();
        }
    }

    public void pause() { //приостановка алгоритма
        paused = true;
    }

    public void resume() { //продолжение выполенения алгоритма
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notify();
        }
    }

    public void stop() { //флаг завершения алгоритма
        running = false;
        finished = true;
        resume();
    }
}
