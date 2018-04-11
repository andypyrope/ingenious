package io.github.andypyrope.fitness.ai;

import io.github.andypyrope.fitness.ai.activation.ActivationFunction;

/**
 * The neuron that operates in accordance with the RPROP (resilient propagation)
 * algorithm. It is almost the same as the standard backward propagation algorithm with
 * the only difference being that the volatility of the edges/biases changes (according to
 * the gradient) instead of staying with the same value. One of the many benefits of this
 * algorithm is that it counteracts the vanishing gradient problem.
 */
class RpropFeedforwardNeuron extends FeedforwardNeuronBase {

   private static final double LOWER_VOLATILITY_MULTIPLIER = 0.5;
   private static final double HIGHER_VOLATILITY_MULTIPLIER = 1.2;
   private static final double INITIAL_VOLATILITY = 0.1;

   private double _inputGradient;
   private double _outputGradient;
   private final double[] _edgeVolatility;
   private final double[] _lastEdgeGradients;
   private final RpropFeedforwardNeuron[] _nextLayer;

   private double _biasVolatility = INITIAL_VOLATILITY;
   private double _lastBiasGradient;

   RpropFeedforwardNeuron(ActivationFunction function,
         RpropFeedforwardNeuron[] nextLayer) {

      super(function, nextLayer);
      _nextLayer = nextLayer;

      _lastEdgeGradients = new double[_edgeCount];
      _edgeVolatility = new double[_edgeCount];
      for (int i = 0; i < _edgeCount; i++) {
         _edgeVolatility[i] = INITIAL_VOLATILITY;
      }
   }

   /**
    * (non-Javadoc)
    *
    * @see io.github.andypyrope.fitness.ai.FeedforwardNeuron#calculateGradient()
    */
   @Override
   public void calculateGradient() {
      _outputGradient = 0.0;
      for (int i = 0; i < _edges.length; i++) {
         _outputGradient += _edges[i] * _nextLayer[i]._inputGradient;
      }

      _inputGradient = _function.getSlope(_netInput, _output) * _outputGradient;
   }

   /**
    * (non-Javadoc)
    *
    * @see io.github.andypyrope.fitness.ai.FeedforwardNeuron#calculateGradient(double)
    */
   @Override
   public void calculateGradient(double targetOutput) {
      _outputGradient = _output - targetOutput;
      _inputGradient = _function.getSlope(_netInput, _output) * _outputGradient;
   }

   /**
    * (non-Javadoc)
    *
    * @see io.github.andypyrope.fitness.ai.FeedforwardNeuron#adjust()
    */
   @Override
   public void adjust() {
      for (int i = 0; i < _edgeCount; i++) {
         adjustEdge(i);
      }
      adjustBias();
   }

   private void adjustEdge(int index) {
      final double gradient = _output * _nextLayer[index]._inputGradient;
      final double gradientMult = gradient * _lastEdgeGradients[index];

      _edges[index] -= getDelta(gradient,
            _lastEdgeGradients[index],
            _edgeVolatility[index]);

      if (gradientMult < 0) {
         _edgeVolatility[index] *= LOWER_VOLATILITY_MULTIPLIER;
      } else if (gradientMult > 0) {
         _edgeVolatility[index] *= HIGHER_VOLATILITY_MULTIPLIER;
      }
      _lastEdgeGradients[index] = gradientMult < 0 ? 0 : gradient;
   }

   private void adjustBias() {
      final double gradient = _inputGradient * _bias;
      final double gradientMult = gradient * _lastBiasGradient;

      _bias -= getDelta(gradient, _lastBiasGradient, _biasVolatility);

      if (gradientMult < 0) {
         _biasVolatility *= LOWER_VOLATILITY_MULTIPLIER;
      } else if (gradientMult > 0) {
         _biasVolatility *= HIGHER_VOLATILITY_MULTIPLIER;
      }
      _lastBiasGradient = gradientMult < 0 ? 0 : gradient;
   }

   private double getDelta(double gradient, double lastGradient,
         double volatility) {

      double delta = Math.copySign(volatility, gradient);
      return gradient * lastGradient < 0 ? -1 * delta : delta;
   }
}
