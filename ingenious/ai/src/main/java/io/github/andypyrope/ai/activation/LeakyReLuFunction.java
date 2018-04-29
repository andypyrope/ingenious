package io.github.andypyrope.ai.activation;

/**
 * Leaky ReLU - the same as ReLU, but not with a zero slope when its input is below 0.
 * Instead, it has a parameter that determines the slope of the function when its input is
 * less than 0. This modification addresses the issue with the dying neurons that the ReLU
 * activation function is notorious for. This is one of the most used activation functions
 * thanks to its simplicity and efficiency.
 */
public class LeakyReLuFunction implements ActivationFunction {

   private final double _slope;

   /**
    * @param slope The slope that the function has when the input is less than 0. The
    *              function itself is `slope * x`. A value of around 0.1 is preferred.
    */
   public LeakyReLuFunction(final double slope) {
      _slope = slope;
   }

   @Override
   public double getOutput(double input) {
      return input < 0.0 ? _slope * input : input;
   }

   @Override
   public double getSlope(double input, double output) {
      return input < 0.0 ? _slope : 1;
   }
}
