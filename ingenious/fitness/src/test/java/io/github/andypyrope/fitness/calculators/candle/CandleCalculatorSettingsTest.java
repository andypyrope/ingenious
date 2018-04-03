package io.github.andypyrope.fitness.calculators.candle;

import static org.junit.jupiter.api.Assertions.*;

import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;

class CandleCalculatorSettingsTest {

   @Test
   void test() {
      final CandleCalculatorSettings settings = new CandleCalculatorSettings();
      assertTrue(settings.getMinInputSize() < settings.getMaxInputSize());
      assertTrue(settings.getMinHiddenLayers() < settings.getMaxHiddenLayers());
      assertTrue(settings.getMinHiddenSize() < settings.getMaxHiddenSize());
      assertTrue(settings.getMaxVolatility() > 0.0);
      assertTrue(
         settings.getMinPassesPerInput() < settings.getMaxPassesPerInput());
      assertTrue(settings.getPassesWhenGettingFitness() > 0);
      assertTrue(settings.getOutputCandleCount() > 0);
      assertEquals(1, settings.getCandleDistance());
      assertEquals(ChronoUnit.DAYS, settings.getCandleDistanceUnit());

      settings.setMinInputSize(2);
      settings.setMaxInputSize(4);
      settings.setMinHiddenLayers(2);
      settings.setMaxHiddenLayers(53);
      settings.setMinHiddenSize(3);
      settings.setMaxHiddenSize(5);
      settings.setMaxVolatility(0.3);
      settings.setMinPassesPerInput(2);
      settings.setMaxPassesPerInput(43);

      settings.setPassesWhenGettingFitness(35);
      settings.setOutputCandleCount(12);

      settings.setCandleDistance(3);
      settings.setCandleDistanceUnit(ChronoUnit.CENTURIES);
   }

}
