package io.github.andypyrope.ai.atomic.layers;

import io.github.andypyrope.ai.activation.ActivationFunction;

/**
 * An atomic layer of neurons that only have an activation function.
 * <p>
 * It has no learnable parameters.
 */
public class ActivationLayer extends AtomicLayerBase {

   private final int _size;
   private final ActivationFunction _function;

   public ActivationLayer(final int size, final ActivationFunction function) {
      super(size, size);
      _size = size;
      _function = function;
   }

   @Override
   protected void calculateWithInput(final double[] inputArray) {
      for (int i = 0; i < _size; i++) {
         _output[i] = _function.getOutput(inputArray[i]);
      }
   }

   @Override
   protected void adjustWithGradient(final double[] outputGradient) {
      for (int i = 0; i < _size; i++) {
         _inputGradient[i] = _function.getSlope(_lastInput[i], _output[i]) *
               outputGradient[i];
      }
   }

   @Override
   public int getCalculationComplexity() {
      return _size;
   }

   @Override
   public int getAdjustmentComplexity() {
      return _size;
   }
}
