package io.github.andypyrope.ai.atomic.layers;

import java.util.Random;

/**
 * An atomic layer of neurons that add a bias.
 * <p>
 * Their input gradient is the same as their output gradient.
 */
public class BiasLayer extends AtomicLayerBase {

   private static final double INITIAL_BIAS_OFFSET = AVERAGE_RANDOM_DOUBLE * -1;

   private final int _size;
   private final double[] _biases;
   private final double[] _biasVolatility;
   private final double[] _lastBiasGradients;

   public BiasLayer(final int size, final Random random) {
      super(size, size);
      _size = size;

      _biases = new double[_size];
      _biasVolatility = new double[_size];
      _lastBiasGradients = new double[_size];
      for (int i = 0; i < _size; i++) {
         _biases[i] = random.nextDouble() + INITIAL_BIAS_OFFSET;
         _biasVolatility[i] = RPROP_INITIAL_VOLATILITY;
      }
   }

   @Override
   protected void calculateWithInput(final double[] inputArray) {
      for (int i = 0; i < _size; i++) {
         _output[i] = inputArray[i] + _biases[i];
      }
   }

   @Override
   protected void adjustWithGradient(final double[] outputGradient) {
      for (int i = 0; i < _size; i++) {
         _inputGradient[i] = outputGradient[i];

         // RPROP
         final double gradient = _inputGradient[i];
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
      return _size;
   }

   @Override
   public int getAdjustmentComplexity() {
      return _size;
   }
}
