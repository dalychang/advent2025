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

  record Result(int dial, int zeroes) {}
  
  // Brute force it.
  public static Result calculate(int dial, int moves, char direction) {
    int delta = direction == 'R' ? 1 : -1;
    int zeroes = 0;
    while (moves > 100) {
      zeroes++;
      moves -= 100;
    }
    if (direction == 'R') {
      if (dial != 0 && dial + moves >= 100) {
        zeroes++;
      }
    } else {
      if (dial != 0 && dial - moves <= 0) {
        zeroes++;
      }
    }
    dial = (dial + (delta * moves) + 100) % 100;
    return new Result(dial, zeroes);
  }
    
  public static void main(String[] args) throws Exception {
    final List<String> lines = Helper.loadFile("dev_advent/p1/input.txt");
    Clock clock = Clock.systemUTC();
    long startTime = clock.millis();
    
    for (String line : lines) {
      System.out.println(line);
    }
    System.out.println("\n");

    int dial = 50;
    long answer = 0;
    Result currentResult = new Result(50, 0);
    for (String line : lines) {
      char d = line.charAt(0);
      int move = Integer.parseInt(line.substring(1));
      Result newResult = calculate(currentResult.dial(), move, d);
      currentResult = new Result(newResult.dial(), newResult.zeroes() + currentResult.zeroes());
    }
    
    
        
    System.out.println("answer is " + currentResult.zeroes());     
    
    System.out.println("time taken " + (clock.millis() - startTime) + "ms");
  }
}
