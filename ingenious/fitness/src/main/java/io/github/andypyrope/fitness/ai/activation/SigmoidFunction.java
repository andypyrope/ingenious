package io.github.andypyrope.fitness.ai.activation;

public class SigmoidFunction implements ActivationFunction {

   @Override
   public double getOutput(double input) {
      return 1.0 / (1.0 + Math.pow(Math.E, -1.0 * input));
   }

   @Override
   public double getSlope(double input, double output) {
      return output * (1.0 - output);
   }
}
