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
  record Range(long start, long end) {}
  
  public static void mergeRanges(List<Range> ranges, Range newRange) {
    for (Range range : ranges) {
      if (range.end() >= newRange.end() && range.start() <= newRange.start()) {
        // new range is completely within an existing range.
        return;
      } else if (range.end() <= newRange.end() && range.start() >= newRange.start()) {
        // range is completely within new range.
        ranges.remove(range);
        mergeRanges(ranges, newRange);
        return;
      } else if (range.end() >= newRange.end() && range.start() >= newRange.start() && newRange.end() >= range.start()) {
        // new range overlaps in front
        ranges.remove(range);
        mergeRanges(ranges, new Range(newRange.start(), range.end()));
        return;
      } else if (range.end() <= newRange.end() && range.start() <= newRange.start() && range.end() >= newRange.start()) {
        // new range overlaps in back
        ranges.remove(range);
        mergeRanges(ranges, new Range(range.start(), newRange.end()));
        return;
      }
    }
    ranges.add(newRange);
  }
    
  public static void main(String[] args) throws Exception {
    final List<String> lines = Helper.loadFile("dev_advent/p5/input.txt");
    Clock clock = Clock.systemUTC();
    long startTime = clock.millis();
    
    List<Range> ranges = new ArrayList<>();
    for (String line : lines) {
      if (line.trim().isEmpty()) {
        break;
      }

      String[] parts = line.split("-");
      ranges.add(new Range(Long.parseLong(parts[0]), Long.parseLong(parts[1])));
    }

    List<Range> splitRanges = new ArrayList<>();
    for (Range range : ranges) {
      mergeRanges(splitRanges, range);
    }

    long answer = 0;
    for (Range range : splitRanges) {
      answer += range.end() - range.start() + 1;
    }

    System.out.println("answer is " + answer);     
    
    System.out.println("time taken " + (clock.millis() - startTime) + "ms");
  }
}
