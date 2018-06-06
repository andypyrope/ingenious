package io.github.andypyrope.ai.atomic;

import io.github.andypyrope.ai.NeuralNetwork;
import io.github.andypyrope.ai.NoCalculationException;

/**
 * A neural network which accepts an array of as many primitive real numbers as it has
 * input neurons, and after calculation its output is an array of as many primitive real
 * numbers as it has output neurons.
 * <p>
 * The "atomic" in its name comes from the fact that the value in each neuron is atomic -
 * a single real number as opposed to something complex.
 */
public interface AtomicNetwork extends NeuralNetwork {

   /**
    * Finds out what the output of the network is, given a specific input.
    *
    * @param inputValues The input of the network.
    */
   void calculate(double[] inputValues);

   /**
    * Runs an algorithm to improve the weights/biases.
    *
    * @param targetOutput The expected output
    * @throws NoCalculationException If no calculation has been made yet.
    */
   void adjust(double[] targetOutput) throws NoCalculationException;

   /**
    * @return The output of this neural network.
    * @throws NoCalculationException If no calculation has been made yet.
    */
   double[] getOutput() throws NoCalculationException;

   /**
    * @param targetOutput The expected output.
    * @return The euclidean distance between the actual output and an expected one.
    * @throws NoCalculationException If no calculation has been made yet.
    */
   double getEuclideanDistance(double[] targetOutput) throws NoCalculationException;
}
