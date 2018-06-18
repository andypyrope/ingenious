package io.github.andypyrope.ai.atomic;

import io.github.andypyrope.ai.InvalidSizeException;
import io.github.andypyrope.ai.NetworkLayer;
import io.github.andypyrope.ai.NoCalculationException;

public interface AtomicLayer extends NetworkLayer {

   /**
    * @param input The input to calculate the output with.
    * @throws InvalidSizeException If the given input array is not with the expected size.
    */
   void calculate(double[] input) throws InvalidSizeException;

   /**
    * @param targetOutput The target output.
    * @return The euclidean distance from the last calculated output to the target output.
    * @throws NoCalculationException If no calculation has been made yet.
    * @throws InvalidSizeException      If the target output is not with the expected size.
    */
   double getEuclideanDistance(double[] targetOutput)
         throws NoCalculationException, InvalidSizeException;

   /**
    * @param targetOutput The array that contains the target atomic data. This array is
    *                     used for learning and is not modified.
    * @throws NoCalculationException If no calculation has been made yet.
    * @throws InvalidSizeException      If the target output is not with the expected size.
    */
   void adjust(double[] targetOutput) throws InvalidSizeException;
}
