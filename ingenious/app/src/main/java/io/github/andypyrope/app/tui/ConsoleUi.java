package io.github.andypyrope.app.tui;

import io.github.andypyrope.evolution.worlds.World;
import io.github.andypyrope.platform.settings.InvalidValueException;
import io.github.andypyrope.platform.settings.Setting;
import io.github.andypyrope.platform.settings.numeric.IntSetting;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ConsoleUi {

   private static final Pattern COMMAND_HELP = Pattern.compile("help");
   private static final Pattern COMMAND_ITERATE = Pattern
         .compile("iterate ?(\\d+)?");
   private static final Pattern COMMAND_EXIT = Pattern.compile("exit");
   private World _world;
   private Setting[] _settings;
   private Map<String, Setting> _settingMap;
   private final Scanner _scanner;

   ConsoleUi(Scanner scanner) {
      _scanner = scanner;
   }

   void configure(Setting[] settings) {
      _settings = settings;
      _settingMap = new HashMap<>();
      for (final Setting setting : settings) {
         _settingMap.put(setting.getId(), setting);
      }
      do {
         printSettings();
      } while (acceptConfiguration());
   }

   private void printSettings() {
      System.out.println("Configure the following settings and say 'ok' to confirm:");
      for (final Setting setting : _settings) {
         if (setting instanceof IntSetting) {
            System.out.println(
                  String.format("[int]['%s'] %s: %d", setting.getId(), setting.getLabel(),
                        ((IntSetting) setting).getValue()));
         } else {
            System.out.println("[???] - " + setting.getClass().getCanonicalName());
         }
      }
   }

   private boolean acceptConfiguration() {
      System.out.println("Key?");
      final String key = acceptInput().trim();
      if (key.equals("ok")) {
         return false;
      }
      final Setting setting = _settingMap.get(key);
      if (setting == null) {
         System.out.println("Setting '" + key + "' does not exist. Try again");
         return true;
      }

      if (setting instanceof IntSetting) {
         System.out.println("Value? (integer)");
      } else {
         System.out.println("Sorry, I don't support the type " +
               setting.getClass().getCanonicalName() + ". Try again.");
         return true;
      }

      System.out.println("Value?");
      final String value = acceptInput().trim();
      try {
         ((IntSetting) setting).setValue(Integer.parseInt(value));
      } catch (InvalidValueException | NumberFormatException e) {
         System.out.println("This value doesn't seem to be valid: " + e.getMessage());
      }
      return true;
   }

   void launch(World world) {
      _world = world;
      printWorldInfo();
      while (true) {
         if (!acceptCommand()) break;
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
      System.out.print(" >> ");
      return _scanner.nextLine();
   }
}
