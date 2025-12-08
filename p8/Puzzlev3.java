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
  record Position(int x, int y, int z) {}
  
  // Not quite the distance, but same result.
  public static double findDistance(Position a, Position b) {
    return Math.pow(a.x() - b.x(), 2) + Math.pow(a.y() - b.y(), 2) + Math.pow(a.z() - b.z(), 2);
  }

  public static Position findMin(List<Position> positions, Position in) {
    Position target = null;
    double least = Double.MAX_VALUE;
    for (Position p : positions) {
      if (p == in) {
        continue;
      }

      double distance = findDistance(in, p);
      if (distance < least) {
        least = distance;
        target = p;
      }
    }

    return target;
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

    double biggestDistance = 0;
    List<Position> last = null;
    for (Position p : positions) {
      Position target = findMin(positions, p);
      double distance = findDistance(target, p);
      
      if (biggestDistance < distance) {
        biggestDistance = distance;
        last = List.of(p, target);
      }
    }
    
    long answer = last.get(0).x() * last.get(1).x();
    System.out.println("answer is " + answer);
    
    System.out.println("time taken " + (clock.millis() - startTime) + "ms");
  }
}
