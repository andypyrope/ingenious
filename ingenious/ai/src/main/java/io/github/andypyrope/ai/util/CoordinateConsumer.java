package io.github.andypyrope.ai.util;

/**
 * A consumer used to easily traverse 3-dimensional (raster) data.
 */
public interface CoordinateConsumer {

   /**
    * @param x The current X(horizontal) position.
    * @param y The current Y(vertical) position.
    * @param z The current Z(depth) position.
    */
   void accept(int x, int y, int z);
}
