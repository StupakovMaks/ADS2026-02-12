package by.it.group551003.stupakov.lesson13;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

public class GraphA {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();

        Map<String, List<String>> edges = new TreeMap<>();
        Map<String, Integer> inDegree = new TreeMap<>();

        String[] parts = line.split(",");
        for (String part : parts) {
            String[] v = part.split("->");
            String from = v[0].trim();
            String to = v[1].trim();

            if (!edges.containsKey(from)) {
                edges.put(from, new ArrayList<>());
            }
            edges.get(from).add(to);

            if (!inDegree.containsKey(from)) {
                inDegree.put(from, 0);
            }
            if (!inDegree.containsKey(to)) {
                inDegree.put(to, 0);
            }
            inDegree.put(to, inDegree.get(to) + 1);
        }

        TreeSet<String> ready = new TreeSet<>();
        for (String vertex : inDegree.keySet()) {
            if (inDegree.get(vertex) == 0) {
                ready.add(vertex);
            }
        }

        StringBuilder sb = new StringBuilder();
        while (!ready.isEmpty()) {
            String current = ready.pollFirst();
            sb.append(current).append(" ");
            List<String> next = edges.get(current);
            if (next == null) {
                continue;
            }
            for (String to : next) {
                inDegree.put(to, inDegree.get(to) - 1);
                if (inDegree.get(to) == 0) {
                    ready.add(to);
                }
            }
        }
        System.out.println(sb.toString().trim());
    }
}