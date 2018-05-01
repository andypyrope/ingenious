package io.github.andypyrope.ai.atomic.neurons;

import io.github.andypyrope.ai.activation.ActivationFunction;

/**
 * The RPROP (resilient back-propagation) algorithm.
 */
public class RpropNeuronFactory extends AtomicNeuronFactoryBase {

   /**
    * @param function The activation function to use in all neurons.
    */
   public RpropNeuronFactory(final ActivationFunction function) {
      super(function);
   }

   @Override
   protected AtomicNeuron makeNeuron(final AtomicNeuron[] nextLayer,
         final ActivationFunction function) {
      return new RpropNeuron(nextLayer, function);
   }
}
