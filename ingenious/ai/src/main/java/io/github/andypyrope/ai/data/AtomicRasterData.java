package io.github.andypyrope.ai.data;

import io.github.andypyrope.ai.InvalidSizeException;
import io.github.andypyrope.ai.util.TriRasterSize;

/**
 * Raster data that has a width, depth and height of 1.
 */
public class AtomicRasterData extends RasterDataBase {

   private double _data;

   /**
    * @param value The atomic value corresponding to the raster data.
    */
   AtomicRasterData(final double value) {
      super(new TriRasterSize(1, 1, 1));
      _data = value;
   }

   /**
    * Casts atomic data (`double` values) into raster data.
    *
    * @param values The atomic values to cast into raster data.
    * @return An array of resulting raster data.
    */
   public static RasterData[] castToRaster(final double[] values) {
      final RasterData[] result = new RasterData[values.length];
      for (int i = 0; i < values.length; i++) {
         result[i] = new AtomicRasterData(values[i]);
      }
      return result;
   }

   /**
    * Casts raster data ({@link RasterData} values) into atomic data (`double` values). If
    * the raster data has any dimension larger than 1, the extra values are lost. Only the
    * (0,0,0) cell is taken.
    *
    * @param values The raster values to cast into atomic data.
    * @return An array of resulting atomic data.
    */
   public static double[] castToAtomic(final RasterData[] values) {
      final double[] result = new double[values.length];
      for (int i = 0; i < values.length; i++) {
         result[i] = values[i].getCell(0, 0, 0);
      }
      return result;
   }

   @Override
   public double getCell(final int x, final int y, final int z) {
      if (x != 0 || y != 0 || z != 0) {
         throw new InvalidSizeException(
               "Pixel out of bounds: " + x + ", " + y + ", " + z);
      }
      return _data;
   }

   @Override
   public void setCell(final int x, final int y, final int z, final double value) {
      if (x != 0 || y != 0 || z != 0) {
         throw new InvalidSizeException(
               "Pixel out of bounds: " + x + ", " + y + ", " + z);
      }
      _data = value;
   }
}
