package io.github.andypyrope.app.tui;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.andypyrope.evolution.worlds.World;

public class ConsoleInteractor {

   private final World _world;
   private final Scanner _scanner = new Scanner(System.in);

   private static final Pattern COMMAND_HELP = Pattern.compile("help");
   private static final Pattern COMMAND_ITERATE = Pattern
            .compile("iterate ?(\\d+)?");
   private static final Pattern COMMAND_EXIT = Pattern.compile("exit");

   public ConsoleInteractor(World world) {
      _world = world;
   }

   public void launch() {
      printWorldInfo();
      System.out.print(" >> ");
      while (acceptCommand()) {
         System.out.print(" >> ");
      }
      System.out.println("Bye!");
   }

   private boolean acceptCommand() {
      final String command = acceptInput().trim();

      if (COMMAND_HELP.matcher(command).matches()) {
         showHelp();
      } else if (COMMAND_ITERATE.matcher(command).matches()) {
         final Matcher matcher = COMMAND_ITERATE.matcher(command);
         matcher.find();
         final String iterations = matcher.group(1);
         iterate(iterations == null ? 1 : Integer.parseInt(iterations));
      } else if (COMMAND_EXIT.matcher(command).matches()) {
         return false;
      } else {
         System.out.println("Incorrect command");
      }
      return true;
   }

   private void showHelp() {
      System.out.println("Sorry bruh");
   }

   private void iterate(int times) {
      _world.iterate(times);
      printWorldInfo();
   }

   private void printWorldInfo() {
      System.out.println(
         String.format("Gen %02d. Fitness: %3.1f %3.1f %3.1f %3.1f. Population: %d",
            _world.getGeneration(),
            _world.getMinFitness(),
            _world.getMeanFitness(),
            _world.getMedianFitness(),
            _world.getMaxFitness(),
            _world.size()));
   }

   private String acceptInput() {
      return _scanner.nextLine();
   }
}
