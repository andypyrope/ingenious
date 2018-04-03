package io.github.andypyrope.fitness.calculators.simple;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import io.github.andypyrope.fitness.calculators.Calculator;
import io.github.andypyrope.fitness.calculators.InvalidDnaException;
import io.github.andypyrope.platform.dna.Dna;

public class SimpleCalculatorTest {

   private Class<? extends Exception> _exceptionClass;

   @Test
   public void testCreationWithNull() {
      assertNull(tryToCreate(null));
      assertEquals(_exceptionClass, InvalidDnaException.class);
   }

   @Test
   public void testGetFitness() {
      final Dna dnaMock = EasyMock.createNiceMock(Dna.class);
      final int dnaSize = 8;
      final double fitness = dnaSize * 2;

      EasyMock.expect(dnaMock.size()).andReturn(dnaSize).times(1);
      EasyMock.expect(dnaMock.read()).andReturn(5).times(dnaSize); // 2 ones

      EasyMock.replay(dnaMock);

      final Calculator calculator = tryToCreate(dnaMock);
      compareDoubles(fitness, calculator.getFitness());
      compareDoubles(fitness, calculator.getFitness());

      EasyMock.verify(dnaMock);
   }

   @Test
   public void testGetStudyingComplexity() {
      assertEquals(0, tryToCreate().getStudyingComplexity());
   }

   @Test
   public void testCanStudy() {
      assertEquals(false, tryToCreate().canStudy());
   }

   @Test
   public void testStudy() {
      tryToCreate().study();
   }

   private Calculator tryToCreate() {
      return tryToCreate(EasyMock.createNiceMock(Dna.class));
   }

   private void compareDoubles(double a, double b) {
      assertTrue(Math.abs(a - b) < 0.0000001);
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
