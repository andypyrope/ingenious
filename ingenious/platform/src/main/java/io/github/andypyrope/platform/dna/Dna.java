package io.github.andypyrope.platform.dna;

/**
 * Repesents a single strand of DNA.
 */
public interface Dna {
   /**
    * Produces mutations in the DNA
    * 
    * @param mutationProbability The probability of a single element in the DNA
    *           being mutated
    */
   void mutate(double mutationProbability);

   /**
    * Returns the reading pointer to the first position
    */
   void resetReader();

   /**
    * Reads a single <b>unsigned</b> integer. Its minimum and maximum values
    * aren't necessarily {@link Integer#MIN_VALUE and Integer#MAX_VALUE}! This
    * may or may not return the same as {@link #readSigned()}.
    * 
    * @return An unsigned integer value
    */
   int read();

   /**
    * Reads a single integer. Its minimum and maximum values aren't necessarily
    * {@link Integer#MIN_VALUE and Integer#MAX_VALUE}!
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
    * @param length
    * @return An integer array
    */
   int[] readArray(int length);

   /**
    * Reads a matrix with specified height and width
    * 
    * @param height
    * @param width
    * @return A matrix
    */
   int[][] readMatrix(int height, int width);

   /**
    * Reads a cube (3D matrix) with specified depth, height and width
    * 
    * @param depth
    * @param height
    * @param width
    * @return A 3D matrix
    */
   int[][][] readCube(int depth, int height, int width);

   /**
    * @return The length of the DNA, measured in the number of times
    *         {@link #read()} must be called to arrive at the same spot
    */
   int size();
}
