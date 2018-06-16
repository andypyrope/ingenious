package io.github.andypyrope.ai.testutil;

import io.github.andypyrope.ai.activation.ActivationFunction;

public class HalfFunction implements ActivationFunction {

   @Override
   public double getOutput(final double input) {
      return input * 0.5;
   }

   @Override
   public double getSlope(final double input, final double output) {
      return 0.5;
   }
}