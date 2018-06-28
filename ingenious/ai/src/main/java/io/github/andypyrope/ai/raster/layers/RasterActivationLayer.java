package io.github.andypyrope.ai.raster.layers;

import io.github.andypyrope.ai.activation.ActivationFunction;
import io.github.andypyrope.ai.data.RasterData;
import io.github.andypyrope.ai.util.Vector;

import java.util.Random;

/**
 * The raster version of a layer of activation function neurons. There is a single bias
 * for each neuron.
 */
public class RasterActivationLayer extends RasterLayerBase {

   private final ActivationFunction _function;

   private final double[] _biases;
   private final double[] _biasVolatility;
   private final double[] _lastBiasGradients;

   private final int _calculationComplexity;
   private final int _adjustmentComplexity;

   /**
    * Creates a layer which applies an activation function to each pixel in its input.
    *
    * @param count    The number of inputs (and outputs).
    * @param size     The size (width, height, depth) of the input/output.
    * @param function The activation function of the layer.
    */
   RasterActivationLayer(final int count, final Vector size,
         final ActivationFunction function, final Random random) {

      super(count, size, count, size);
      initializeInputGradientData();

      _function = function;

      _biases = new double[_inputCount];
      _lastBiasGradients = new double[_inputCount];
      _biasVolatility = new double[_inputCount];
      for (int i = 0; i < _inputCount; i++) {
         _biases[i] = random.nextDouble() - AVERAGE_RANDOM_DOUBLE;
         _biasVolatility[i] = RPROP_INITIAL_VOLATILITY;
      }

      _calculationComplexity = count * _outputSize.getPixelCount();
      _adjustmentComplexity = count * (_inputSize.getPixelCount() * 2 + 5);
   }

   @Override
   public void adjustWithGradient(final RasterData[] outputGradients) {
      for (int i = 0; i < _inputCount; i++) {
         final RasterData inputGradient = _inputGradients[i];
         final RasterData outputGradient = outputGradients[i];
         final RasterData lastInput = _lastInput[i];
         final RasterData currentOutput = _output[i];
         final double bias = _biases[i];

         inputGradient.forEach((x, y, z) -> {
            final double slope = _function.getSlope(lastInput.getCell(x, y, z) + bias,
                  currentOutput.getCell(x, y, z));

            inputGradient.setCell(x, y, z, outputGradient.getCell(x, y, z) * slope);
         });

         final double biasGradient = inputGradient.getSum();
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
      for (int i = 0; i < _inputCount; i++) {
         final RasterData input = inputs[i];
         final RasterData output = _output[i];
         final double bias = _biases[i];

         output.forEach((x, y, z) -> output.setCell(x, y, z,
               _function.getOutput(input.getCell(x, y, z) + bias)));
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
