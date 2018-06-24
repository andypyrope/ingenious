package io.github.andypyrope.ai.util;

import io.github.andypyrope.ai.InvalidSizeException;

public interface Vector {

   /**
    * @param size The size to add across all dimensions.
    * @return The sum of this size and the number.
    */
   Vector plus(double size);

   /**
    * @param x The value to add to the X coordinate.
    * @param y The value to add to the Y coordinate.
    * @param z The value to add to the Z coordinate.
    * @return The resulting vector.
    */
   Vector plus(int x, int y, int z);

   /**
    * @param other Another raster size.
    * @return The sum of the two sizes.
    */
   Vector plus(Vector other);

   /**
    * @param x The value to subtract from the X coordinate.
    * @param y The value to subtract from the Y coordinate.
    * @param z The value to subtract from the Z coordinate.
    * @return The resulting vector.
    */
   Vector minus(int x, int y, int z);

   /**
    * @param other Another raster size.
    * @return The difference between the two sizes.
    */
   Vector minus(Vector other);

   /**
    * @param multiplier What to multiply the positions/sizes across all dimensions by.
    * @return The resulting multiplied position/size.
    */
   Vector multipliedBy(int multiplier);

   /**
    * @param other Another raster size.
    * @return The resulting size with its dimensions divided by each respective size in
    *       the other raster size. If the result is non-integer, it is floored (i.e.
    *       integer division is performed).
    */
   Vector dividedBy(Vector other);

   /**
    * @return The X position (width).
    */
   double getX();

   /**
    * @return The Y position (height).
    */
   double getY();

   /**
    * @return The Z position (depth).
    */
   double getZ();

   /**
    * @return The total number of pixels if this vector is raster.
    */
   int getPixelCount();

   /**
    * @param other Another raster size.
    * @return Whether any of the dimensions are different.
    */
   boolean differsFrom(Vector other);

   /**
    * @throws InvalidSizeException If this is not valid as a raster size (i.e. doesn't
    *                              have positive integer coordinates).
    */
   void validateAsSize() throws InvalidSizeException;

   /**
    * @return Whether this is a valid index. A valid index has non-negative integer
    *       coordinates.
    */
   boolean isValidIndex();

   /**
    * Assuming this vector represents a raster size, calculates the resulting size of a
    * scan over this size, i.e. the number of steps in each dimension that a window would
    * make over data of this size if the data has a certain padding and the window has a
    * certain size and moves with a certain stride(step size).
    *
    * @param windowSize The size of the window.
    * @param padding    The padding of the data. `null` for none (same as a zero vector).
    * @param stride     The stride of the window. `null` for none (same as a unit
    *                   vector).
    * @return The resulting size after a scan of this kind.
    */
   Vector getScanSize(Vector windowSize, Vector padding,
         Vector stride);

   /**
    * Iterate through this as a size; as if it's a multidimensional matrix.
    *
    * @param consumer The consumer to use.
    * @throws InvalidSizeException If this vector is not a valid size.
    */
   void forEach(CoordinateConsumer consumer) throws InvalidSizeException;

   /**
    * Iterate through this as size.
    *
    * @param windowSize The window size.
    * @param padding    The padding of this size.
    * @param stride     The stride the window moves with.
    * @param consumer   The consumer that accepts 1) The position in this size and 2) The
    *                   "index" of this step in the form of as many integers as there are
    *                   dimensions. For example when calling this while performing a
    *                   convolution, the first position would be the one in the input data
    *                   and the second position would be the one in the output data.
    */
   void slideWindow(Vector windowSize, Vector padding,
         Vector stride, VectorCoordinateConsumer consumer);


   /**
    * Traverses the integer coordinates around this position. For every position with
    * absolute distance from this position (deltaX, deltaY, ...), the corresponding
    * coefficient is `max(1 - deltaX, 0) * max(1 - deltaY, 0) * ...`. The ones with a
    * coefficient of 0.0 (i.e. the ones that are at least 1 unit away from the position in
    * any of the dimensions) are ignored.
    * <p>
    * The way the coefficient is calculated ensures that the sum of all coefficients is
    * exactly 1.0.
    *
    * @param consumer The consumer which accepts the positions of the surrounding
    *                 positions as well as their corresponding coefficient.
    */
   void traverseAround(final CoordinateWithCoefficientConsumer consumer);
}
