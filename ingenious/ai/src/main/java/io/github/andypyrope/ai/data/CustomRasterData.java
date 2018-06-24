package io.github.andypyrope.ai.data;

import io.github.andypyrope.ai.util.Vector;

public class CustomRasterData extends RasterDataBase {

   private final double[][][] _data;

   public CustomRasterData(final Vector size) {
      super(size);
      _data = new double[_height][_width][_depth];
   }

   @Override
   public double getCell(final int x, final int y, final int z) {
      if (x < 0 || y < 0 || z < 0 || x >= _width || y >= _height || z >= _depth) {
         return 0;
      }
      return _data[y][x][z];
   }

   @Override
   public void setCell(final int x, final int y, final int z, final double value) {
      _data[y][x][z] = value;
   }
}
