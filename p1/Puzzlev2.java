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
  
  public static long calculate(String s) {
    return 1;
  }
    
  public static void main(String[] args) throws Exception {
    final List<String> lines = Helper.loadFile("dev_advent/p1/input.txt");
    Clock clock = Clock.systemUTC();
    long startTime = clock.millis();
    
    for (String line : lines) {
      System.out.println(line);
    }

    int dial = 50;
    long answer = 0;
    for (String line : lines) {
      char d = line.charAt(0);
      int move = Integer.parseInt(line.substring(1));
      int delta = dial == 0 ? -1 : 0;

      answer += move / 100;
      move = move % 100;

      if (d == 'R') {
        if (dial != 0 && dial + move >= 100) {
          answer ++;
        }
        dial = (dial + move) % 100;
      } else {
        if (dial != 0 && dial - move <= 0) {
          answer ++;
        }
        dial = (dial - move + 100) % 100;
      }
      System.out.println(dial);
    }
    
    
        
    System.out.println("answer is " + answer);     
    
    System.out.println("time taken " + (clock.millis() - startTime) + "ms");
  }
}
