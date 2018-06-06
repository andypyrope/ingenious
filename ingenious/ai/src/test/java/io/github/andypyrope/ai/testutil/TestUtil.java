package io.github.andypyrope.ai.testutil;

import org.junit.jupiter.api.Assertions;

import java.util.Arrays;
import java.util.stream.Collectors;

public class TestUtil {

   private static final double LOW_END_MULTIPLIER = 0.9;
   private static final double HIGH_END_MULTIPLIER = 1.1;

   private static void compareDoubles(final double minValue, final double maxValue,
         final double actual) {

      Assertions.assertTrue(actual < maxValue + Double.MIN_NORMAL, String.format(
            "The actual value - %.28f - should be at most %.28f", actual, maxValue));
      Assertions.assertTrue(actual > minValue - Double.MIN_NORMAL, String.format(
            "The actual value - %.28f - should be at least %.28f", actual, minValue));
   }

   public static void compareDoubles(final double expected, final double actual) {
      if (expected > 0) {
         compareDoubles(expected * LOW_END_MULTIPLIER - Double.MIN_NORMAL,
               expected * HIGH_END_MULTIPLIER + Double.MIN_NORMAL, actual);
      } else {
         compareDoubles(expected * HIGH_END_MULTIPLIER - Double.MIN_NORMAL,
               expected * LOW_END_MULTIPLIER + Double.MIN_NORMAL, actual);
      }
   }

   public static void compareDoubleArrays(final double[] expected,
         final double[] actual) {
      final String errorMessage = String.format(
            "Expected 'double' array [%s] to be equal to [%s]",
            Arrays.stream(actual).boxed().map(String::valueOf)
                  .collect(Collectors.joining(",")),
            Arrays.stream(expected).boxed().map(String::valueOf)
                  .collect(Collectors.joining(", "))
      );

      if (expected.length != actual.length) {
         throw new AssertionError(errorMessage + " -- mismatching length");
      }

      for (int i = 0; i < expected.length; i++) {
         try {
            compareDoubles(expected[i], actual[i]);
         } catch (AssertionError e) {
            throw new AssertionError(errorMessage + " -- mismatch at index " + i, e);
         }
      }
   }
}
