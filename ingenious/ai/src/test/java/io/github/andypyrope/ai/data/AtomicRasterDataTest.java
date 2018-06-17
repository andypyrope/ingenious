package io.github.andypyrope.ai.data;

import io.github.andypyrope.ai.testutil.TestUtil;
import io.github.andypyrope.ai.util.TriRasterSize;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
   void testCastToAtomic() {
      final RasterData[] rasterData = new RasterData[DATA.length];
      for (int i = 0; i < DATA.length; i++) {
         rasterData[i] = EasyMock.createMock(RasterData.class);
         EasyMock.expect(rasterData[i].getCell(0, 0, 0)).andReturn(DATA[i]);
         EasyMock.replay(rasterData[i]);
      }
      TestUtil.compareDoubleArrays(DATA, AtomicRasterData.castToAtomic(rasterData));
      EasyMock.verify((Object[]) rasterData);
   }

   @Test
   void testGetCell() {
      final RasterData data = new AtomicRasterData(SINGLE_DATA);
      expectMismatchException(() -> data.getCell(1, 0, 0));
      expectMismatchException(() -> data.getCell(0, 1, 0));
      expectMismatchException(() -> data.getCell(0, 0, 1));

      TestUtil.compareDoubles(SINGLE_DATA, data.getCell(0, 0, 0));
   }

   @Test
   void testSetCell() {
      final RasterData data = new AtomicRasterData(SINGLE_DATA);
      expectMismatchException(() -> data.setCell(1, 0, 0, NEW_DATA));
      expectMismatchException(() -> data.setCell(0, 1, 0, NEW_DATA));
      expectMismatchException(() -> data.setCell(0, 0, 1, NEW_DATA));

      TestUtil.compareDoubles(SINGLE_DATA, data.getCell(0, 0, 0));
      data.setCell(0, 0, 0, NEW_DATA);
      TestUtil.compareDoubles(NEW_DATA, data.getCell(0, 0, 0));
   }

   @Test
   void testGetSize() {
      Assertions.assertFalse(new AtomicRasterData(SINGLE_DATA).getSize()
            .differsFrom(new TriRasterSize(1, 1, 1)));
   }

   private void expectMismatchException(final Runnable runnable) {
      TestUtil.expectException(MismatchException.class, runnable);
   }
}