package io.github.andypyrope.app.tui;

import java.util.Scanner;

import io.github.andypyrope.evolution.worlds.World;
import io.github.andypyrope.evolution.worlds.simple.SimpleWorld;
import io.github.andypyrope.evolution.worlds.simple.SimpleWorldSettings;
import io.github.andypyrope.fitness.calculators.InvalidCalculatorSettingsException;
import io.github.andypyrope.fitness.calculators.candle.CandleCalculatorProvider;
import io.github.andypyrope.fitness.calculators.candle.CandleCalculatorSettings;

public class ConsoleApp {

   public static void main(String[] args)
      throws InvalidCalculatorSettingsException {

      final Scanner scanner = new Scanner(System.in);
      System.out.print("Where is the dataset: ");
      final String datasetPath = scanner.nextLine().trim();

      final String[] datasets = new String[] { "datasets/" + datasetPath };
      final CandleCalculatorSettings settings = new CandleCalculatorSettings();
      settings.setMaxHiddenLayers(5);
      settings.setMaxHiddenSize(20);
      settings.setMaxVolatility(1000);

      final World world = new SimpleWorld(
         new SimpleWorldSettings(20, 0.50, 1000000L),
         new CandleCalculatorProvider(datasets, settings));

      new ConsoleInteractor(world, scanner).launch();
   }

}
