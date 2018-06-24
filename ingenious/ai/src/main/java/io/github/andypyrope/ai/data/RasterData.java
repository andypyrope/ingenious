package io.github.andypyrope.ai.data;

import io.github.andypyrope.ai.InvalidSizeException;
import io.github.andypyrope.ai.util.CoordinateConsumer;
import io.github.andypyrope.ai.util.Vector;

import java.util.Random;

/**
 * Unlike atomic data, which has 0 dimensions, this one has up to 3 dimensions. Useful for
 * representing continuous raster data, e.g. an image.
 */
public interface RasterData {

   /**
    * @return The width, height and depth of this data.
    */
   Vector getSize();

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
    * Gets the value at a specific position. The surrounding 2^dimensions cells are taken
    * with the ratios that correspond to their
    *
    * @param position A vector that specifies the coordinates of the cell. If the
    *                 coordinates are not whole numbers, parts of cells are taken with
    *                 their respective ratios. All cells with coordinates outside of the
    *                 domain of this data are assumed to have data 0.
    * @return the value at the specified position.
    */
   double getCell(Vector position);

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
    * @param value The value to add to the (x, y, z) cell.
    */
   void addTo(int x, int y, int z, double value);

   /**
    * Adds to the value of a specific cell.
    *
    * @param position The position where to add the value. If it is not an integer
    *                 position, the surrounding cells are updated with ratios depending on
    *                 how close they are to this position.
    * @param value    The value to add to the cell(s) at the specified position.
    */
   void addTo(Vector position, double value);

   /**
    * Multiplies the value of a specific cell by a number(scalar).
    *
    * @param x          The horizontal position of the cell. 0 <= x < width.
    * @param y          The vertical position of the cell. 0 <= y < height.
    * @param z          The depth of the cell. 0 <= k < depth.
    * @param multiplier The value to multiply the (x, y, k) cell by.
    */
   void multiply(int x, int y, int z, double multiplier);

   /**
    * Makes sure the dimensions of the raster data are correct.
    *
    * @param size The expected size
    * @throws InvalidSizeException If any of the dimensions are incorrect.
    */
   void verifyDimensions(Vector size) throws InvalidSizeException;

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
   void forEach(CoordinateConsumer consumer);

   /**
    * @return The sum of all cells of the data.
    */
   double getSum();
}
