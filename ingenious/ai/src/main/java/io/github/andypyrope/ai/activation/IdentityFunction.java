package io.github.andypyrope.ai.activation;

public class IdentityFunction implements ActivationFunction {

   @Override
   public double getOutput(double input) {
      return input;
   }

   @Override
   public double getSlope(double input, double output) {
      return 1.0;
   }
}
