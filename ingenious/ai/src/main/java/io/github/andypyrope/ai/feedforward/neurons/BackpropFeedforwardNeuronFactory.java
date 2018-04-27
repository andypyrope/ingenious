package io.github.andypyrope.ai.feedforward.neurons;

import io.github.andypyrope.ai.activation.ActivationFunction;

/**
 * The standard back-propagation algorithm in its simplest form.
 */
public class BackpropFeedforwardNeuronFactory extends FeedforwardNeuronFactoryBase {

   private final double _volatility;

   /**
    * @param function   The activation function to use.
    * @param volatility The volatility of each neuron.
    */
   public BackpropFeedforwardNeuronFactory(final ActivationFunction function,
         final double volatility) {

      super(function);
      _volatility = volatility;
   }

   @Override
   protected FeedforwardNeuron makeNeuron(final FeedforwardNeuron[] nextLayer,
         final ActivationFunction function) {
      return new BackpropFeedforwardNeuron(nextLayer, function, _volatility);
   }
}
