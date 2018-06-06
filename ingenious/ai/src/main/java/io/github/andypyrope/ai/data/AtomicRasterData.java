package io.github.andypyrope.ai.data;

/**
 * Raster data that has a width, depth and height of 1.
 */
public class AtomicRasterData implements RasterData {

   private static final int WIDTH = 1;
   private static final int HEIGHT = 1;
   private static final int DEPTH = 1;
   private static int _lastId = 0;
   private final double[][][] _data;

   private final String _id;

   /**
    * @param value The atomic value corresponding to the raster data.
    */
   AtomicRasterData(final double value) {
      _data = new double[WIDTH][HEIGHT][DEPTH];
      _data[0][0][0] = value;
      _id = getClass().getSimpleName() + "_" + _lastId++;
   }

   /**
    * Casts atomic data (`double` values) into raster data.
    *
    * @param values The atomic values to cast into raster data.
    * @return An array of resulting raster data.
    */
   public static RasterData[] castToRaster(double[] values) {
      final RasterData[] result = new RasterData[values.length];
      for (int i = 0; i < values.length; i++) {
         result[i] = new AtomicRasterData(values[i]);
      }
      return result;
   }

   @Override
   public double getCell(final int x, final int y, final int k) {
      return _data[x][y][k];
   }

   @Override
   public void setCell(final int x, final int y, final int k, final double value) {
      _data[x][y][k] = value;
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

   @Override
   public String getId() {
      return _id;
   }
}
