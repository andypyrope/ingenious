package io.github.andypyrope.ai.data;

import io.github.andypyrope.ai.testutil.DeterministicRandom;
import io.github.andypyrope.ai.testutil.TestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

class RasterDataBaseTest {

   private static final int WIDTH = 2;
   private static final int HEIGHT = 1;
   private static final int DEPTH = 3;

   private static final double[][][] INITIAL_DATA = new double[][][]{
         new double[][]{new double[]{Math.PI, 2}},
         new double[][]{new double[]{Math.E, 4}},
         new double[][]{new double[]{5, 6}},
   };

   @Test
   void testSetAll() {
      final RasterData data = makeData();
      data.setAll(420);
      for (int x = 0; x < WIDTH; x++) {
         for (int y = 0; y < HEIGHT; y++) {
            for (int z = 0; z < DEPTH; z++) {
               TestUtil.compareDoubles(420, ((TestRasterData) data)._data[x][y][z]);
            }
         }
      }
   }

   @Test
   void testAddTo() {
      final RasterData data = makeData();
      TestUtil.compareDoubles(Math.E, ((TestRasterData) data)._data[0][0][1]);
      data.addTo(0, 0, 1, Math.PI);
      TestUtil.compareDoubles(Math.E + Math.PI, ((TestRasterData) data)._data[0][0][1]);
   }

   @Test
   void testMultiply() {
      final RasterData data = makeData();
      TestUtil.compareDoubles(4, ((TestRasterData) data)._data[1][0][1]);
      data.multiply(1, 0, 1, Math.PI);
      TestUtil.compareDoubles(4 * Math.PI, ((TestRasterData) data)._data[1][0][1]);
   }

   @Test
   void testVerifyDimensions() {
      expectMismatchException(
            () -> makeData().verifyDimensions(WIDTH + 1, HEIGHT, DEPTH));
      expectMismatchException(
            () -> makeData().verifyDimensions(WIDTH, HEIGHT + 1, DEPTH));
      expectMismatchException(
            () -> makeData().verifyDimensions(WIDTH, HEIGHT, DEPTH + 1));
      makeData().verifyDimensions(WIDTH, HEIGHT, DEPTH);
   }

   @Test
   void testForEach() {
      final RasterData data = new TestRasterData(2, 2, 2);
      // (z, y, x) follows the pattern of increasing binary numbers in this case
      final int[][] coordinates = new int[][]{
            new int[]{0, 0, 0},
            new int[]{1, 0, 0},
            new int[]{0, 1, 0},
            new int[]{1, 1, 0},
            new int[]{0, 0, 1},
            new int[]{1, 0, 1},
            new int[]{0, 1, 1},
            new int[]{1, 1, 1},
      };
      final AtomicInteger loopIndex = new AtomicInteger(0);
      data.forEach((x, y, z) -> {
         Assertions.assertEquals(coordinates[loopIndex.get()][0], x);
         Assertions.assertEquals(coordinates[loopIndex.get()][1], y);
         Assertions.assertEquals(coordinates[loopIndex.get()][2], z);
         loopIndex.incrementAndGet();
      });
   }

   @Test
   void testRandomize() {
      final TestRasterData data = new TestRasterData(2, 2, 2);
      data.randomize(new DeterministicRandom());
      TestUtil.compareDoubleArrays(new double[]{0.20, 0.81}, data._data[0][0]);
      TestUtil.compareDoubleArrays(new double[]{0.46, 0.33}, data._data[1][0]);
      TestUtil.compareDoubleArrays(new double[]{0.0086, 0.92}, data._data[0][1]);
      TestUtil.compareDoubleArrays(new double[]{0.67, 0.73}, data._data[1][1]);
   }

   @Test
   void testClear() {
      final TestRasterData data = new TestRasterData();
      TestUtil.compareDoubles(data._data[0][0][0], INITIAL_DATA[0][0][0]);
      data.clear();
      data.forEach((x, y, z) -> TestUtil.compareDoubles(0.0, data.getCell(x, y, z)));
   }

   private void expectMismatchException(final Runnable runnable) {
      TestUtil.expectException(MismatchException.class, runnable);
   }

   private RasterData makeData() {
      return new TestRasterData();
   }

   private class TestRasterData extends RasterDataBase {
      private final double[][][] _data;
      private final int _width;
      private final int _height;
      private final int _depth;

      TestRasterData(final int width, final int height, final int depth) {
         _width = width;
         _height = height;
         _depth = depth;
         _data = new double[_width][_height][_depth];
      }

      TestRasterData() {
         _width = WIDTH;
         _height = HEIGHT;
         _depth = DEPTH;
         _data = new double[WIDTH][HEIGHT][DEPTH];
         for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
               for (int z = 0; z < DEPTH; z++) {
                  _data[x][y][z] = INITIAL_DATA[z][y][x];
               }
            }
         }
      }

      @Override
      public double getCell(final int x, final int y, final int z) {
         return _data[x][y][z];
      }

      @Override
      public void setCell(final int x, final int y, final int z, final double value) {
         _data[x][y][z] = value;
      }

      @Override
      public int getWidth() {
         return _width;
      }

      @Override
      public int getHeight() {
         return _height;
      }

      @Override
      public int getDepth() {
         return _depth;
      }
   }
}