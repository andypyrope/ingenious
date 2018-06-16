package io.github.andypyrope.ai.data;

public class CustomRasterData extends RasterDataBase {

   private final int _width;
   private final int _height;
   private final int _depth;

   private final double[][][] _data;

   public CustomRasterData(final int width, final int height, final int depth) {
      _width = width;
      _height = height;
      _depth = depth;
      _data = new double[height][width][depth];
   }

   @Override
   public double getCell(int x, int y, int z) {
      return _data[y][x][z];
   }

   @Override
   public void setCell(int x, int y, int z, double value) {
      _data[y][x][z] = value;
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
