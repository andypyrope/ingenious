package io.github.andypyrope.ai.atomic.neurons;

import io.github.andypyrope.ai.activation.ActivationFunction;

/**
 * The standard back-propagation algorithm in its simplest form.
 */
public class BackpropNeuronFactory extends AtomicNeuronFactoryBase {

   private final double _volatility;

   /**
    * @param function   The activation function to use.
    * @param volatility The volatility of each neuron.
    */
   public BackpropNeuronFactory(final ActivationFunction function,
         final double volatility) {

      super(function);
      _volatility = volatility;
   }

   @Override
   protected AtomicNeuron makeNeuron(final AtomicNeuron[] nextLayer,
         final ActivationFunction function) {
      return new BackpropNeuron(nextLayer, function, _volatility);
   }
}
