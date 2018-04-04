package io.github.andypyrope.fitness.calculators.candle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.andypyrope.fitness.calculators.Calculator;
import io.github.andypyrope.fitness.calculators.InvalidDnaException;
import io.github.andypyrope.fitness.data.candle.Candle;
import io.github.andypyrope.platform.dna.Dna;

public class CandleCalculatorTest {

   private static final int[] DEFAULT_READ_RESULTS = new int[] {};
   private static final double[] DEFAULT_READ_DOUBLE_RESUTLS = new double[] {};
   private static final int[] DEFAULT_READ_BOUND_INT_RESUTLS = new int[] { 5, 2,
      1, 4 };

   private static final double[][] DEFAULT_CANDLE_VALUES = new double[][] {
      new double[] { 1.01, 2, 3, 4, 5, 6, 7, 8 },
      new double[] { 3.32, 5.62, 1.12, 1, 2, 3, 4, 5, 6, 7 } };

   private Dna _dna;
   private Candle[][] _candles;

   private Class<? extends Exception> _exceptionClass;
   private Exception _exception;

   private final CandleCalculatorSettings _settings = new CandleCalculatorSettings();

   @BeforeEach
   public void setUp() {
      setDnaMock(DEFAULT_READ_RESULTS,
         DEFAULT_READ_DOUBLE_RESUTLS,
         DEFAULT_READ_BOUND_INT_RESUTLS);
      setCandleMocks(DEFAULT_CANDLE_VALUES);

      _settings.setOutputCandleCount(3);
   }

   @Test
   public void testCreationWithNull() {
      assertNull(tryToCreate(null));
      assertEquals(_exceptionClass, InvalidDnaException.class);
      assertEquals("Cannot instantiate SimpleCalculator with null DNA",
         _exception.getMessage());
   }

   @Test
   public void testGetFitness() {
      final Calculator calculator = tryToCreate();
      final double fitness = calculator.getFitness();
      assertEquals(fitness, calculator.getFitness());

      EasyMock.verify(_dna);
   }

   @Test
   public void testGetStudyingComplexity() {
      assertEquals(32, tryToCreate().getStudyingComplexity());
   }

   @Test
   public void testCanStudy() {
      assertEquals(true, tryToCreate().canStudy());
   }

   @Test
   public void testStudy() {
      final Calculator calculator = tryToCreate();
      for (int i = 0; i < 10; i++) {
         calculator.study();
      }
   }

   private void setDnaMock(int[] readResults, double[] readDoubleResults,
      int[] boundReadResults) {

      _dna = EasyMock.createMock(Dna.class);

      for (int readResult : readResults) {
         EasyMock.expect(_dna.read()).andReturn(readResult);
      }
      for (double readDoubleResult : readDoubleResults) {
         EasyMock.expect(_dna.readDouble()).andReturn(readDoubleResult);
      }
      for (int boundReadResult : boundReadResults) {
         EasyMock.expect(_dna.read(EasyMock.anyInt(), EasyMock.anyInt()))
               .andReturn(boundReadResult);
      }
      EasyMock.replay(_dna);
   }

   private void setCandleMocks(double[][] values) {
      _candles = new Candle[values.length][];

      for (int i = 0; i < values.length; i++) {
         _candles[i] = new Candle[values[i].length];

         for (int j = 0; j < values[i].length; j++) {
            _candles[i][j] = EasyMock.createNiceMock(Candle.class);

            EasyMock.expect(_candles[i][j].getClosingPrice())
                  .andReturn(values[i][j]).anyTimes();
            EasyMock.replay(_candles[i][j]);
         }
      }
   }

   private Calculator tryToCreate() {
      return tryToCreate(_dna);
   }

   private Calculator tryToCreate(Dna dna) {
      try {
         return new CandleCalculator(dna, _candles, _settings);
      } catch (InvalidDnaException e) {
         _exception = e;
         _exceptionClass = e.getClass();
         return null;
      }
   }
}
