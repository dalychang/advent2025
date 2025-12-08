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
  record Position(int x, int y, int z) {}
  
  // Not quite the distance, but same result.
  public static double findDistance(Position a, Position b) {
    return Math.pow(a.x() - b.x(), 2) + Math.pow(a.y() - b.y(), 2) + Math.pow(a.z() - b.z(), 2);
  }

  public static Position findMin(List<Position> positions, Position in, double minDistance, Map<Position, Set<Position>> groupings) {
    Position target = null;
    double least = Double.MAX_VALUE;
    for (Position p : positions) {
      if (p == in) {
        continue;
      }

      double distance = findDistance(in, p);
      if (distance < least && distance > minDistance) {
        least = distance;
        target = p;
      }
    }

    return target;
  }

  public static void merge(Map<Position, Set<Position>> groupings, Position p1, Position p2) {
    Set<Position> s1 = groupings.get(p1);
    Set<Position> s2 = groupings.get(p2);

    if (s1 == s2) return;
    Set<Position> sm = new HashSet<>();
    sm.addAll(s1);
    sm.addAll(s2);
    for (Position p : sm) {
      groupings.put(p, sm);
    }
  }
    
  public static void main(String[] args) throws Exception {
    final List<String> lines = Helper.loadFile("dev_advent/p8/input.txt");
    Clock clock = Clock.systemUTC();
    long startTime = clock.millis();
    
    List<Position> positions = new ArrayList<>();
    
    for (String line : lines) {
      String[] s = line.split(",");
      positions.add(new Position(Integer.parseInt(s[0]), Integer.parseInt(s[1]), Integer.parseInt(s[2])));
    }

    Set<Position> positionsUsed = new HashSet<>();
    Set<Position> positionsPending = new HashSet<>();
    positionsPending.addAll(positions);
    Map<Position, Set<Position>> groupings = new HashMap<>();

    int connections = 0;
    double distanceFloor = 0;
    long answer = 0;
    while (!positionsPending.isEmpty()) {
      double least = Double.MAX_VALUE;
      Position np = null;
      Position target = null;
      for (Position p : positions) {
        Position cp = findMin(positions, p, distanceFloor, groupings);
        double distance = findDistance(cp, p);
        if (least > distance) {
          least = distance;
          np = p;
          target = cp;
        }
      }

      if (!groupings.containsKey(target)) {
        Set ns = new HashSet<Position>();
        ns.add(target);
        groupings.put(target, ns);
      }

      if (!groupings.containsKey(np)) {
        Set ns = new HashSet<Position>();
        ns.add(np);
        groupings.put(np, ns);
      }
      merge(groupings, target, np);

      connections++;
      positionsPending.remove(np);
      positionsPending.remove(target);
      distanceFloor = least;

      if (positionsPending.isEmpty()) {
        System.out.println(np + " " + target);
        answer = np.x() * target.x();
      }
    }
    
    System.out.println("answer is " + answer);
    
    System.out.println("time taken " + (clock.millis() - startTime) + "ms");
  }
}
