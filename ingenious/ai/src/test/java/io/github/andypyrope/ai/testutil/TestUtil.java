package io.github.andypyrope.ai.testutil;

import io.github.andypyrope.ai.data.RasterData;
import io.github.andypyrope.ai.raster.RasterLayer;
import org.junit.jupiter.api.Assertions;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;
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

   public static void compareRasterDataArrays(final RasterData[] expected,
         final RasterData[] actual) {

      if (expected.length != actual.length) {
         throw new AssertionError("Expected array with size " + actual.length +
               " to have size " + expected.length);
      }

      for (int i = 0; i < expected.length; i++) {
         try {
            compareRasterData(expected[i], actual[i]);
         } catch (final AssertionError e) {
            throw new AssertionError("Mismatch at index " + i, e);
         }
      }
   }

   private static void compareRasterData(final RasterData expected,
         final RasterData actual) {
      final String errorMessage = String.format(
            "Expected  raster data '%s' to be equal to '%s'",
            rasterDataToString(actual), rasterDataToString(expected));

      actual.verifyDimensions(expected.getWidth(), expected.getHeight(),
            expected.getDepth());

      expected.forEach((x, y, z) -> {
         try {
            compareDoubles(expected.getCell(x, y, z), actual.getCell(x, y, z));
         } catch (AssertionError e) {
            throw new AssertionError(
                  String.format(errorMessage + " -- mismatch at (%d,%d,%d)", x, y, z));
         }
      });
   }

   private static String rasterDataToString(final RasterData data) {
      final StringBuilder result = new StringBuilder(data.getWidth() *
            data.getHeight() * data.getDepth() * 5);
      for (int z = 0; z < data.getDepth(); z++) {
         result.append("[");
         for (int y = 0; y < data.getHeight(); y++) {
            result.append("(");
            for (int x = 0; x < data.getWidth(); x++) {
               result.append(data.getCell(x, y, z)).append(",");
            }
            result.append(")");
         }
         result.append("]");
      }
      return result.toString();
   }

   public static void expectException(final Class<? extends Exception> exceptionClass,
         final Runnable runnable) {

      Exception exception = null;
      try {
         runnable.run();
      } catch (final Exception e) {
         exception = e;
      }
      Assertions.assertNotNull(exception, "An exception has been thrown");
      Assertions.assertSame(exceptionClass, exception.getClass(),
            String.format("The caught exception is of type '%s'",
                  exceptionClass.getSimpleName()));
   }

   public static <T> void assertArraySame(final T[] expected, final T[] actual) {
      if (expected.length != actual.length) {
         throw new AssertionError(String.format("Expected array to have a length " +
                     "of %d but it has a length of %d instead",
               expected.length, actual.length));
      }

      for (int i = 0; i < expected.length; i++) {
         Assertions.assertSame(expected[i], actual[i]);
      }
   }

   public static double getEuclideanDistance(final RasterLayer layer,
         final RasterData[] targetOutput) {

      final RasterData[] output = layer.getOutputAsRaster();
      AtomicReference<Double> squaredDistance = new AtomicReference<>(0.0);
      for (int i = 0; i < output.length; i++) {
         final RasterData currentOutput = output[i];
         final RasterData currentTargetOutput = targetOutput[i];

         currentOutput.forEach((x, y, z) -> {
            final double difference = currentOutput.getCell(x, y, z) -
                  currentTargetOutput.getCell(x, y, z);
            squaredDistance.set(squaredDistance.get() + difference * difference);
         });
      }
      return Math.sqrt(squaredDistance.get());
   }
}
