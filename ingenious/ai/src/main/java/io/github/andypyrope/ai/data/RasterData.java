package io.github.andypyrope.ai.data;

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
    * @param k The depth of the cell. 0 <= k < depth.
    * @return the value at the (x, y, depth) cell.
    */
   double getCell(int x, int y, int k);

   /**
    * Sets the value at a specific cell.
    *
    * @param x     The horizontal position of the cell. 0 <= x < width.
    * @param y     The vertical position of the cell. 0 <= y < height.
    * @param k     The depth of the cell. 0 <= k < depth.
    * @param value The new value at the (x, y, depth) cell.
    */
   void setCell(int x, int y, int k, double value);

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
    * @return A string that identifies the data, for example the name of the file it has
    *       been parsed from. It may not be unique.
    */
   String getId();
}
