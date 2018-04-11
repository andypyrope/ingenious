package io.github.andypyrope.fitness.calculators.simple;

import io.github.andypyrope.platform.dna.Dna;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SimpleCalculatorProviderTest {

   @Test
   void testProvide() {
      assertNotNull(new SimpleCalculatorProvider()
            .provide(EasyMock.createNiceMock(Dna.class)));
   }

   @Test
   void testGetDesiredDnaLength() {
      assertEquals(50, new SimpleCalculatorProvider().getDesiredDnaLength());
   }

   @Test
   void testGetMaxStudyingComplexity() {
      assertEquals(0,
         new SimpleCalculatorProvider().getMaxStudyingComplexity());
   }
}
