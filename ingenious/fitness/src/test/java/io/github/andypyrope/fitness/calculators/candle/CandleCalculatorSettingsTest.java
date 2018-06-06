package io.github.andypyrope.fitness.calculators.candle;

import io.github.andypyrope.fitness.calculators.CalculatorSettings;
import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CandleCalculatorSettingsTest {

   @Test
   void test() {
      final CandleCalculatorSettings settings = new CandleCalculatorSettings();
      assertTrue(settings.getMinInputCandles() < settings.getMaxInputCandles());
      assertTrue(settings.getMinHiddenLayers() < settings.getMaxHiddenLayers());
      assertTrue(settings.getMinHiddenSize() < settings.getMaxHiddenSize());
      assertTrue(settings.getMaxVolatility() > 0.0);
      assertTrue(
            settings.getMinPassesPerInput() < settings.getMaxPassesPerInput());
      assertTrue(settings.getPassesWhenGettingFitness() > 0);
      assertTrue(settings.getOutputCandleCount() > 0);
      assertEquals(0, settings.getOutputCandleOffset());
      settings.setOutputCandleOffset(8);
      assertEquals(8, settings.getOutputCandleOffset());
      assertEquals(1, settings.getCandleDistance());
      assertEquals(ChronoUnit.DAYS, settings.getCandleDistanceUnit());

      settings.setMinInputCandles(2);
      settings.setMaxInputCandles(4);
      settings.setMinHiddenLayers(2);
      settings.setMaxHiddenLayers(53);
      settings.setMinHiddenSize(3);
      settings.setMaxHiddenSize(5);
      settings.setMaxVolatility(0.3);

      settings.setPassesWhenGettingFitness(35);

      settings.setCandleDistance(3);
      settings.setCandleDistanceUnit(ChronoUnit.CENTURIES);
   }

   @Test
   void testGetSettings() {
      final CalculatorSettings settings = new CandleCalculatorSettings();
      assertEquals(3, settings.getSettings().length);
   }
}
