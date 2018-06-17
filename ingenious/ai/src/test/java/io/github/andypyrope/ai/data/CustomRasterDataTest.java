package io.github.andypyrope.ai.data;

import io.github.andypyrope.ai.testutil.TestUtil;
import io.github.andypyrope.ai.util.RasterSize;
import io.github.andypyrope.ai.util.TriRasterSize;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CustomRasterDataTest {

   @Test
   void testGetCell() {
      final RasterData data = new CustomRasterData(new TriRasterSize(1, 1, 1));
      TestUtil.compareDoubles(0, data.getCell(0, 0, 0));
   }

   @Test
   void testSetCell() {
      final RasterData data = new CustomRasterData(new TriRasterSize(1, 1, 1));
      data.setCell(0, 0, 0, Math.PI);
      TestUtil.compareDoubles(Math.PI, data.getCell(0, 0, 0));
   }

   @Test
   void testGetSize() {
      final RasterSize size = new TriRasterSize(1, 1, 1);
      Assertions.assertSame(size, new CustomRasterData(size).getSize());
   }
}