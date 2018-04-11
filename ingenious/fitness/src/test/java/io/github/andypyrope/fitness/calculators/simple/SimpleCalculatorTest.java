package io.github.andypyrope.fitness.calculators.simple;

import io.github.andypyrope.fitness.calculators.Calculator;
import io.github.andypyrope.fitness.calculators.InvalidDnaException;
import io.github.andypyrope.fitness.testutil.TestUtil;
import io.github.andypyrope.platform.dna.Dna;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimpleCalculatorTest {

   private Class<? extends Exception> _exceptionClass;

   @Test
   void testCreationWithNull() {
      assertNull(tryToCreate(null));
      assertEquals(InvalidDnaException.class, _exceptionClass);
   }

   @Test
   void testGetFitness() {
      final Dna dnaMock = EasyMock.createNiceMock(Dna.class);
      final int dnaSize = 8;
      final double fitness = dnaSize * 2;

      EasyMock.expect(dnaMock.size()).andReturn(dnaSize).times(1);
      EasyMock.expect(dnaMock.read()).andReturn(5).times(dnaSize); // 2 ones

      EasyMock.replay(dnaMock);

      final Calculator calculator = tryToCreate(dnaMock);
      assertNotNull(calculator);
      TestUtil.compareDoubles(fitness, calculator.getFitness());
      TestUtil.compareDoubles(fitness, calculator.getFitness());

      EasyMock.verify(dnaMock);
   }

   @Test
   void testGetStudyingComplexity() {
      assertEquals(0, tryToCreate().getStudyingComplexity());
   }

   @Test
   void testCanStudy() {
      assertFalse(tryToCreate().canStudy());
   }

   @Test
   void testStudy() {
      tryToCreate().study();
   }

   private Calculator tryToCreate() {
      return tryToCreate(EasyMock.createNiceMock(Dna.class));
   }

   private Calculator tryToCreate(Dna dna) {
      try {
         return new SimpleCalculator(dna);
      } catch (InvalidDnaException e) {
         _exceptionClass = e.getClass();
         return null;
      }
   }
}
