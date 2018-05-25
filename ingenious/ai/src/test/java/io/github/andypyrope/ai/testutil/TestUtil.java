package io.github.andypyrope.ai.testutil;

import java.util.Arrays;
import java.util.List;
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

   public static void compareDoubleArrays(double[] expected, double[] actual) {
      compareDoubles(Arrays.stream(expected).boxed().collect(Collectors.toList()),
            Arrays.stream(actual).boxed().collect(Collectors.toList()));
   }

   private static void compareDoubles(List<Double> expected, List<Double> actual) {
      final String errorMessage = String.format(
            "Expected 'Double' array [%s] to be equal to [%s]",
            actual.stream().map(String::valueOf).collect(Collectors.joining(",")),
            expected.stream().map(String::valueOf).collect(Collectors.joining(", "))
      );

      if (expected.size() != actual.size()) {
         throw new RuntimeException(errorMessage);
      }

      for (int i = 0; i < expected.size(); i++) {
         if (expected.get(i) == null && actual.get(i) == null) {
            continue;
         }

         if ((expected.get(i) == null) != (actual.get(i) == null)
               ||
               Math.abs(expected.get(i) - actual.get(i)) > DOUBLE_COMPARISON_PRECISION) {

            throw new RuntimeException(errorMessage);
         }
      }
   }
}
