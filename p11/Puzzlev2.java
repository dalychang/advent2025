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

public class Puzzlev2 {
  record Node(String label, List<String> outputs) {}

  public static String generateKey(Node currentNode, Set<String> visited) {
    List<String> list = new ArrayList<>(visited);
    Collections.sort(list);
    return list.toString() + currentNode.label();
  }

  public static long traverse(Node currentNode, Set<String> visited, Map<String, Node> nodeMap, Map<String, Long> cache, boolean seenFft, boolean seenDac) {
    String key = currentNode.label() + ":" + seenDac + ":" + seenFft;
    if (cache.containsKey(key)) {
      return cache.get(key);
    }

    if (currentNode.label().equals("fft")) {
      seenFft = true;
    }
    if (currentNode.label().equals("dac")) {
      seenDac = true;
    }

    long total = 0;
    for (String outLabel : currentNode.outputs()) {
      if (outLabel.equals("out")) {
        if (seenDac && seenFft) {
          total++;
        }
        continue;
      }
      
      Node out = nodeMap.get(outLabel);
      if (!visited.contains(out.label())) {
        Set<String> visitCopy = new HashSet<>(visited);
        visitCopy.add(out.label());
        total += traverse(out, visitCopy, nodeMap, cache, seenFft, seenDac);
      }
    }
    
    cache.put(key, total);
    return total;
  }

  public static long compute(List<Node> nodes, Map<String, Node> nodeMap, Map<String, Long> cache) {
    Node start = nodeMap.get("svr");
    Set<String> visited = new HashSet<>();
    visited.add(start.label());
    return traverse(start, visited, nodeMap, cache, false, false);
  }
    
  public static void main(String[] args) throws Exception {
    final List<String> lines = Helper.loadFile("dev_advent/p11/input.txt");
    Clock clock = Clock.systemUTC();
    long startTime = clock.millis();
    
    List<Node> nodes = new ArrayList<>();
    Map<String, Node> nodeMap = new HashMap<>();
    Map<String, Long> cache = new HashMap<>();
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

    long answer = compute(nodes, nodeMap, cache);
    
    System.out.println("answer is " + answer);     
    
    System.out.println("time taken " + (clock.millis() - startTime) + "ms");
  }
}
