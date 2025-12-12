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

/**
 * This puzzle is broken. The answer is incomplete, but it happens to solve the given input.
 */
public class Puzzle {
  private static final Pattern HEADER_REGEX = Pattern.compile("^\\d+:");
  private static final Pattern PLACE_REGEX = Pattern.compile("(\\d+)x(\\d+): ([\\d\\s]*)");

  record Shape(List<boolean[][]> tiles) {}
  record Grid(int x, int y, List<Integer> requirements) {}
  
  public static long calculate(String s) {
    return 1;
  }

  public static boolean[][] rotate90(boolean[][] base) {
    boolean[][] r90 = new boolean[3][3];
    for (int x = 0; x < 3; x++) {
      for (int y = 0; y < 3; y++) {
        r90[x][y] = base[y][2-x];
      }
    }
    return r90;
  }

  public static boolean[][] hFlip(boolean[][] base) {
    boolean[][] tile = new boolean[3][3];
    for (int x = 0; x < 3; x++) {
      for (int y = 0; y < 3; y++) {
        tile[x][y] = base[2-x][y];
      }
    }
    return tile;
  }

  public static boolean[][] vFlip(boolean[][] base) {
    boolean[][] tile = new boolean[3][3];
    for (int x = 0; x < 3; x++) {
      for (int y = 0; y < 3; y++) {
        tile[x][y] = base[x][2-y];
      }
    }
    return tile;
  }

  public static List<boolean[][]> generateTiles(boolean[][] base) {
    boolean[][] r90 = rotate90(base);
    boolean[][] r90v = vFlip(r90);
    boolean[][] r90h = hFlip(r90);
    boolean[][] r180 = rotate90(r90);
    boolean[][] r180v = vFlip(r180);
    boolean[][] r180h = hFlip(r180);
    boolean[][] r270 = rotate90(r180);
    boolean[][] r270v = vFlip(r270);
    boolean[][] r270h = hFlip(r270);
    boolean[][] basev = vFlip(base);
    boolean[][] baseh = hFlip(base);

    List<boolean[][]> mutates = new ArrayList<>();
    mutates.add(base);
    mutates.add(basev);
    mutates.add(baseh);
    mutates.add(r90);
    mutates.add(r90v);
    mutates.add(r90h);
    mutates.add(r180);
    mutates.add(r180v);
    mutates.add(r180h);
    mutates.add(r270);
    mutates.add(r270v);
    mutates.add(r270h);
    
    Set<String> fingerprints = new HashSet<>();
    List<boolean[][]> tiles = new ArrayList<>();

    for (boolean[][] tile : mutates) {
      String fingerprint = Arrays.toString(tile);
      if (!fingerprints.contains(fingerprint)) {
        fingerprints.add(fingerprint);
        tiles.add(tile);
      }
    }

    return tiles;
  }

  public static boolean[][] buildTile(List<String> in) {
    boolean[][] tile = new boolean[3][3];
    for (int y = 0; y < in.size(); y++) {
      String s = in.get(y);
      for (int x = 0; x < s.length(); x++) {
        if (s.charAt(x) == '#') {
          tile[x][y] = true;
        }
      }
    }
    
    return tile;
  }

  public static int count(Shape shape) {
    boolean[][] tile = shape.tiles().get(0);
    Helper.printBitmap(tile, '#', '.');
    int count = 0;
    for (int x = 0; x < 3; x++) {
      for (int y = 0; y < 3; y++) {
        if (tile[x][y]) count++;
      }
    }
    return count;
  }

  public static boolean solve(List<Shape> shapes, Grid grid) {
    int area = grid.x() * grid.y();

    int minArea = 0;
    for (int i = 0; i < shapes.size(); i++) {
      minArea += count(shapes.get(i)) * grid.requirements().get(i);
    }

    System.out.println("Area: " + area + " min: " + minArea);
    if (minArea > area) {
      return false;
    }

    boolean[][] space = new boolean[grid.x()][grid.y()];
    // TODO: Check for whether the tiles will fit into 'space'.

    return true;
  }
    
  public static void main(String[] args) throws Exception {
    final List<String> lines = Helper.loadFile("dev_advent/p12/input.txt");
    Clock clock = Clock.systemUTC();
    long startTime = clock.millis();
    
    List<Shape> shapes = new ArrayList<>();
    List<Grid> grids = new ArrayList<>();

    for (int i = 0; i < lines.size(); i++) {
      String header = lines.get(i);
      if (HEADER_REGEX.matcher(header).find()) {
        System.out.println(header);
        boolean[][] tile = buildTile(List.of(lines.get(i + 1), lines.get(i + 2), lines.get(i + 3)));
        List<boolean[][]> tiles = generateTiles(tile);
        shapes.add(new Shape(tiles));
        i += 4;
      } else {
        Matcher m = PLACE_REGEX.matcher(header);
        m.find();
        int x = Integer.parseInt(m.group(1));
        int y = Integer.parseInt(m.group(2));
        List<Integer> counts = new ArrayList<>();
        String[] c = m.group(3).trim().split("\\s+");
        for (String cc : c) {
          counts.add(Integer.parseInt(cc));
        }
        grids.add(new Grid(x, y, counts));
      }
    }

    System.out.println(shapes);
    System.out.println("\n");
    System.out.println(grids);
    
    long answer = 0;
    for (Grid grid : grids) {
      if (solve(shapes, grid)) {
        answer++;
      }
    }
    System.out.println("answer is " + answer);     
    
    System.out.println("time taken " + (clock.millis() - startTime) + "ms");
  }
}
