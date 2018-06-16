package io.github.andypyrope.ai.atomic.layers;

import java.util.Random;

/**
 * An atomic layer of neurons that add a bias.
 * <p>
 * Their input gradient is the same as their output gradient.
 */
public class BiasLayer extends AtomicLayerBase {

   private final int _count;
   private final double[] _biases;
   private final double[] _biasVolatility;
   private final double[] _lastBiasGradients;

   public BiasLayer(final int count, final Random random) {
      super(count, count);
      _count = count;

      _biases = new double[_count];
      _biasVolatility = new double[_count];
      _lastBiasGradients = new double[_count];
      for (int i = 0; i < _count; i++) {
         _biases[i] = random.nextDouble() - AVERAGE_RANDOM_DOUBLE;
         _biasVolatility[i] = RPROP_INITIAL_VOLATILITY;
      }
   }

   @Override
   protected void calculateWithInput(final double[] inputArray) {
      for (int i = 0; i < _count; i++) {
         _output[i] = inputArray[i] + _biases[i];
      }
   }

   @Override
   protected void adjustWithGradient(final double[] outputGradient) {
      for (int i = 0; i < _count; i++) {
         _inputGradients[i] = outputGradient[i];

         // RPROP
         final double gradient = _inputGradients[i];
         final double multipliedGradient = gradient * _lastBiasGradients[i];
         final double delta = Math.copySign(_biasVolatility[i], gradient);

         if (multipliedGradient < 0) {
            _biases[i] -= delta * RPROP_LOWER_VOLATILITY_MULTIPLIER;
            _biasVolatility[i] *= RPROP_LOWER_VOLATILITY_MULTIPLIER;
         } else if (multipliedGradient > 0) {
            _biases[i] -= delta;
            _biasVolatility[i] *= RPROP_HIGHER_VOLATILITY_MULTIPLIER;
         }

         _lastBiasGradients[i] = multipliedGradient < 0 ? 0 : gradient;
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
