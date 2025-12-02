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

/*
 * Regex version of part 2.
 */
public class Puzzlev3 {
  public record Range(long start, long end) {}
  public static Pattern RANGE_REGEX = Pattern.compile("(\\d+)\\-(\\d+)");
  public static Pattern INVALID_REGEX = Pattern.compile("^(\\d+)\\1+$");
  
  public static List<Long> getInvalidIds(Range r) {
    List<Long> ids = new ArrayList<>();
    for (long n = r.start(); n <= r.end(); n++) {
      String numberString = String.valueOf(n);

      Matcher m = INVALID_REGEX.matcher(numberString);
      if (m.find()) {
        ids.add(n);
      }
    }
    return ids;
  }

  public static void main(String[] args) throws Exception {
    final List<String> lines = Helper.loadFile("dev_advent/p2/input.txt");
    Clock clock = Clock.systemUTC();
    long startTime = clock.millis();
    
    List<Range> ranges = new ArrayList<>();

    for (String line : lines) {
      String[] stringRange = line.split(",");
      for (String s : stringRange) {
        Matcher m = RANGE_REGEX.matcher(s);
        if (!m.find()) {
          System.out.println("Error parsing " + s);
          continue;
        }

        ranges.add(new Range(Long.parseLong(m.group(1)), Long.parseLong(m.group(2))));
      }
    }

    long answer = 0;
    for (Range r : ranges) {
      //System.out.println(r);
      List<Long> invalidIds = getInvalidIds(r);
      for (Long id : invalidIds) {
        answer += id;
      }
    }

    System.out.println("answer is " + answer);     
    
    System.out.println("time taken " + (clock.millis() - startTime) + "ms");
  }
}
