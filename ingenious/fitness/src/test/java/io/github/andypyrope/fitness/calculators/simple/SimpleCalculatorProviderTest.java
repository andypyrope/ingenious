package io.github.andypyrope.fitness.calculators.simple;

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
}
