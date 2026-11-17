package by.it.group551003.stupakov.lesson13;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class GraphC {

    private static Map<String, List<String>> edges = new TreeMap<>();
    private static Map<String, List<String>> reverse = new TreeMap<>();
    private static Set<String> visited = new HashSet<>();
    private static List<String> order = new ArrayList<>();
    private static TreeSet<String> component;

    private static void dfs1(String vertex) {
        visited.add(vertex);
        for (String to : edges.get(vertex)) {
            if (!visited.contains(to)) {
                dfs1(to);
            }
        }
        order.add(vertex);
    }

    private static void dfs2(String vertex) {
        visited.add(vertex);
        component.add(vertex);
        for (String to : reverse.get(vertex)) {
            if (!visited.contains(to)) {
                dfs2(to);
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();

        String[] parts = line.split(",");
        for (String part : parts) {
            String[] v = part.split("->");
            String from = v[0].trim();
            String to = v[1].trim();

            if (!edges.containsKey(from)) {
                edges.put(from, new ArrayList<>());
                reverse.put(from, new ArrayList<>());
            }
            if (!edges.containsKey(to)) {
                edges.put(to, new ArrayList<>());
                reverse.put(to, new ArrayList<>());
            }
            edges.get(from).add(to);
            reverse.get(to).add(from);
        }

        for (String vertex : edges.keySet()) {
            if (!visited.contains(vertex)) {
                dfs1(vertex);
            }
        }

        visited.clear();
        for (int i = order.size() - 1; i >= 0; i--) {
            String vertex = order.get(i);
            if (visited.contains(vertex)) {
                continue;
            }
            component = new TreeSet<>();
            dfs2(vertex);
            StringBuilder sb = new StringBuilder();
            for (String s : component) {
                sb.append(s);
            }
            System.out.println(sb);
        }
    }
}