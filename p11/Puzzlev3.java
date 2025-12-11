package dev.advent;

import java.time.Clock;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Puzzlev3 {
  record Node(String label, List<String> outputs) {}

  public static long traverse(Node currentNode, Set<Node> visited, Map<String, Node> nodeMap, String target, Map<String, Long> cache) {
    if (cache.containsKey(currentNode.label())) {
      return cache.get(currentNode.label());
    }

    long total = 0;
    for (String outLabel : currentNode.outputs()) {
      if (outLabel.equals(target)) {
        total++;
        continue;
      }
      
      Node out = nodeMap.get(outLabel);
      if (!visited.contains(out) && out != null) {
        Set<Node> visitCopy = new HashSet<>(visited);
        visitCopy.add(out);
        total += traverse(out, visitCopy, nodeMap, target, cache);
      }
    }

    cache.put(currentNode.label(), total);
    return total;
  }

  public static long compute(List<Node> nodes, Map<String, Node> nodeMap, String start, String target) {
    Map<String, Long> cache = new HashMap<>();
    Node startNode = nodeMap.get(start);
    Set<Node> visited = new HashSet<>();
    visited.add(startNode);
    return traverse(startNode, visited, nodeMap, target, cache);
  }
    
  public static void main(String[] args) throws Exception {
    final List<String> lines = Helper.loadFile("dev_advent/p11/input.txt");
    Clock clock = Clock.systemUTC();
    long startTime = clock.millis();
    
    List<Node> nodes = new ArrayList<>();
    Map<String, Node> nodeMap = new HashMap<>();
    for (String line : lines) {
      String[] s = line.split(":");
      String label = s[0];
      String[] t = s[1].trim().split("\\s+");
      List<String> outputs = new ArrayList<>();
      for (String u : t) {
        outputs.add(u);
      }
      
      Node n = new Node(label, outputs);
      nodes.add(n);
      nodeMap.put(label, n);
    }

    long dacToFft = compute(nodes, nodeMap, "svr", "dac") * compute(nodes, nodeMap, "dac", "fft") * compute(nodes, nodeMap, "fft", "out");
    long fftToDac = compute(nodes, nodeMap, "svr", "fft") * compute(nodes, nodeMap, "fft", "dac") * compute(nodes, nodeMap, "dac", "out");
    long answer = dacToFft + fftToDac;
    
    System.out.println("answer is " + answer);     
    
    System.out.println("time taken " + (clock.millis() - startTime) + "ms");
  }
}
