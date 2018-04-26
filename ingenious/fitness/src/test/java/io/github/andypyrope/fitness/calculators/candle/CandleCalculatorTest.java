package io.github.andypyrope.fitness.calculators.candle;

import io.github.andypyrope.fitness.calculators.Calculator;
import io.github.andypyrope.fitness.calculators.InvalidDnaException;
import io.github.andypyrope.fitness.data.candle.Candle;
import io.github.andypyrope.platform.dna.Dna;
import io.github.andypyrope.platform.settings.numeric.IntSetting;
import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CandleCalculatorTest {

   @SuppressWarnings("MismatchedReadAndWriteOfArray")
   private static final double[] DEFAULT_READ_DOUBLE_RESULTS = new double[]{};
   private static final int[] DEFAULT_READ_BOUND_INT_RESULTS = new int[]{5, 2,
         1, 4};

   private static final double[][] DEFAULT_CANDLE_VALUES = new double[][]{
         new double[]{1.01, 2, 3, 4, 5, 6, 7, 8},
         new double[]{3.32, 5.62, 1.12, 1, 2, 3, 4, 5, 6, 7}};

   private Dna _dna;
   private Candle[][] _candles;

   private Class<? extends Exception> _exceptionClass;
   private Exception _exception;

   private final CandleCalculatorSettings _settings = new CandleCalculatorSettings();

   @BeforeEach
   void setUp() {
      setDnaMock();
      setCandleMocks();

      ((IntSetting) _settings.getSettings()[0]).setValue(3);
   }

   @Test
   void testCreationWithNull() {
      assertNull(tryToCreate(null));
      assertEquals(InvalidDnaException.class, _exceptionClass);
      assertEquals("Cannot instantiate SimpleCalculator with null DNA",
            _exception.getMessage());
   }

   @Test
   void testGetFitness() {
      final Calculator calculator = tryToCreate();
      final double fitness = calculator.getFitness();
      assertEquals(fitness, calculator.getFitness());

      EasyMock.verify(_dna);
   }

   @Test
   void testGetStudyingComplexity() {
      assertEquals(24, tryToCreate().getStudyingComplexity());
   }

   @Test
   void testCanStudy() {
      assertTrue(tryToCreate().canStudy());
   }

   @Test
   void testStudy() {
      final Calculator calculator = tryToCreate();
      for (int i = 0; i < 10; i++) {
         calculator.study();
      }
   }

   private void setDnaMock() {
      _dna = EasyMock.createMock(Dna.class);

      //noinspection ConstantConditions
      for (double readDoubleResult : DEFAULT_READ_DOUBLE_RESULTS) {
         EasyMock.expect(_dna.readDouble()).andReturn(readDoubleResult);
      }
      for (int boundReadResult : DEFAULT_READ_BOUND_INT_RESULTS) {
         EasyMock.expect(_dna.read(EasyMock.anyInt(), EasyMock.anyInt()))
               .andReturn(boundReadResult);
      }
      _dna.resetReader();
      EasyMock.expectLastCall();
      EasyMock.replay(_dna);
   }

   private void setCandleMocks() {
      _candles = new Candle[DEFAULT_CANDLE_VALUES.length][];

      for (int i = 0; i < DEFAULT_CANDLE_VALUES.length; i++) {
         _candles[i] = new Candle[DEFAULT_CANDLE_VALUES[i].length];

         for (int j = 0; j < DEFAULT_CANDLE_VALUES[i].length; j++) {
            _candles[i][j] = EasyMock.createNiceMock(Candle.class);

            EasyMock.expect(_candles[i][j].getClosingPrice())
                  .andReturn(DEFAULT_CANDLE_VALUES[i][j]).anyTimes();
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
