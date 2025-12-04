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

public class Puzzle {

  public static int checkPosition(char[][] map, int x, int y) {
    if (x < 0 || x >= map[0].length) {
      return 0;
    }
    if (y < 0 || y >= map.length) {
      return 0;
    }
    return map[y][x] == '@' ? 1 : 0;
  }
  
  public static int countAdjacent(char[][] map, int x, int y) {
    int count = 0;
    count += checkPosition(map, x - 1, y);
    count += checkPosition(map, x + 1, y);
    count += checkPosition(map, x, y - 1);
    count += checkPosition(map, x, y + 1);
    count += checkPosition(map, x - 1, y - 1);
    count += checkPosition(map, x - 1, y + 1);
    count += checkPosition(map, x + 1, y - 1);
    count += checkPosition(map, x + 1, y + 1);
    return count;
  }
    
  public static void main(String[] args) throws Exception {
    final List<String> lines = Helper.loadFile("dev_advent/p4/input.txt");
    Clock clock = Clock.systemUTC();
    long startTime = clock.millis();
    
    char[][] map = Helper.readCharGrid(lines);
    //Helper.printCharMap(map);

    long answer = 0;
    for (int y = 0; y < map.length; y++) {
      for (int x = 0; x < map[0].length; x++) {
        if (map[y][x] != '@') {
          continue;
        }

        int count = countAdjacent(map, x, y);
        if (count < 4) {
          answer++;
        }
      }
    }
       
    System.out.println("answer is " + answer);     
    
    System.out.println("time taken " + (clock.millis() - startTime) + "ms");
  }
}
