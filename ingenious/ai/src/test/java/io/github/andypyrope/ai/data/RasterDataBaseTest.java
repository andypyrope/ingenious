package io.github.andypyrope.ai.data;

import io.github.andypyrope.ai.InvalidSizeException;
import io.github.andypyrope.ai.testutil.DeterministicRandom;
import io.github.andypyrope.ai.testutil.TestUtil;
import io.github.andypyrope.ai.util.CoordinateConsumer;
import io.github.andypyrope.ai.util.StandardVector;
import io.github.andypyrope.ai.util.Vector;
import org.easymock.EasyMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

class RasterDataBaseTest {

   private static final int WIDTH = 2;
   private static final int HEIGHT = 1;
   private static final int DEPTH = 3;
   private static final Vector SIZE = new StandardVector(WIDTH, HEIGHT, DEPTH);

   private static final double[][][] INITIAL_DATA = new double[][][]{
         new double[][]{new double[]{Math.PI, 2}},
         new double[][]{new double[]{Math.E, 4}},
         new double[][]{new double[]{5, 6}},
   };

   private static final List<Object> OBJECTS_TO_VERIFY = new ArrayList<>();

   @BeforeEach
   void setUp() {
      OBJECTS_TO_VERIFY.clear();
   }

   @AfterEach
   void tearDown() {
      for (final Object object : OBJECTS_TO_VERIFY) {
         EasyMock.verify(object);
      }
   }

   @Test
   void testConstructor() {
      new TestRasterData(makeSizeMock());
   }

   @Test
   void testGetSize() {
      final Vector size = makeSizeMock(1, 1, 1);
      EasyMock.replay(size);
      Assertions.assertSame(size, new TestRasterData(size).getSize());
   }

   @Test
   void testGetCellWithIntegerVector() {
      final RasterData data = new TestRasterData(makeSizeMock());
      TestUtil.compareDoubles(data.getCell(0, 0, 1),
            data.getCell(new StandardVector(0, 0, 1)));
   }

   @Test
   void testGetCellWithNonIntegerVector() {
      final TestRasterData data = new TestRasterData(makeSizeMock());
      TestUtil.compareDoubles(0 + // Only used to fix the indentation
                  0.00 * 0.00 * Math.PI +
                  0.00 * 0.00 * 2 +
                  0.80 * 0.25 * Math.E +
                  0.20 * 0.25 * 4 +
                  0.80 * 0.75 * 5 +
                  0.20 * 0.75 * 6,
            data.getCell(new StandardVector(0.2, 0, 1.75)));
   }

   /**
    * Tries many positions to make sure that the sum of the sum of the coefficients is
    * exactly 1.0.
    */
   @Test
   void testGetCellWithNonIntegerVectorInSymmetric() {
      final TestRasterData data = new TestRasterData(new StandardVector(2, 2, 2));
      final Random random = ThreadLocalRandom.current();
      final double value = 42.0;
      data.setAll(value);
      for (int i = 0; i < 100; i++) {
         TestUtil.compareDoubles(value, data.getCell(new StandardVector(
               random.nextDouble(), random.nextDouble(), random.nextDouble())));
      }
      TestUtil.compareDoubles(value, data.getCell(new StandardVector(0, 0, 0)));
      TestUtil.compareDoubles(value, data.getCell(new StandardVector(0, 0, 1)));
      TestUtil.compareDoubles(value, data.getCell(new StandardVector(0, 1, 0)));
      TestUtil.compareDoubles(value, data.getCell(new StandardVector(0, 1, 1)));
      TestUtil.compareDoubles(value, data.getCell(new StandardVector(1, 0, 0)));
      TestUtil.compareDoubles(value, data.getCell(new StandardVector(1, 0, 1)));
      TestUtil.compareDoubles(value, data.getCell(new StandardVector(1, 1, 0)));
      TestUtil.compareDoubles(value, data.getCell(new StandardVector(1, 1, 1)));
   }

   @Test
   void testSetAll() {
      final RasterData data = new TestRasterData(SIZE);
      data.setAll(420);
      for (int x = 0; x < WIDTH; x++) {
         for (int y = 0; y < HEIGHT; y++) {
            for (int z = 0; z < DEPTH; z++) {
               TestUtil.compareDoubles(420, data.getCell(x, y, z));
            }
         }
      }
   }

   @Test
   void testAddTo() {
      final RasterData data = new TestRasterData(makeSizeMock());
      TestUtil.compareDoubles(Math.E, data.getCell(0, 0, 1));
      data.addTo(0, 0, 1, Math.PI);
      TestUtil.compareDoubles(Math.E + Math.PI, data.getCell(0, 0, 1));
   }

   @Test
   void testAddToWithIntegerVector() {
      final RasterData data = new TestRasterData(makeSizeMock());
      TestUtil.compareDoubles(Math.E, data.getCell(0, 0, 1));
      data.addTo(new StandardVector(0, 0, 1), Math.PI);
      TestUtil.compareDoubles(Math.E + Math.PI, data.getCell(0, 0, 1));
   }

   @Test
   void testAddToWithNonIntegerVector() {
      final TestRasterData data = new TestRasterData(makeSizeMock());

      TestUtil.compareDoubles(Math.PI, data.getCell(0, 0, 0));
      TestUtil.compareDoubles(2, data.getCell(1, 0, 0));
      TestUtil.compareDoubles(Math.E, data.getCell(0, 0, 1));
      TestUtil.compareDoubles(4, data.getCell(1, 0, 1));
      TestUtil.compareDoubles(5, data.getCell(0, 0, 2));
      TestUtil.compareDoubles(6, data.getCell(1, 0, 2));
      final double value = 1.0;
      data.addTo(new StandardVector(0.2, 0, 1.75), value);

      TestUtil.compareDoubles(Math.PI +
            0.00 * 0.00 * value, data.getCell(0, 0, 0));
      TestUtil.compareDoubles(2 +
            0.00 * 0.00 * value, data.getCell(1, 0, 0));
      TestUtil.compareDoubles(Math.E +
            0.80 * 0.25 * value, data.getCell(0, 0, 1));
      TestUtil.compareDoubles(4 +
            0.20 * 0.25 * value, data.getCell(1, 0, 1));
      TestUtil.compareDoubles(5 +
            0.80 * 0.75 * value, data.getCell(0, 0, 2));
      TestUtil.compareDoubles(6 +
            0.20 * 0.75 * value, data.getCell(1, 0, 2));
   }

   @Test
   void testMultiply() {
      final TestRasterData data = new TestRasterData(makeSizeMock());
      TestUtil.compareDoubles(4, data.getCell(1, 0, 1));
      data.multiply(1, 0, 1, Math.PI);
      TestUtil.compareDoubles(4 * Math.PI, data.getCell(1, 0, 1));
   }

   @Test
   void testVerifyDimensions() {
      final Vector targetSize = EasyMock.createMock(Vector.class);
      final Vector invalidSize = makeSizeMock(1, 1, 1);
      EasyMock.expect(invalidSize.differsFrom(targetSize)).andReturn(true);
      final Vector validSize = makeSizeMock(1, 1, 1);
      EasyMock.expect(validSize.differsFrom(targetSize)).andReturn(false);
      EasyMock.replay(targetSize, invalidSize, validSize);

      expectInvalidSizeException(
            () -> new TestRasterData(invalidSize).verifyDimensions(targetSize));
      new TestRasterData(validSize).verifyDimensions(targetSize);
   }

   @Test
   void testForEach() {
      final CoordinateConsumer consumer = EasyMock.createMock(CoordinateConsumer.class);
      final Vector size = makeSizeMock(1, 1, 1);
      size.forEach(consumer);
      EasyMock.expectLastCall();
      EasyMock.replay(size, consumer);

      new TestRasterData(size).forEach(consumer);
   }

   @Test
   void testRandomize() {
      final TestRasterData data = new TestRasterData(new StandardVector(2, 2, 2));
      data.randomize(new DeterministicRandom());
      TestUtil.compareDoubleArraysLoose(new double[]{0.20, 0.81}, data._data[0][0]);
      TestUtil.compareDoubleArraysLoose(new double[]{0.465, 0.332}, data._data[1][0]);
      TestUtil.compareDoubleArraysLoose(new double[]{0.0086, 0.92}, data._data[0][1]);
      TestUtil.compareDoubleArraysLoose(new double[]{0.67, 0.73}, data._data[1][1]);
   }

   @Test
   void testClear() {
      final TestRasterData data = new TestRasterData(SIZE);
      TestUtil.compareDoubles(data._data[0][0][0], INITIAL_DATA[0][0][0]);
      data.clear();
      for (int x = 0; x < WIDTH; x++) {
         for (int y = 0; y < HEIGHT; y++) {
            for (int z = 0; z < DEPTH; z++) {
               TestUtil.compareDoubles(0.0, data.getCell(x, y, z));
            }
         }
      }
   }

   @Test
   void testGetSum() {
      TestUtil.compareDoubles(Math.PI + 2 + Math.E + 4 + 5 + 6,
            new TestRasterData(makeSizeMock()).getSum());
   }

   private void expectInvalidSizeException(final Runnable runnable) {
      TestUtil.expectException(InvalidSizeException.class, runnable);
   }

   private Vector makeSizeMock() {
      final Vector size = makeSizeMock(WIDTH, HEIGHT, DEPTH);
      EasyMock.replay(size);
      return size;
   }

   private Vector makeSizeMock(final int width, final int height, final int depth) {
      final Vector size = EasyMock.createMock(Vector.class);
      size.validateAsSize();
      EasyMock.expectLastCall();
      EasyMock.expect(size.getX()).andReturn((double) width);
      EasyMock.expect(size.getY()).andReturn((double) height);
      EasyMock.expect(size.getZ()).andReturn((double) depth);
      OBJECTS_TO_VERIFY.add(size);
      return size;
   }

   private class TestRasterData extends RasterDataBase {
      private final double[][][] _data;

      TestRasterData(final Vector size) {
         super(size);
         _data = new double[_width][_height][_depth];
         if (_width == WIDTH && _height == HEIGHT && _depth == DEPTH) {
            for (int x = 0; x < _width; x++) {
               for (int y = 0; y < _height; y++) {
                  for (int z = 0; z < _depth; z++) {
                     _data[x][y][z] = INITIAL_DATA[z][y][x];
                  }
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
   }
}