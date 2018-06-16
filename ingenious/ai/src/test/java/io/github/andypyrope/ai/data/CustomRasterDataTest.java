package io.github.andypyrope.ai.data;

import io.github.andypyrope.ai.testutil.TestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CustomRasterDataTest {

   @Test
   void testGetCell() {
      final RasterData data = new CustomRasterData(1, 1, 1);
      TestUtil.compareDoubles(0, data.getCell(0, 0, 0));
   }

   @Test
   void testSetCell() {
      final RasterData data = new CustomRasterData(1, 1, 1);
      data.setCell(0, 0, 0, Math.PI);
      TestUtil.compareDoubles(Math.PI, data.getCell(0, 0, 0));
   }

   @Test
   void testGetWidth() {
      Assertions.assertEquals(2, new CustomRasterData(2, 1, 1).getWidth());
   }

   @Test
   void testGetHeight() {
      Assertions.assertEquals(3, new CustomRasterData(1, 3, 1).getHeight());
   }

   @Test
   void testGetDepth() {
      Assertions.assertEquals(4, new CustomRasterData(1, 1, 4).getDepth());
   }
}