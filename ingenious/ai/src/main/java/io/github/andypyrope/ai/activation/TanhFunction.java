package io.github.andypyrope.ai.activation;

public class TanhFunction implements ActivationFunction {

   @Override
   public double getOutput(double input) {
      return Math.tanh(input);
   }

   @Override
   public double getSlope(double input, double output) {
      return 1 - output * output;
   }
}
