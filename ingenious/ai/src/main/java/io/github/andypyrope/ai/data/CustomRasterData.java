package io.github.andypyrope.ai.data;

import io.github.andypyrope.ai.util.RasterSize;

public class CustomRasterData extends RasterDataBase {

   private final double[][][] _data;

   public CustomRasterData(final RasterSize size) {
      super(size);
      _data = new double[size.getHeight()][size.getWidth()][size.getDepth()];
   }

   @Override
   public double getCell(int x, int y, int z) {
      return _data[y][x][z];
   }

   @Override
   public void setCell(int x, int y, int z, double value) {
      _data[y][x][z] = value;
   }
}
