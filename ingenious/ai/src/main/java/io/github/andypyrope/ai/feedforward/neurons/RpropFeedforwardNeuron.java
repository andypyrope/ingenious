package io.github.andypyrope.ai.feedforward.neurons;

import io.github.andypyrope.ai.activation.ActivationFunction;

/**
 * The neuron that operates in accordance with the RPROP (resilient propagation)
 * algorithm. It is almost the same as the standard backward propagation algorithm with
 * the only difference being that the volatility of the edges/biases changes (according to
 * the gradient) instead of staying with the same value. One of the many benefits of this
 * algorithm is that it counteracts the vanishing gradient problem.
 */
class RpropFeedforwardNeuron extends FeedforwardNeuronBase {

   private static final double LOWER_VOLATILITY_MULTIPLIER = 0.5;
   private static final double HIGHER_VOLATILITY_MULTIPLIER = 1.1;
   private static final double INITIAL_VOLATILITY = 0.1;

   private final double[] _edgeVolatility;
   private final double[] _lastEdgeGradients;

   private double _biasVolatility = INITIAL_VOLATILITY;
   private double _lastBiasGradient;

   RpropFeedforwardNeuron(FeedforwardNeuron[] nextLayer, ActivationFunction function) {

      super(nextLayer, function);

      _lastEdgeGradients = new double[_edgeCount];
      _edgeVolatility = new double[_edgeCount];
      for (int i = 0; i < _edgeCount; i++) {
         _edgeVolatility[i] = INITIAL_VOLATILITY;
      }
   }

   /**
    * (non-Javadoc)
    *
    * @see FeedforwardNeuron#adjust()
    */
   @Override
   public void adjust() {
      _outputGradient = 0.0;
      for (int i = 0; i < _edges.length; i++) {
         _outputGradient += _edges[i] * _nextLayer[i].getInputGradient();
      }

      _inputGradient = _function.getSlope(_netInput, _output) * _outputGradient;

      for (int i = 0; i < _edgeCount; i++) {
         adjustEdge(i);
      }
      adjustBias();
   }

   /**
    * (non-Javadoc)
    *
    * @see FeedforwardNeuron#adjust(double)
    */
   @Override
   public void adjust(final double targetOutput) {
      _outputGradient = _output - targetOutput;
      _inputGradient = _function.getSlope(_netInput, _output) * _outputGradient;

      adjustBias();
   }

   private void adjustEdge(int index) {
      final double gradient = _output * _nextLayer[index].getInputGradient();
      final double multipliedGradient = gradient * _lastEdgeGradients[index];
      final double delta = Math.copySign(_edgeVolatility[index], gradient);

      if (multipliedGradient < 0) {
         _edges[index] -= delta * LOWER_VOLATILITY_MULTIPLIER;
         _edgeVolatility[index] *= LOWER_VOLATILITY_MULTIPLIER;
      } else if (multipliedGradient > 0) {
         _edges[index] -= delta;
         _edgeVolatility[index] *= HIGHER_VOLATILITY_MULTIPLIER;
      }

      _lastEdgeGradients[index] = multipliedGradient < 0 ? 0 : gradient;
   }

   private void adjustBias() {
      final double gradient = _inputGradient * _bias;
      final double multipliedGradient = gradient * _lastBiasGradient;
      final double delta = Math.copySign(_biasVolatility, gradient);

      if (multipliedGradient < 0) {
         _bias -= delta * LOWER_VOLATILITY_MULTIPLIER;
         _biasVolatility *= LOWER_VOLATILITY_MULTIPLIER;
      } else if (multipliedGradient > 0) {
         _bias -= delta;
         _biasVolatility *= HIGHER_VOLATILITY_MULTIPLIER;
      }

      _lastBiasGradient = multipliedGradient < 0 ? 0 : gradient;
   }
}
