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
    * @return The length of the DNA, measured in the number of times {@link #read()} must
    *       be called to arrive at the same spot
    */
   int size();
}
