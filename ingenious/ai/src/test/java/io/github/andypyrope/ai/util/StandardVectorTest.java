package io.github.andypyrope.ai.util;

import io.github.andypyrope.ai.InvalidSizeException;
import io.github.andypyrope.ai.testutil.TestUtil;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

class StandardVectorTest {

   private static final double X_POS = 2.25;
   private static final double Y_POS = 3.5;
   private static final double Z_POS = 4.5;

   @Test
   void testPlus() {
      Assertions.assertEquals(asString(X_POS + 1, Y_POS + 1, Z_POS + 1),
            makeVector().plus(1).toString());
      Assertions.assertEquals(asString(X_POS + 1, Y_POS, Z_POS - 1),
            makeVector().plus(1, 0, -1).toString());
      Assertions.assertEquals(asString(X_POS + 0.75, Y_POS - 0.5, Z_POS - 1.5),
            makeVector().plus(dummyVector(0.75, -0.5, -1.5)).toString());
   }

   @Test
   void testMinus() {
      Assertions.assertEquals(asString(X_POS - 1, Y_POS - 1, Z_POS - 3),
            makeVector().minus(1, 1, 3).toString());
      Assertions.assertEquals(asString(X_POS - 1, Y_POS - 1, Z_POS - 3),
            makeVector().minus(dummyVector(1, 1, 3)).toString());
   }

   @Test
   void testMultipliedBy() {
      Assertions.assertEquals(asString(X_POS * 2, Y_POS * 2, Z_POS * 2),
            makeVector().multipliedBy(2).toString());
   }

   @Test
   void testDividedBy() {
      Assertions.assertEquals(asString(X_POS * 2, Y_POS, Z_POS / 2),
            makeVector().dividedBy(dummyVector(0.5, 1, 2)).toString());
   }

   @Test
   void testGetX() {
      Assertions.assertEquals(X_POS, makeVector().getX());
   }

   @Test
   void testGetY() {
      Assertions.assertEquals(Y_POS, makeVector().getY());
   }

   @Test
   void testGetZ() {
      Assertions.assertEquals(Z_POS, makeVector().getZ());
   }

   @Test
   void testGetPixelCount() {
      expectInvalidSizeException(() -> makeVector().getPixelCount());
      Assertions.assertEquals(2 * 3 * 4, new StandardVector(2, 3, 4).getPixelCount());
   }

   @Test
   void testToString() {
      Assertions.assertEquals("(2.22, 3.50, 4.13)",
            new StandardVector(2.222222, 3.5, 4.125).toString());
   }

   @Test
   void testDiffersFrom() {
      Assertions.assertTrue(makeVector()
            .differsFrom(new StandardVector(X_POS + 1, Y_POS, Z_POS)));
      Assertions.assertTrue(makeVector()
            .differsFrom(new StandardVector(X_POS, Y_POS + 1, Z_POS)));
      Assertions.assertTrue(makeVector()
            .differsFrom(new StandardVector(X_POS, Y_POS, Z_POS + 1)));

      Assertions.assertFalse(makeVector()
            .differsFrom(new StandardVector(X_POS, Y_POS, Z_POS)));
   }

   @Test
   void testValidateAsSize() {
      expectInvalidSizeException(() -> new StandardVector(0, 1, 1).validateAsSize());
      expectInvalidSizeException(() -> new StandardVector(1, 0, 1).validateAsSize());
      expectInvalidSizeException(() -> new StandardVector(1, 1, 0).validateAsSize());
      expectInvalidSizeException(() -> new StandardVector(1.5, 1, 1).validateAsSize());
      expectInvalidSizeException(() -> new StandardVector(1, 1.5, 1).validateAsSize());
      expectInvalidSizeException(() -> new StandardVector(1, 1, 1.5).validateAsSize());
      new StandardVector(1, 1, 1.0).validateAsSize();
   }

   @Test
   void testIsValidIndex() {
      final Vector[] invalid = new Vector[]{
            new StandardVector(0.000000001, 0, 1),
            new StandardVector(0, 2.5, 0),
            new StandardVector(0, -1, 0),
      };
      final Vector[] valid = new Vector[]{
            new StandardVector(0, 0, 0),
            new StandardVector(1, 1, 1),
            new StandardVector(3, 4, 5),
      };
      for (final Vector vector : invalid) {
         Assertions.assertFalse(vector.isValidIndex(),
               String.format("Vector %s should be invalid", vector.toString()));
      }
      for (final Vector vector : valid) {
         Assertions.assertTrue(vector.isValidIndex(),
               String.format("Vector %s should be valid", vector.toString()));
      }
   }

   @Test
   void testGetScanSize() {
      Assertions.assertEquals(asString(X_POS, Y_POS, Z_POS),
            makeVector().getScanSize(
                  dummyVector(1, 1, 1),
                  dummyVector(0, 0, 0),
                  dummyVector(1, 1, 1)
            ).toString());
      Assertions.assertEquals(asString(X_POS, Y_POS, Z_POS),
            makeVector().getScanSize(dummyVector(1, 1, 1), null, null).toString());
      Assertions.assertEquals(asString(X_POS / 2, Y_POS * 4, Z_POS),
            makeVector().getScanSize(
                  dummyVector(1, 1, 1),
                  dummyVector(0, 0, 0),
                  dummyVector(2, 0.25, 1)
            ).toString());
      Assertions.assertEquals(asString(X_POS - 1, Y_POS - 2, Z_POS - 3),
            makeVector().getScanSize(
                  dummyVector(2, 3, 4),
                  dummyVector(0, 0, 0),
                  dummyVector(1, 1, 1)
            ).toString());
      Assertions.assertEquals(asString(X_POS + 2, Y_POS + 4, Z_POS + 6),
            makeVector().getScanSize(
                  dummyVector(1, 1, 1),
                  dummyVector(1, 2, 3),
                  dummyVector(1, 1, 1)
            ).toString());
      Assertions.assertEquals(asString((X_POS - 2) / 2, (Y_POS - 1 + 2), (Z_POS + 4) * 2),
            makeVector().getScanSize(
                  dummyVector(3, 2, 1),
                  dummyVector(0, 1, 2),
                  dummyVector(2, 1, 0.5)
            ).toString());
   }

   @Test
   void testForEach() {
      expectInvalidSizeException(() -> makeVector().forEach(null));
      final AtomicInteger index = new AtomicInteger(0);
      final String[] expectedIndices = new String[]{
            "0 0 0",
            "1 0 0",
            "0 1 0",
            "1 1 0",
            "0 0 1",
            "1 0 1",
            "0 1 1",
            "1 1 1",
      };
      new StandardVector(2, 2, 2).forEach((x, y, z) -> {
         Assertions.assertEquals(expectedIndices[index.get()], x + " " + y + " " + z,
               "The iteration of index " + index.get() + " (starting from 1) is correct");
         index.addAndGet(1);
      });
   }

   @Test
   void testSlideWindow() {
      final String[] expectedPositions = new String[]{
            asString(-1.0, -1.0, -2.0),
            asString(-0.5, -1.0, -2.0),
            asString(0.00, -1.0, -2.0),
            asString(-1.0, 0.00, -2.0),
            asString(-0.5, 0.00, -2.0),
            asString(0.00, 0.00, -2.0),

            asString(-1.0, -1.0, 1.00),
            asString(-0.5, -1.0, 1.00),
            asString(0.00, -1.0, 1.00),
            asString(-1.0, 0.00, 1.00),
            asString(-0.5, 0.00, 1.00),
            asString(0.00, 0.00, 1.00),
      };
      final String[] expectedIndices = new String[]{
            "0 0 0",
            "1 0 0",
            "2 0 0",
            "0 1 0",
            "1 1 0",
            "2 1 0",
            "0 0 1",
            "1 0 1",
            "2 0 1",
            "0 1 1",
            "1 1 1",
            "2 1 1",
      };
      final List<Vector> actualPositions = new ArrayList<>();
      final List<String> actualIndices = new ArrayList<>();
      new StandardVector(2, 3, 7).slideWindow(
            dummyVector(3, 4, 6), // Window size
            dummyVector(1, 1, 2), // Padding
            dummyVector(0.5, 1.0, 3.0), // Stride
            (inputPosition, x, y, z) -> {
               actualPositions.add(inputPosition);
               actualIndices.add(x + " " + y + " " + z);
            });
      Assertions.assertEquals(expectedIndices.length, actualIndices.size());
      Assertions.assertEquals(expectedPositions.length, actualPositions.size());
      for (int i = 0; i < expectedPositions.length; i++) {
         Assertions.assertEquals(expectedPositions[i], actualPositions.get(i).toString(),
               "The input position " + i + " is correct");
         Assertions.assertEquals(expectedIndices[i], actualIndices.get(i),
               "The output index " + i + " is correct");
      }
   }

   @Test
   void testSlideWindowWithDefaultSettings() {
      final String[] expectedPositions = new String[]{
            asString(0.0, 0.0, 0.0),
            asString(1.0, 0.0, 0.0),
            asString(0.0, 1.0, 0.0),
            asString(1.0, 1.0, 0.0),
      };
      final String[] expectedIndices = new String[]{
            "0 0 0",
            "1 0 0",
            "0 1 0",
            "1 1 0",
      };
      final List<Vector> actualPositions = new ArrayList<>();
      final List<String> actualIndices = new ArrayList<>();
      new StandardVector(2, 2, 2).slideWindow(
            dummyVector(1, 1, 2), // Window size
            null, // Padding
            null, // Stride
            (inputPosition, x, y, z) -> {
               actualPositions.add(inputPosition);
               actualIndices.add(x + " " + y + " " + z);
            });
      Assertions.assertEquals(expectedIndices.length, actualIndices.size());
      Assertions.assertEquals(expectedPositions.length, actualPositions.size());
      for (int i = 0; i < expectedPositions.length; i++) {
         Assertions.assertEquals(expectedPositions[i], actualPositions.get(i).toString(),
               "The input position " + i + " is correct");
         Assertions.assertEquals(expectedIndices[i], actualIndices.get(i),
               "The output index " + i + " is correct");
      }
   }

   @Test
   void testTraverseAround() {
      final String[] expectedPositions = new String[]{
            asString(1, 2, 3, 0.2 * 0.75 * 0.4),
            asString(2, 2, 3, 0.8 * 0.75 * 0.4),
            asString(1, 3, 3, 0.2 * 0.25 * 0.4),
            asString(2, 3, 3, 0.8 * 0.25 * 0.4),

            asString(1, 2, 4, 0.2 * 0.75 * 0.6),
            asString(2, 2, 4, 0.8 * 0.75 * 0.6),
            asString(1, 3, 4, 0.2 * 0.25 * 0.6),
            asString(2, 3, 4, 0.8 * 0.25 * 0.6),
      };
      final List<String> actualPositions = new ArrayList<>();
      new StandardVector(1.8, 2.25, 3.6).traverseAround((x, y, z, coefficient) ->
            actualPositions.add(asString(x, y, z, coefficient)));

      Assertions.assertEquals(expectedPositions.length, actualPositions.size());
      for (int i = 0; i < expectedPositions.length; i++) {
         Assertions.assertEquals(expectedPositions[i], actualPositions.get(i));
      }
   }

   @Test
   void testTraverseAroundWithIntegerPosition() {
      final String[] expectedPositions = new String[]{
            asString(1, 2, 3, 1.0),
      };
      final List<String> actualPositions = new ArrayList<>();
      new StandardVector(1, 2, 3).traverseAround((x, y, z, coefficient) ->
            actualPositions.add(asString(x, y, z, coefficient)));

      Assertions.assertEquals(expectedPositions.length, actualPositions.size());
      for (int i = 0; i < expectedPositions.length; i++) {
         Assertions.assertEquals(expectedPositions[i], actualPositions.get(i));
      }
   }

   private void expectInvalidSizeException(final Runnable runnable) {
      TestUtil.expectException(InvalidSizeException.class, runnable);
   }

   private Vector dummyVector(double x, double y, double z) {
      final Vector result = EasyMock.createMock(Vector.class);
      EasyMock.expect(result.getX()).andReturn(x).anyTimes();
      EasyMock.expect(result.getY()).andReturn(y).anyTimes();
      EasyMock.expect(result.getZ()).andReturn(z).anyTimes();
      EasyMock.expect(result.multipliedBy(EasyMock.anyInt()))
            .andAnswer(() -> dummyVector(x * 2, y * 2, z * 2));
      EasyMock.expect(result.minus(StandardVector.UNIT))
            .andAnswer(() -> dummyVector(x - 1, y - 1, z - 1));
      EasyMock.replay(result);
      return result;
   }

   private String asString(final double x, final double y, final double z) {
      return String.format("(%.2f, %.2f, %.2f)", x, y, z);
   }

   private String asString(final double x, final double y, final double z,
         final double coefficient) {

      return String.format("(%.2f, %.2f, %.2f) with coefficient %.2f",
            x, y, z, coefficient);
   }

   private Vector makeVector() {
      return new StandardVector(X_POS, Y_POS, Z_POS);
   }
}