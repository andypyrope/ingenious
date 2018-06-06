package io.github.andypyrope.ai.data;

import io.github.andypyrope.ai.testutil.TestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

class AtomicRasterDataTest {

   private static final double SINGLE_DATA = 42.0;
   private static final double NEW_DATA = -0.24;
   private static final double[] DATA = new double[]{0.2, 124, -34};

   @Test
   void testCastToRaster() {
      final RasterData[] data = AtomicRasterData.castToRaster(DATA);
      TestUtil.compareDoubles(DATA[0], data[0].getCell(0, 0, 0));
      TestUtil.compareDoubles(DATA[1], data[1].getCell(0, 0, 0));
      TestUtil.compareDoubles(DATA[2], data[2].getCell(0, 0, 0));
   }

   @Test
   void testGetCell() {
      final RasterData data = new AtomicRasterData(SINGLE_DATA);
      TestUtil.compareDoubles(SINGLE_DATA, data.getCell(0, 0, 0));
   }

   @Test
   void testSetCell() {
      final RasterData data = new AtomicRasterData(SINGLE_DATA);
      TestUtil.compareDoubles(SINGLE_DATA, data.getCell(0, 0, 0));
      data.setCell(0, 0, 0, NEW_DATA);
      TestUtil.compareDoubles(NEW_DATA, data.getCell(0, 0, 0));
   }

   @Test
   void testGetWidth() {
      Assertions.assertEquals(1, new AtomicRasterData(SINGLE_DATA).getWidth());
   }

   @Test
   void testGetHeight() {
      Assertions.assertEquals(1, new AtomicRasterData(SINGLE_DATA).getHeight());
   }

   @Test
   void testGetDepth() {
      Assertions.assertEquals(1, new AtomicRasterData(SINGLE_DATA).getDepth());
   }

   @Test
   void testGetId() {
      final String id = new AtomicRasterData(SINGLE_DATA).getId();
      final Pattern pattern = Pattern.compile("AtomicRasterData_\\d+");

      Assertions.assertTrue(pattern.matcher(id).matches(), String.format(
            "The ID '%s' matches the pattern '%s'", id, pattern.toString()));
   }
}