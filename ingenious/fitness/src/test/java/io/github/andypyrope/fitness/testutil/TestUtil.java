package io.github.andypyrope.fitness.testutil;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestUtil {

   private static final double DOUBLE_COMPARISON_PRECISION = 0.00001;

   public static void compareDoubles(double expected, double actual) {
      if (Math.abs(expected - actual) > DOUBLE_COMPARISON_PRECISION) {
         throw new RuntimeException(
               String.format("Expected '%f' to be '%f'", actual, expected));
      }
      assertTrue(Math.abs(expected - actual) < DOUBLE_COMPARISON_PRECISION);
   }

   public static void compareDoubleArrays(Double[] first, Double[] second) {
      for (int i = 0; i < first.length; i++) {
         if (first[i] == null && second[i] == null) {
            continue;
         }

         if ((first[i] == null) != (second[i] == null)
               || Math.abs(first[i] - second[i]) > DOUBLE_COMPARISON_PRECISION) {

            throw new RuntimeException(String.format(
                  "Expected the 'Double' arrays [%s] and [%s] to be equal",
                  Arrays.stream(first).map(String::valueOf).collect(
                        Collectors.joining(", ")),
                  Arrays.stream(second).map(String::valueOf).collect(
                        Collectors.joining(",")))
            );
         }
      }
   }
}
