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
  record Range(long start, long end) {}
  
  public static boolean isInRange(List<Range> ranges, long value) {
    for (Range range : ranges) {
      if (value >= range.start() && value <= range.end()) {
        return true;
      }
    }

    return false;
  }
    
  public static void main(String[] args) throws Exception {
    final List<String> lines = Helper.loadFile("dev_advent/p5/input.txt");
    Clock clock = Clock.systemUTC();
    long startTime = clock.millis();
    
    List<Range> ranges = new ArrayList<>();
    long answer = 0;
    boolean readingRanges = true;
    for (String line : lines) {
      if (line.trim().isEmpty()) {
        readingRanges = false;
        continue;
      }

      if (readingRanges) {
        String[] parts = line.split("-");
        ranges.add(new Range(Long.parseLong(parts[0]), Long.parseLong(parts[1])));
      } else {
        long value = Long.parseLong(line);
        if (isInRange(ranges, value)) {
          answer++;
        }
      }
    }
    
    System.out.println("answer is " + answer);     
    
    System.out.println("time taken " + (clock.millis() - startTime) + "ms");
  }
}
