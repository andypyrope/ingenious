package io.github.andypyrope.ai.raster.layers;

import io.github.andypyrope.ai.data.RasterData;
import io.github.andypyrope.ai.util.RasterSize;

import java.util.Random;

// Pure OOP does not seem to make sense for layers, which are 90% algorithm.
@SuppressWarnings("FeatureEnvy")
public class RasterBiasLayer extends RasterLayerBase {

   private final double[] _biases;
   private final double[] _biasVolatility;
   private final double[] _lastBiasGradients;

   private final int _calculationComplexity;
   private final int _adjustmentComplexity;

   /**
    * Creates a layer which adds a learnable bias to each of its inputs of type {@link
    * RasterData} with specified dimensions and count.
    *
    * @param count  The number of inputs (and outputs).
    * @param size   The size (width, height, depth) of the input/output.
    * @param random The random number generator to use.
    */
   RasterBiasLayer(final int count, final RasterSize size, final Random random) {
      super(count, size, count, size);

      _biases = new double[_inputCount];
      _lastBiasGradients = new double[_inputCount];
      _biasVolatility = new double[_inputCount];
      for (int i = 0; i < _inputCount; i++) {
         _biases[i] = random.nextDouble() - AVERAGE_RANDOM_DOUBLE;
         _biasVolatility[i] = RPROP_INITIAL_VOLATILITY;
      }

      _calculationComplexity = count * size.getPixelCount();
      _adjustmentComplexity = _calculationComplexity;
   }

   @Override
   public void calculateWithInput(final RasterData[] inputs) {
      for (int i = 0; i < _inputCount; i++) {
         final RasterData input = inputs[i];
         final RasterData output = _output[i];
         final double bias = _biases[i];

         output.forEach((x, y, z) ->
               output.setCell(x, y, z, input.getCell(x, y, z) + bias));
      }
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
   public int getCalculationComplexity() {
      return _calculationComplexity;
   }

   @Override
   public int getAdjustmentComplexity() {
      return _adjustmentComplexity;
   }
}
