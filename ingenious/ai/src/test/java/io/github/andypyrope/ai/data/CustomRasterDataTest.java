package io.github.andypyrope.ai.data;

import io.github.andypyrope.ai.testutil.TestUtil;
import io.github.andypyrope.ai.util.StandardVector;
import io.github.andypyrope.ai.util.Vector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CustomRasterDataTest {

   @Test
   void testGetCell() {
      final RasterData data = new CustomRasterData(new StandardVector(1, 1, 1));
      TestUtil.compareDoubles(0, data.getCell(0, 0, 0));
      data.setAll(1.0);
      TestUtil.compareDoubles(1, data.getCell(0, 0, 0));

      TestUtil.compareDoubles(0, data.getCell(-1, 0, 0));
      TestUtil.compareDoubles(0, data.getCell(0, -1, 0));
      TestUtil.compareDoubles(0, data.getCell(0, 0, -1));

      TestUtil.compareDoubles(0, data.getCell(1, 0, 0));
      TestUtil.compareDoubles(0, data.getCell(0, 1, 0));
      TestUtil.compareDoubles(0, data.getCell(0, 0, 1));
   }

   @Test
   void testSetCell() {
      final RasterData data = new CustomRasterData(new StandardVector(1, 1, 1));
      data.setCell(0, 0, 0, Math.PI);
      TestUtil.compareDoubles(Math.PI, data.getCell(0, 0, 0));
   }

   @Test
   void testGetSize() {
      final Vector size = new StandardVector(1, 1, 1);
      Assertions.assertSame(size, new CustomRasterData(size).getSize());
   }
}