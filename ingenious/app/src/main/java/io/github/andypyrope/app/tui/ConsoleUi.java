package io.github.andypyrope.app.tui;

import io.github.andypyrope.evolution.worlds.World;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ConsoleUi {

   private static final Pattern COMMAND_HELP = Pattern.compile("help");
   private static final Pattern COMMAND_ITERATE = Pattern
         .compile("iterate ?(\\d+)?");
   private static final Pattern COMMAND_EXIT = Pattern.compile("exit");
   private final World _world;
   private final Scanner _scanner;

   ConsoleUi(World world, Scanner scanner) {
      _world = world;
      _scanner = scanner;
   }

   void launch() {
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
         if (!matcher.find()) {
            return true;
         }
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
      System.out.println(_world.getInfo());
   }

   private String acceptInput() {
      return _scanner.nextLine();
   }
}
