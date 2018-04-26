package io.github.andypyrope.ai.activation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReLuFunctionTest {

   @Test
   void test() {
      final ActivationFunction function = new ReLuFunction();

      assertEquals(4.8, function.getOutput(4.8));
      assertEquals(0.0, function.getOutput(-142.0));

      assertEquals(1.0, function.getSlope(2.0, 2.0));
      assertEquals(0.0, function.getSlope(-1.0, 0.0));
   }

}
