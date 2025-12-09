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
  record Position(long x, long y) {}
  
  public static boolean hasBetween(Position a, Position b, List<Position> positions) {
    long minX = Math.min(a.x(), b.x());
    long maxX = Math.max(a.x(), b.x());
    long minY = Math.min(a.y(), b.y());
    long maxY = Math.max(a.y(), b.y());

    for (Position p : positions) {
      if (p.x() > minX && p.x() < maxX && p.y() > minY && p.y() < maxY) {
        return true;
      }

      if ((p.x() < minX || p.x() > maxX) && (p.y() < minY || p.y() > maxY)) {
        continue;
      }

      if (p == a || p == b) {
        continue;
      }

      Position q = positions.get((positions.indexOf(p) + 1) % positions.size());
      if (p.x() > minX && p.x() < maxX) {
        if ((p.y() >= maxY && q.y() <= minY) || (q.y() >= maxY && p.y() <= minY)) {
          return true;
        }
      } else if (p.y() > minY && p.y() < maxY) {
        if ((p.x() >= maxX && q.x() <= minX) || (q.x() >= maxX && p.x() <= minX)) {
          return true;
        }
      }
    }

    return false;
  }
    
  public static void main(String[] args) throws Exception {
    final List<String> lines = Helper.loadFile("dev_advent/p9/input.txt");
    Clock clock = Clock.systemUTC();
    long startTime = clock.millis();
    
    List<Position> positions = new ArrayList<>();
    for (String line : lines) {
      String[] s = line.split(",");
      positions.add(new Position(Long.parseLong(s[0]), Long.parseLong(s[1])));
    }
    
    long maxArea = 0;
    for (int i = 0; i < positions.size(); i++) {
      for (int j = i; j < positions.size(); j++) {
        Position a = positions.get(i);
        Position b = positions.get(j);
        long area = (Math.abs(a.x() - b.x()) + 1) * (Math.abs(a.y() - b.y()) + 1);

        if (area > maxArea) {
          if (!hasBetween(a, b, positions)) {
            maxArea = area;
          }
        }
      }
    }

    long answer = maxArea;
    System.out.println("answer is " + answer);     
    
    System.out.println("time taken " + (clock.millis() - startTime) + "ms");
  }
}
