package io.github.andypyrope.fitness.ai.feedforward.neurons;

import io.github.andypyrope.fitness.ai.activation.ActivationFunction;

/**
 * The RPROP (resilient back-propagation) algorithm.
 */
public class RpropFeedforwardNeuronFactory extends FeedforwardNeuronFactoryBase {

   private final ActivationFunction _function;

   /**
    * @param function The activation function to use in all neurons.
    */
   @SuppressWarnings("WeakerAccess")
   public RpropFeedforwardNeuronFactory(final ActivationFunction function) {
      _function = function;
   }

   @Override
   public FeedforwardNeuron makeNeuron(final FeedforwardNeuron[] nextLayer) {
      return new RpropFeedforwardNeuron(nextLayer, _function);
   }
}
