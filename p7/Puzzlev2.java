package dev.advent;

import java.time.Clock;
import java.util.Arrays;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Deque;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Puzzlev2 {

  record Position(int x, int y) {}
  
  public static long countSplits(char[][] map, Position p, Map<Position, Long> cache) {
    int y = p.y() + 1;
    while (y < map[0].length && map[p.x()][y] == '.') {
      y++;
    }
    if (y >= map[0].length) {
      return 1;
    } else if (map[p.x()][y] == '^') {
      Position np1 = new Position(p.x() - 1, y);
      Position np2 = new Position(p.x() + 1, y);

      if (!cache.containsKey(np1)) {
        cache.put(np1, countSplits(map, np1, cache));
      }
      if (!cache.containsKey(np2)) {
        cache.put(np2, countSplits(map, np2, cache));
      }

      return cache.get(np1) + cache.get(np2);
    } else {
      System.out.println("This shouldn't happen.");
      return 0;
    }
  }
    
  public static void main(String[] args) throws Exception {
    final List<String> lines = Helper.loadFile("dev_advent/p7/input.txt");
    Clock clock = Clock.systemUTC();
    long startTime = clock.millis();
    
    char[][] map = Helper.readCharGrid(lines);
    int sx = lines.get(0).indexOf("S");
    Map<Position, Long> cache = new HashMap<>();
    Position start = new Position(sx, 0);

    long answer = countSplits(map, start, cache);
    
    System.out.println("answer is " + answer);     
    
    System.out.println("time taken " + (clock.millis() - startTime) + "ms");
  }
}
