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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Comparator;

public class Puzzlev2 {

  private static final Pattern EXTRACT_REGEX = Pattern.compile("\\[([#\\.]+)\\]\\s*([\\d\\(\\),\\s]+)\\s*\\{([\\d,\\s]+)\\}");
  private static final int BIG_NO = Integer.MAX_VALUE / 2;
  private static final int THREADS = 32;
  private static final ExecutorService executorService = Executors.newFixedThreadPool(THREADS);

  record Machine(boolean[] lights, List<List<Integer>> toggles, List<Integer> joltages, int maxJoltage, int[] deps) {}

  public static List<List<Integer>> getButtonsFor(Machine machine, int lightSlot) {
    List<List<Integer>> buttons = new ArrayList<>();
    for (List<Integer> button : machine.toggles()) {
      if (button.contains(lightSlot)) {
        buttons.add(button);
      }
    }
    return buttons;
  }

  public static List<Integer> getSmallestDeps(Machine machine, int above) {
    int next = Integer.MAX_VALUE / 2;
    for (Integer i : machine.deps()) {
      if (i > above && i < next) {
        next = i;
      }
    }

    if (next == Integer.MAX_VALUE / 2) {
      return List.of();
    }

    List<Integer> deps = new ArrayList<>();
    for (int i = 0; i < machine.deps().length; i++) {
      if (machine.deps()[i] == next) {
        deps.add(i);
      }
    }
    return deps;
  }

  public static boolean isStateValid(Machine machine, int[] state) {
    for (int i = 0; i < state.length; i++) {
      if (state[i] > machine.joltages().get(i)) {
        return false;
      }
    }
    return true;
  }

  public static boolean isStateSolved(Machine machine, int[] state) {
    for (int i = 0; i < state.length; i++) {
      if (state[i] != machine.joltages().get(i)) {
        return false;
      }
    }
    return true;
  }

  public static int solve(Machine machine, int[] state, int currentDep, List<Integer> deps, List<List<Integer>> buttons, String space) {
    if (buttons.isEmpty()) {
      return Integer.MAX_VALUE / 2;
    }

    List<Integer> currentButton = buttons.get(0);
    List<List<Integer>> remainingButtons = buttons.subList(1, buttons.size());
    int[] stateCopy = Arrays.copyOf(state, state.length);
    int bestPresses = Integer.MAX_VALUE / 2;
    int presses = 0;
    while (stateCopy[currentDep] <= machine.joltages().get(currentDep)) {
      if (!remainingButtons.isEmpty()) {
        bestPresses = Math.min(bestPresses, solve(machine, stateCopy, currentDep, deps, remainingButtons, space + "  ") + presses);
      } else if (stateCopy[currentDep] == machine.joltages().get(currentDep)) {
        if (!deps.isEmpty()) {
          int nextDep = deps.get(0);
          List<Integer> remainingDeps = deps.subList(1, deps.size());
          List<List<Integer>> nextButtons = getButtonsFor(machine, nextDep);
          bestPresses = Math.min(bestPresses, solve(machine, stateCopy, nextDep, remainingDeps, nextButtons, space + "  ") + presses);
        } else {
          List<Integer> nextDeps = getSmallestDeps(machine, machine.deps()[currentDep]);
          if (!nextDeps.isEmpty()) {
            int nextDep = nextDeps.get(0);
            List<Integer> remainingDeps = nextDeps.subList(1, nextDeps.size());
            List<List<Integer>> nextButtons = getButtonsFor(machine, nextDep);
            bestPresses = Math.min(bestPresses, solve(machine, stateCopy, nextDep, remainingDeps, nextButtons, space + "  ") + presses);
          }
        }
      }

      for (Integer i : currentButton) {
        stateCopy[i]++;
      }
      presses++;

      if (isStateSolved(machine, stateCopy)) {
        return presses;
      }

      if (!isStateValid(machine, stateCopy)) {
        break;
      }
    }
        
    return bestPresses;
  }

  public static int solve(Machine machine) {
    int[] state = new int[machine.lights().length];
    List<Integer> nextDeps = getSmallestDeps(machine, 0);
    int nextDep = nextDeps.get(0);
    List<Integer> remainingDeps = nextDeps.subList(1, nextDeps.size());
    List<List<Integer>> nextButtons = getButtonsFor(machine, nextDep);

    return solve(machine, state, nextDep, nextDeps, nextButtons, "");
  }

  public static void main(String[] args) throws Exception {
    final List<String> lines = Helper.loadFile("dev_advent/p10/input.txt");
    Clock clock = Clock.systemUTC();
    long startTime = clock.millis();
    
    List<Machine> machines = new ArrayList<>();
    for (String line : lines) {
      Matcher m = EXTRACT_REGEX.matcher(line);
      m.find();

      String lightString = m.group(1);
      boolean[] lights = new boolean[lightString.length()];
      for (int i = 0; i < lightString.length(); i++) {
        lights[i] = lightString.charAt(i) == '#';
      }

      String toggleString = m.group(2);
      List<List<Integer>> toggles = new ArrayList<>();
      String[] tss = toggleString.split("\\s+");
      for (String ts : tss) {
        String[] ss = ts.substring(1, ts.length() - 1).split(",");
        List<Integer> tt = new ArrayList<>();
        for (String s : ss) {
          tt.add(Integer.parseInt(s));
        }
        toggles.add(tt);
      }

      String joltageString = m.group(3);
      List<Integer> joltages = new ArrayList<>();
      String[] jss = joltageString.split(",");
      int maxJoltage = 0;
      for (String js : jss) {
        int joltage = Integer.parseInt(js); 
        joltages.add(joltage);
        maxJoltage = Math.max(maxJoltage, joltage);
      }

      int[] deps = new int[lights.length];
      for (List<Integer> buttons : toggles) {
        for (Integer button : buttons) {
          deps[button]++;
        }
      }

      machines.add(new Machine(lights, toggles, joltages, maxJoltage, deps));
    }

    final AtomicLong atomicAnswer = new AtomicLong(0);
    List<Future<Void>> results = new ArrayList<>();
    for (int i = 0; i < machines.size(); i++) {
      final Machine machine = machines.get(i);
      final int counter = i;
      results.add(executorService.submit(new Callable<Void>() {
        @Override
        public Void call() {
          int presses = solve(machine);
          System.out.println(counter + ": " + presses);
          atomicAnswer.getAndAdd(presses);
          return null;
        }
      }));
    }
    
    for (Future<Void> f : results) {
      f.get();
    }

    long answer = atomicAnswer.longValue();

    System.out.println("answer is " + answer);     
    
    System.out.println("time taken " + (clock.millis() - startTime) + "ms");

    executorService.shutdown();
  }
}
