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
  
  public static long product(long a, long b) {
    return a * b;
  }
    
  public static void main(String[] args) throws Exception {
    final List<String> lines = Helper.loadFile("dev_advent/p6/input.txt");
    Clock clock = Clock.systemUTC();
    long startTime = clock.millis();
    
    List<List<Long>> numbers = new ArrayList<>();
    List<String> ops = new ArrayList<>();
    for (String line : lines) {
      String[] s = line.trim().split("\\s+");
      if (numbers.isEmpty()) {
        for (int i = 0; i < s.length; i++) {
          numbers.add(new ArrayList<>());
        }
      }

      boolean isOps = line.contains("+");
      for (int i = 0; i < s.length; i++) {
        if (isOps) {
          ops.add(s[i]);
        } else {
          numbers.get(i).add(Long.parseLong(s[i]));
        }
      }
    }

    long answer = 0;
    for (int i = 0; i < numbers.size(); i++) {
      List<Long> values = numbers.get(i);
      long total = 0; 
      if (ops.get(i).equals("+")) {
        total = values.stream().reduce(0L, Long::sum);
      } else {
        total = values.stream().reduce(1L, Puzzle::product);
      }
      answer += total;
    }

    System.out.println("answer is " + answer);     
    
    System.out.println("time taken " + (clock.millis() - startTime) + "ms");
  }
}
