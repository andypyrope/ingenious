package io.github.andypyrope.ai.atomic;

import io.github.andypyrope.ai.MismatchException;
import io.github.andypyrope.ai.NetworkLayer;
import io.github.andypyrope.ai.NoCalculationException;

public interface AtomicLayer extends NetworkLayer {

   /**
    * @param inputArray The input to calculate the output with.
    * @throws MismatchException If the given input array is not with the expected size.
    */
   void calculate(double[] inputArray) throws MismatchException;

   /**
    * @param targetOutput The target output.
    * @return The euclidean distance from the last calculated output to the target output.
    * @throws NoCalculationException If no calculation has been made yet.
    * @throws MismatchException      If the target output is not with the expected size.
    */
   double getEuclideanDistance(double[] targetOutput)
         throws NoCalculationException, MismatchException;

   /**
    * @param targetOutput The array that contains the target atomic data. This array is
    *                     used for learning and is not modified.
    * @throws NoCalculationException If no calculation has been made yet.
    * @throws MismatchException      If the target output is not with the expected size.
    */
   void adjust(double[] targetOutput) throws MismatchException;
}
