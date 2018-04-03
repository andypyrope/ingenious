package io.github.andypyrope.fitness.calculators.simple;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import io.github.andypyrope.platform.dna.Dna;

public class SimpleCalculatorProviderTest {

   @Test
   public void testProvide() {
      assertNotNull(new SimpleCalculatorProvider()
            .provide(EasyMock.createNiceMock(Dna.class)));
   }

   @Test
   public void testGetDesiredDnaLength() {
      assertEquals(50, new SimpleCalculatorProvider().getDesiredDnaLength());
   }

   @Test
   public void testGetMaxStudyingComplexity() {
      assertEquals(0,
         new SimpleCalculatorProvider().getMaxStudyingComplexity());
   }
}
