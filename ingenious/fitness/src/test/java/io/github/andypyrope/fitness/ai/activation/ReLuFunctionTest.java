package io.github.andypyrope.fitness.ai.activation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

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
