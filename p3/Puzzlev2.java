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

  public static int getDigitPlace(String s, int start, int place) {
    int biggest = 0;
    int index = 0;
    for (int i = start; i < s.length() - (12 - place); i++) {
      int n = Character.getNumericValue(s.charAt(i));
      if (n > biggest) {
        biggest = n;
        index = i;
      }
    }
    return index;
  }
  
  public static long calculate(String s) {
    long total = 0;
    int startPlace = 0;
    for (int i = 1; i <= 12; i++) {
      int place = getDigitPlace(s, startPlace, i);
      int n = Character.getNumericValue(s.charAt(place));
      total = total * 10 + n;
      startPlace = place + 1;
    }

    return total;
  }
    
  public static void main(String[] args) throws Exception {
    final List<String> lines = Helper.loadFile("dev_advent/p3/input.txt");
    Clock clock = Clock.systemUTC();
    long startTime = clock.millis();
    
    long answer = 0;
    for (String line : lines) {
      //System.out.println(line);
      answer += calculate(line);
    }
    
    System.out.println("answer is " + answer);     
    
    System.out.println("time taken " + (clock.millis() - startTime) + "ms");
  }
}
