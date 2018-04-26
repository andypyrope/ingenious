package io.github.andypyrope.ai.activation;

/**
 * ReLU - Rectified linear unit
 */
public class ReLuFunction implements ActivationFunction {

   @Override
   public double getOutput(double input) {
      return input < 0.0 ? 0.0 : input;
   }

   @Override
   public double getSlope(double input, double output) {
      return input < 0.0 ? 0.0 : 1;
   }
}
