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

public class Puzzle {

  record Position(int x, int y) {}

  public static void main(String[] args) throws Exception {
    final List<String> lines = Helper.loadFile("dev_advent/p7/input.txt");
    Clock clock = Clock.systemUTC();
    long startTime = clock.millis();
    
    char[][] map = Helper.readCharGrid(lines);
    int sx = lines.get(0).indexOf("S");
    Set<Position> positions = new HashSet<>();
    Set<Position> splits = new HashSet<>();
    Deque<Position> pending = new ArrayDeque<>();
    Position start = new Position(sx, 0);
    pending.add(start);

    while (!pending.isEmpty()) {
      Position p = pending.pop();
      int y = p.y() + 1;
      while (y < map[0].length && map[p.x()][y] == '.') {
        y++;
      }
      if (y >= map[0].length) {
        continue;
      } else if (map[p.x()][y] == '^') {
        splits.add(new Position(p.x(), y));
        Position np1 = new Position(p.x() - 1, y);
        Position np2 = new Position(p.x() + 1, y);
        if (!positions.contains(np1)) {
          positions.add(np1);
          pending.add(np1);
        }
        if (!positions.contains(np2)) {
          positions.add(np2);
          pending.add(np2);
        }
        continue;
      } else {
        System.out.println("This shouldn't happen.");
      }
    }

    long answer = splits.size();
    
    System.out.println("answer is " + answer);     
    
    System.out.println("time taken " + (clock.millis() - startTime) + "ms");
  }
}
