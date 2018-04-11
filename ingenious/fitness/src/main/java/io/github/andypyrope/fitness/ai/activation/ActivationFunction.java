package io.github.andypyrope.fitness.ai.activation;

/**
 * A function which:
 * <ol>
 * <li>Is non-decreasing</li>
 * <li>Has a defined derivative</li>
 * <li>Usually has a constrained output range</li>
 * </ol>
 * This function is used to transform the total net input of a node into an
 * output, which is then passed onto subsequent neurons
 */
public interface ActivationFunction {
   /**
    * Runs the function with 'input' as its parameter
    *
    * @param input The total net input of a neuron
    * @return The actual output of that neuron after it has been manipulated by
    *         the activation function
    */
   double getOutput(double input);

   /**
    * Runs the derivative of the function with 'input' as its parameter. However
    * sometimes the output of the neuron can be used for more efficient
    * calculation.
    * 
    * @param input The neuron input
    * @param output The neuron output
    * @return The slope of the activation function for that neuron
    */
   double getSlope(double input, double output);
}
