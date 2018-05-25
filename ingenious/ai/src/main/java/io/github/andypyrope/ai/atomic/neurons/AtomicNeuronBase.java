package io.github.andypyrope.ai.atomic.neurons;

import io.github.andypyrope.ai.activation.ActivationFunction;

public abstract class AtomicNeuronBase implements AtomicNeuron {

   private static final double INITIAL_BIAS = 0.0;

   final int _edgeCount;

   final AtomicNeuron[] _nextLayer;
   double _inputGradient;
   final double[] _edges;
   final ActivationFunction _function;
   double _outputGradient;
   double _bias;
   double _netInput;
   double _output;

   AtomicNeuronBase(final AtomicNeuron[] nextLayer, final ActivationFunction function) {
      _bias = INITIAL_BIAS;
      _function = function;
      _nextLayer = nextLayer == null ? new AtomicNeuron[0] : nextLayer;

      _edgeCount = _nextLayer.length;

      double t = 1.0;
      final double edgeWeightMultiplier = 0.5 / _edgeCount;

      _edges = new double[_edgeCount];
      for (int i = 0; i < _edgeCount; i++) {
         _edges[i] = Math.sin(t++) * edgeWeightMultiplier;
      }
   }

   /**
    * (non-Javadoc)
    *
    * @see AtomicNeuron#setNetInput(double)
    */
   @Override
   public void setNetInput(final double netInput) {
      _netInput = netInput;
   }

   /**
    * (non-Javadoc)
    *
    * @see AtomicNeuron#getOutput()
    */
   @Override
   public double getOutput() {
      return _output;
   }

   /**
    * (non-Javadoc)
    *
    * @see AtomicNeuron#propagate()
    */
   @Override
   public void propagate() {
      _output = _function.getOutput(_netInput + _bias);

      for (int i = 0; i < _edgeCount; i++) {
         _nextLayer[i].addInto(_output * _edges[i]);
      }
   }

   @Override
   public void addInto(final double amount) {
      _netInput += amount;
   }

   @Override
   public double getInputGradient() {
      return _inputGradient;
   }
}