package io.github.andypyrope.fitness.calculators.candle;

import io.github.andypyrope.fitness.calculators.Calculator;
import io.github.andypyrope.fitness.calculators.InvalidCalculatorSettingsException;
import io.github.andypyrope.platform.dna.Dna;
import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CandleCalculatorProviderTest {

   private Exception _exception;
   private String[] _datasets = new String[] {
      "datasets/candle/two-candles.csv" };
   private final CandleCalculatorSettings _settings = new CandleCalculatorSettings();

   private Dna _dna;

   @BeforeEach
   void setUp() {
      _dna = EasyMock.createMock(Dna.class);
      EasyMock.expect(_dna.read()).andReturn(1).anyTimes();
      EasyMock.expect(_dna.readDouble()).andReturn(0.5).anyTimes();
      EasyMock.expect(_dna.read(EasyMock.anyInt(), EasyMock.anyInt()))
            .andReturn(5).anyTimes();
      EasyMock.replay(_dna);
      _settings.setMaxInputCandles(1);
      _settings.setOutputCandleCount(1);
   }

   @Test
   void testProvide() throws InvalidCalculatorSettingsException {
      final Calculator calculator = new CandleCalculatorProvider(
         _datasets,
         _settings).provide(_dna);
      assertNotNull(calculator);
   }

   @Test
   void testProvideWithInvalidSettings() {
      _settings.setOutputCandleCount(20);
      assertNull(getProvider());
      assertEquals(
         "The size of dataset 'datasets/candle/two-candles.csv', 2, is " +
               "smaller than the sum of the maximum input nodes, 1, " +
               "and the number of output nodes, 20, plus their offset, 0",
         _exception.getMessage());
      assertEquals(InvalidCalculatorSettingsException.class,
         _exception.getClass());
   }

   @Test
   void testGetDesiredDnaLength() {
      assertEquals(13, getProvider().getDesiredDnaLength());

      _settings.setMaxHiddenLayers(100);
      assertEquals(103, getProvider().getDesiredDnaLength());
   }

   @Test
   void testGetMaxStudyingComplexity() {
      assertEquals(5675, getProvider().getMaxStudyingComplexity());

      _settings.setMaxHiddenLayers(100);
      assertEquals(61925, getProvider().getMaxStudyingComplexity());

      _settings.setOutputCandleCount(0);
      assertEquals(61900, getProvider().getMaxStudyingComplexity());

      _settings.setMaxInputCandles(2);
      assertEquals(61925, getProvider().getMaxStudyingComplexity());
   }

   @Test
   void testWithDatasets() {
      _datasets = new String[] { "i-do-not-exist.csv", "welp" };
      assertNull(getProvider());
      assertEquals("Data file 'i-do-not-exist.csv' does not exist",
         _exception.getMessage());
   }

   private CandleCalculatorProvider getProvider() {
      try {
         return new CandleCalculatorProvider(_datasets, _settings);
      } catch (Exception e) {
         _exception = e;
         return null;
      }
   }
}
