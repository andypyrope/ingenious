package io.github.andypyrope.ai.util;

/**
 * A consumer used to easily traverse 3-dimensional (raster) data.
 */
public interface TriCoordinateConsumer {

   /**
    * @param x The current X position.
    * @param y The current Y position.
    * @param z The current depth position.
    */
   void accept(int x, int y, int z);
}
