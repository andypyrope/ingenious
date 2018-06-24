package io.github.andypyrope.ai.util;

/**
 * A consumer used to easily traverse 3-dimensional (raster) data in the case where a
 * certain coefficient is applied to each cell.
 */
public interface CoordinateWithCoefficientConsumer {

   /**
    * @param x           The current X(horizontal) position.
    * @param y           The current Y(vertical) position.
    * @param z           The current Z(depth) position.
    * @param coefficient The coefficient at the current position.
    */
   void accept(int x, int y, int z, double coefficient);
}
