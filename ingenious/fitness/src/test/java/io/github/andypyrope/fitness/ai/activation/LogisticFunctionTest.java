package io.github.andypyrope.fitness.ai.activation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class LogisticFunctionTest {

   @Test
   void test() {
      final ActivationFunction function = new LogisticFunction();

      final int resolution = 100;
      final double from = -10.0;
      final double to = 10.0;
      final double step = (to - from) / resolution;

      double current = from;

      final double derivativeDelta = 0.0001;

      final double derivativeMaxError = 0.001;
      final double derivativeMax = 0.25;
      final double derivativeMin = 0.0;

      final double outputMax = 1.0;
      final double outputMin = 0.0;

      for (int i = 0; i < resolution; i++) {
         final double output = function.getOutput(current);
         final double offsetOutput = function
                  .getOutput(current + derivativeDelta);

         final double expected = (offsetOutput - output) / derivativeDelta;
         final double actual = function.getSlope(current, output);

         assertTrue(Math.abs(actual - expected) < derivativeMaxError);
         assertTrue(actual > derivativeMin);
         assertTrue(actual <= derivativeMax);

         assertTrue(output > outputMin);
         assertTrue(output < outputMax);

         current += step;
      }

      assertEquals(0.5, function.getOutput(0.0));
      assertEquals(0.25, function.getSlope(0.0, function.getOutput(0.0)));
   }

}
