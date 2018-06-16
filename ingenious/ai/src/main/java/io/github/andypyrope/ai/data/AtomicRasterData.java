package io.github.andypyrope.ai.data;

/**
 * Raster data that has a width, depth and height of 1.
 */
public class AtomicRasterData extends RasterDataBase {

   private static final int WIDTH = 1;
   private static final int HEIGHT = 1;
   private static final int DEPTH = 1;
   private final double[][][] _data;
   /**
    * @param value The atomic value corresponding to the raster data.
    */
   AtomicRasterData(final double value) {
      _data = new double[WIDTH][HEIGHT][DEPTH];
      _data[0][0][0] = value;
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
      return _data[x][y][z];
   }

   @Override
   public void setCell(final int x, final int y, final int z, final double value) {
      _data[x][y][z] = value;
   }

   @Override
   public int getWidth() {
      return WIDTH;
   }

   @Override
   public int getHeight() {
      return HEIGHT;
   }

   @Override
   public int getDepth() {
      return DEPTH;
   }
}
