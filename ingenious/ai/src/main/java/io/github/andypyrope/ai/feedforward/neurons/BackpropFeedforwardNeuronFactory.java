package io.github.andypyrope.ai.feedforward.neurons;

import io.github.andypyrope.ai.activation.ActivationFunction;

/**
 * The standard back-propagation algorithm in its simplest form.
 */
public class BackpropFeedforwardNeuronFactory extends FeedforwardNeuronFactoryBase {

   private final ActivationFunction _function;
   private final double _volatility;

   /**
    * @param function   The activation function to use in all neurons.
    * @param volatility The volatility of each neuron.
    */
   public BackpropFeedforwardNeuronFactory(final ActivationFunction function,
         final double volatility) {

      _function = function;
      _volatility = volatility;
   }

   @Override
   public FeedforwardNeuron makeNeuron(final FeedforwardNeuron[] nextLayer) {
      return new BackpropFeedforwardNeuron(nextLayer, _function, _volatility);
   }
}
