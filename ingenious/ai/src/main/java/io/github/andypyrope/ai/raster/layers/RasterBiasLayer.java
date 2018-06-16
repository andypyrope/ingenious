package io.github.andypyrope.ai.raster.layers;

import io.github.andypyrope.ai.data.RasterData;

import java.util.Random;

// Pure OOP does not seem to make sense for layers, which are 90% algorithm.
@SuppressWarnings("FeatureEnvy")
public class RasterBiasLayer extends RasterLayerBase {

   private final int _count;
   private final double[] _biases;
   private final double[] _biasVolatility;
   private final double[] _lastBiasGradients;

   private final int _calculationComplexity;
   private final int _adjustmentComplexity;

   /**
    * Creates a convolutional layer with a set input and filter size. Its output size is
    * deduced from the target filter size. If the filter size exceeds its maximum, it is
    * shrunk down to a size that fits the layer.
    *
    * @param count  The number of inputs (and outputs).
    * @param width  The width of the input/output.
    * @param height The height of the input/output.
    * @param depth  The depth of the input/output.
    * @param random The random number generator to use.
    */
   RasterBiasLayer(final int count, final int width,
         final int height, final int depth, final Random random) {

      super(count, width, height, depth,
            count, width, height, depth);

      _count = count;

      _biases = new double[_count];
      _lastBiasGradients = new double[_count];
      _biasVolatility = new double[_count];
      for (int i = 0; i < _count; i++) {
         _biases[i] = random.nextDouble() - AVERAGE_RANDOM_DOUBLE;
         _biasVolatility[i] = RPROP_INITIAL_VOLATILITY;
      }

      _calculationComplexity = count * width * height * depth;
      _adjustmentComplexity = _calculationComplexity;
   }

   @Override
   public void adjustWithGradient(final RasterData[] outputGradients) {
      for (int i = 0; i < _inputCount; i++) {
         _inputGradients[i] = outputGradients[i];

         final double biasGradient = outputGradients[i].getSum();
         final double multipliedGradient = biasGradient * _lastBiasGradients[i];
         final double delta = Math.copySign(_biasVolatility[i], biasGradient);

         if (multipliedGradient < 0) {
            _biases[i] -= delta * RPROP_LOWER_VOLATILITY_MULTIPLIER;
            _biasVolatility[i] *= RPROP_LOWER_VOLATILITY_MULTIPLIER;
         } else if (multipliedGradient > 0) {
            _biases[i] -= delta;
            _biasVolatility[i] *= RPROP_HIGHER_VOLATILITY_MULTIPLIER;
         }

         _lastBiasGradients[i] = multipliedGradient < 0 ? 0 : biasGradient;
      }
   }

   @Override
   public void calculateWithInput(final RasterData[] inputs) {
      for (int i = 0; i < _count; i++) {
         final RasterData input = inputs[i];
         final RasterData output = _output[i];
         final double bias = _biases[i];

         output.forEach((x, y, z) ->
               output.setCell(x, y, z, input.getCell(x, y, z) + bias));
      }
   }

   @Override
   public int getCalculationComplexity() {
      return _calculationComplexity;
   }

   @Override
   public int getAdjustmentComplexity() {
      return _adjustmentComplexity;
   }
}
