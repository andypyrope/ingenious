package io.github.andypyrope.ai.util;

public interface RasterSize {

   /**
    * @param value The resulting minimum value across all dimensions.
    * @return A new raster size where each dimension is equal to at least a certain size.
    */
   RasterSize atLeast(int value);

   /**
    * @param size Another raster size.
    * @return The sum of the two sizes.
    */
   RasterSize plus(int size);

   /**
    * @param other Another raster size.
    * @return The difference between the two sizes.
    */
   RasterSize minus(RasterSize other);

   /**
    * @return The width of this size.
    */
   int getWidth();

   /**
    * @return The height of this size.
    */
   int getHeight();

   /**
    * @return The depth of this size.
    */
   int getDepth();

   /**
    * @return The total number of pixels.
    */
   int getPixelCount();

   /**
    * @param other Another raster size.
    * @return Whether any of the dimensions are different.
    */
   boolean differsFrom(RasterSize other);

   /**
    * @return Whether the size is invalid. An invalid size is one with any dimension that
    *       is equal to 0 or less.
    */
   boolean isInvalid();
}
