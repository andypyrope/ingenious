package io.github.andypyrope.ai.util;

/**
 * A consumer that accepts both a vector position and 3 integer coordinates of another
 * position.
 */
public interface VectorCoordinateConsumer {

   /**
    * @param position The first position, which may not be an integer.
    * @param x        The X coordinate of the second position.
    * @param y        The Y coordinate of the second position.
    * @param z        The Z coordinate of the second position.
    */
   void accept(Vector position, int x, int y, int z);
}
