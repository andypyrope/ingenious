package io.github.andypyrope.ai.atomic.layers;

import io.github.andypyrope.ai.activation.ActivationFunction;

/**
 * An atomic layer of neurons that only have an activation function.
 * <p>
 * It has no learnable parameters.
 */
public class ActivationLayer extends AtomicLayerBase {

   private final int _count;
   private final ActivationFunction _function;

   public ActivationLayer(final int count, final ActivationFunction function) {
      super(count, count);
      _count = count;
      _function = function;
   }

   @Override
   protected void calculateWithInput(final double[] inputArray) {
      for (int i = 0; i < _count; i++) {
         _output[i] = _function.getOutput(inputArray[i]);
      }
   }

   @Override
   protected void adjustWithGradient(final double[] outputGradient) {
      for (int i = 0; i < _count; i++) {
         _inputGradients[i] = _function.getSlope(_lastInput[i], _output[i]) *
               outputGradient[i];
      }
   }

   @Override
   public int getCalculationComplexity() {
      return _count;
   }

   @Override
   public int getAdjustmentComplexity() {
      return _count;
   }
}
