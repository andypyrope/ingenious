package io.github.andypyrope.platform.testutil;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestUtil {

   private static final double DOUBLE_COMPARISON_PRECISION = 0.0000001;

   public static void compareDoubles(double expected, double actual) {
      if (Math.abs(expected - actual) > DOUBLE_COMPARISON_PRECISION) {
         throw new RuntimeException(
               String.format("Expected '%f' to be '%f'", actual, expected));
      }
      assertTrue(Math.abs(expected - actual) < DOUBLE_COMPARISON_PRECISION);
   }
}
