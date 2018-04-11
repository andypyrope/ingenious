package io.github.andypyrope.platform.dna;

/**
 * Represents a single strand of DNA.
 */
public interface Dna {
   /**
    * Produces mutations in the DNA
    *
    * @param mutationProbability The probability of a single element in the DNA being
    *                            mutated
    */
   void mutate(double mutationProbability);

   /**
    * Makes the DNA completely (pseudo-)random
    */
   void randomize();

   /**
    * Returns the reading pointer to the first position
    */
   void resetReader();

   /**
    * Reads a single <b>unsigned</b> integer. Its minimum and maximum values aren't
    * necessarily {@link Integer#MIN_VALUE and Integer#MAX_VALUE}! This may or may not
    * return the same as {@link #readSigned()}.
    *
    * @return An unsigned integer value
    */
   int read();

   /**
    * @return An integer in the [from, to) range.
    */
   int read(int from, int to);

   /**
    * Reads a single integer. Its minimum and maximum values aren't necessarily {@link
    * Integer#MIN_VALUE and Integer#MAX_VALUE}!
    *
    * @return An integer value
    */
   int readSigned();

   /**
    * Reads a single double with a value from 0.0 to 1.0
    *
    * @return A double value from 0.0 to 1.0
    */
   double readDouble();

   /**
    * Reads an array with a specified length
    *
    * @param length The desired length of the array
    * @return An integer array
    */
   int[] readArray(int length);

   /**
    * Reads a matrix with specified height and width
    *
    * @param height The desired height (first dimension) of the matrix
    * @param width  The desired width (second dimension) of the matrix
    * @return A matrix
    */
   int[][] readMatrix(int height, int width);

   /**
    * Reads a cube (3D matrix) with specified depth, height and width
    *
    * @param depth  The desired depth (first dimension) of the 3D matrix
    * @param height The desired height (second dimension) of the 3D matrix
    * @param width  The desired width (third dimension) of the 3D matrix
    * @return A 3D matrix
    */
   int[][][] readCube(int depth, int height, int width);

   /**
    * @return The length of the DNA, measured in the number of times {@link #read()} must
    *       be called to arrive at the same spot
    */
   int size();
}
