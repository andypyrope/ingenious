package io.github.andypyrope.ai.atomic.neurons;

import io.github.andypyrope.ai.activation.ActivationFunction;

/**
 * The neuron that operates in accordance with the standard back propagation algorithm in
 * its simplest form.
 */
class BackpropNeuron extends AtomicNeuronBase {

   private final double _volatility;

   BackpropNeuron(AtomicNeuron[] nextLayer,
         ActivationFunction function, double volatility) {

      super(nextLayer, function);

      _volatility = volatility;
   }

   /**
    * (non-Javadoc)
    *
    * @see AtomicNeuron#adjust()
    */
   @Override
   public void adjust() {
      _outputGradient = 0.0;
      for (int i = 0; i < _edgeCount; i++) {
         _outputGradient += _edges[i] * _nextLayer[i].getInputGradient();
      }
      _inputGradient = _function.getSlope(_netInput, _output) * _outputGradient;

      for (int i = 0; i < _edgeCount; i++) {
         final double edgeGradient = _output * _nextLayer[i].getInputGradient();
         _edges[i] -= edgeGradient * _volatility;
      }

      final double biasGradient = _inputGradient * _bias;
      _bias -= biasGradient * _volatility;
   }

   @Override
   public void adjust(double targetOutput) {
      _outputGradient = _output - targetOutput;
      _inputGradient = _function.getSlope(_netInput, _output) * _outputGradient;

      final double biasGradient = _inputGradient * _bias;
      _bias -= biasGradient * _volatility;
   }
}
