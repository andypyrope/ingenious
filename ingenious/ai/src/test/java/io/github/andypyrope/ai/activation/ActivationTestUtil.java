package io.github.andypyrope.ai.activation;

import io.github.andypyrope.ai.testutil.TestUtil;
import org.junit.jupiter.api.Assertions;

class ActivationTestUtil {

   static void testSlope(final ActivationFunction function, final double maxSlope) {

      final double resolution = 100;
      final double from = -10.0;
      final double to = 10.0;
      final double step = (to - from) / resolution;
      double position = from;

      double maxActualSlope = -1;
      double maxActualSlopePos = 0.0;

      for (int i = 0; i <= resolution; i++) {
         final double difference = function.getOutput(position + step / 2) -
               function.getOutput(position - step / 2);
         final double expectedSlope = difference / step;
         final double actualSlope = function.getSlope(position,
               function.getOutput(position));

         Assertions.assertTrue(actualSlope > 0.0 - Double.MIN_NORMAL,
               "The slope is at least 0.0 (the function is non-decreasing)");
         TestUtil.compareDoublesLoose(expectedSlope, actualSlope);

         if (actualSlope > maxActualSlope) {
            maxActualSlope = actualSlope;
            maxActualSlopePos = position;
         }
         position += step;
      }

      Assertions.assertTrue(maxActualSlope < maxSlope + 0.00000000001, String.format(
            "The max slope (%f), encountered at position %f, should be at most %f",
            maxActualSlope, maxActualSlopePos, maxSlope));
   }
}
