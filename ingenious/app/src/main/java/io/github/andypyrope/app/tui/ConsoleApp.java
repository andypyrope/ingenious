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
      settings.setMaxHiddenLayers(10);
      settings.setMaxHiddenSize(40);
      settings.setOutputCandleCount(1);
      settings.setOutputCandleOffset(6);

      final World world = new SimpleWorld(
         new SimpleWorldSettings(20, 0.50, 100000L),
         new CandleCalculatorProvider(datasets, settings));

      new ConsoleInteractor(world, scanner).launch();
   }

}
