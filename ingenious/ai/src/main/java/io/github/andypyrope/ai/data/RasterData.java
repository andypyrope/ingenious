package io.github.andypyrope.ai.data;

import io.github.andypyrope.ai.util.TriCoordinateConsumer;

import java.util.Random;

/**
 * Unlike atomic data, which has 0 dimensions, this one has up to 3 dimensions. Useful for
 * representing continuous raster data, e.g. an image.
 */
public interface RasterData {

   /**
    * Gets the value at a specific cell.
    *
    * @param x The horizontal position of the cell. 0 <= x < width.
    * @param y The vertical position of the cell. 0 <= y < height.
    * @param z The depth of the cell. 0 <= k < depth.
    * @return the value at the (x, y, k) cell.
    */
   double getCell(int x, int y, int z);

   /**
    * Sets the value of a specific cell.
    *
    * @param x     The horizontal position of the cell. 0 <= x < width.
    * @param y     The vertical position of the cell. 0 <= y < height.
    * @param z     The depth of the cell. 0 <= k < depth.
    * @param value The new value at the (x, y, k) cell.
    */
   void setCell(int x, int y, int z, double value);

   /**
    * Sets the value of all cells to the same number.
    *
    * @param value The new value at all cells.
    */
   void setAll(double value);

   /**
    * Adds to the value of a specific cell.
    *
    * @param x     The horizontal position of the cell. 0 <= x < width.
    * @param y     The vertical position of the cell. 0 <= y < height.
    * @param z     The depth of the cell. 0 <= k < depth.
    * @param delta The value to add to the (x, y, k) cell.
    */
   void addTo(int x, int y, int z, double delta);

   /**
    * Multiplies the value of a specific cell by a number.
    *
    * @param x          The horizontal position of the cell. 0 <= x < width.
    * @param y          The vertical position of the cell. 0 <= y < height.
    * @param z          The depth of the cell. 0 <= k < depth.
    * @param multiplier The value to multiply the (x, y, k) cell by.
    */
   void multiply(int x, int y, int z, double multiplier);

   /**
    * @return The width of the data (2nd dimension in the raw array).
    */
   int getWidth();

   /**
    * @return The height of the data (1st dimension in the raw array).
    */
   int getHeight();

   /**
    * @return The depth of the data (3rd dimension in the raw array).
    */
   int getDepth();

   /**
    * Makes sure the dimensions of the raster data are correct.
    *
    * @param width  The expected width.
    * @param height The expected height.
    * @param depth  The expected depth.
    * @throws MismatchException If any of the dimensions are incorrect.
    */
   void verifyDimensions(int width, int height, int depth) throws MismatchException;

   /**
    * Sets the data to random values from 0 (inclusive) to 1 (exclusive).
    *
    * @param random The random number generator to use.
    */
   void randomize(Random random);

   /**
    * Sets the data to 0.
    */
   void clear();

   /**
    * Traverses this data.
    *
    * @param consumer The consumer to use.
    */
   void forEach(TriCoordinateConsumer consumer);
}
