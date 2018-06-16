package io.github.andypyrope.ai.atomic.layers;

import java.util.Random;

/**
 * An atomic layer of edges.
 */
public class FullyConnectedLayer extends AtomicLayerBase {

   private final double[][] _edgeVolatility;
   private final double[][] _lastEdgeGradients;

   private final double[][] _edgeWeights;

   public FullyConnectedLayer(final int inputSize, final int outputSize,
         final Random random) {

      super(inputSize, outputSize);

      _edgeWeights = new double[_inputCount][_outputCount];
      _edgeVolatility = new double[_inputCount][_outputCount];
      _lastEdgeGradients = new double[_inputCount][_outputCount];

      // Multiply the edges by this constant in order to keep the output values as close
      // as possible to the input values.
      final double edgeMultiplier = 1.0 / (_inputCount * AVERAGE_RANDOM_DOUBLE);
      for (int i = 0; i < _inputCount; i++) {
         for (int j = 0; j < _outputCount; j++) {
            _edgeWeights[i][j] = random.nextDouble() * edgeMultiplier;
            _edgeVolatility[i][j] = RPROP_INITIAL_VOLATILITY;
         }
      }
   }

   @Override
   protected void calculateWithInput(final double[] input) {
      for (int i = 0; i < _outputCount; i++) {
         _output[i] = 0.0;
      }

      for (int i = 0; i < _inputCount; i++) {
         for (int j = 0; j < _outputCount; j++) {
            _output[j] += input[i] * _edgeWeights[i][j];
         }
      }
   }

   @Override
   protected void adjustWithGradient(final double[] outputGradient) {
      for (int i = 0; i < _inputCount; i++) {
         _inputGradients[i] = 0.0;
         for (int j = 0; j < _outputCount; j++) {
            _inputGradients[i] += _edgeWeights[i][j] * outputGradient[j];

            final double gradient = _lastInput[i] * outputGradient[j];

            // RPROP
            final double multipliedGradient = gradient * _lastEdgeGradients[i][j];
            final double delta = Math.copySign(_edgeVolatility[i][j], gradient);

            if (multipliedGradient < 0) {
               _edgeWeights[i][j] -= delta * RPROP_LOWER_VOLATILITY_MULTIPLIER;
               _edgeVolatility[i][j] *= RPROP_LOWER_VOLATILITY_MULTIPLIER;
            } else if (multipliedGradient > 0) {
               _edgeWeights[i][j] -= delta;
               _edgeVolatility[i][j] *= RPROP_HIGHER_VOLATILITY_MULTIPLIER;
            }

            _lastEdgeGradients[i][j] = multipliedGradient < 0 ? 0 : gradient;
         }
      }
   }

   @Override
   public int getCalculationComplexity() {
      return _inputCount * _outputCount;
   }

   @Override
   public int getAdjustmentComplexity() {
      return _inputCount * _outputCount;
   }
}
