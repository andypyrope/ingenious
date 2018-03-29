package io.github.andypyrope.app.tui;

import io.github.andypyrope.evolution.worlds.World;
import io.github.andypyrope.evolution.worlds.simple.SimpleWorld;
import io.github.andypyrope.evolution.worlds.simple.SimpleWorldSettings;
import io.github.andypyrope.fitness.calculators.simple.SimpleCalculatorProvider;

public class ConsoleApp {

   public static void main(String[] args) {
      final World world = new SimpleWorld(
         new SimpleWorldSettings(50, 0.25),
         new SimpleCalculatorProvider());

      new ConsoleInteractor(world).launch();
   }

}
