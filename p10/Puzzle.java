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

  private static final Pattern EXTRACT_REGEX = Pattern.compile("\\[([#\\.]+)\\]\\s*([\\d\\(\\),\\s]+)\\s*\\{([\\d,\\s]+)\\}");
  
  record Machine(boolean[] lights, List<boolean[]> toggles, List<Integer> joltages) {}

  public static boolean compareState(boolean[] a, boolean[] b) {
    for (int i = 0; i < a.length; i++) {
      if (a[i] != b[i]) {
        return false;
      }
    }
    return true;
  }

  public static void doToggle(boolean[] state, boolean[] button) {
    for (int i = 0; i < state.length; i++) {
      state[i] ^= button[i];
    }
  }

  public static int countToggles(Machine machine, boolean[] state, int button) {
    if (button >= machine.toggles().size()) {
      return compareState(state, machine.lights()) ? 0 : 999999;
    }

    boolean[] stateDont = Arrays.copyOf(state, state.length);
    boolean[] stateDo = Arrays.copyOf(state, state.length);
    doToggle(stateDo, machine.toggles().get(button));

    return Math.min(countToggles(machine, stateDont, button + 1), countToggles(machine, stateDo, button + 1) + 1);
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
      List<boolean[]> toggles = new ArrayList<>();
      String[] tss = toggleString.split("\\s+");
      for (String ts : tss) {
        String[] ss = ts.substring(1, ts.length() - 1).split(",");
        boolean[] toggle = new boolean[lightString.length()];
        for (String s : ss) {
          toggle[Integer.parseInt(s)] = true;
        }
        toggles.add(toggle);
      }

      String joltageString = m.group(3);
      List<Integer> joltages = new ArrayList<>();
      String[] jss = joltageString.split(",");
      for (String js : jss) {
        joltages.add(Integer.parseInt(js));
      }

      machines.add(new Machine(lights, toggles, joltages));
    }

    long answer = 0;
    for (Machine machine : machines) {
      boolean[] state = new boolean[machine.lights().length];
      int presses = countToggles(machine, state, 0);
      //System.out.println(presses);
      answer += presses;
    }
    
    System.out.println("answer is " + answer);     
    
    System.out.println("time taken " + (clock.millis() - startTime) + "ms");
  }
}
