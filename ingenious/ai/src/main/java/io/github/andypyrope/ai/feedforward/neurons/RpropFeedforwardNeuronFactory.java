package io.github.andypyrope.ai.feedforward.neurons;

import io.github.andypyrope.ai.activation.ActivationFunction;

/**
 * The RPROP (resilient back-propagation) algorithm.
 */
public class RpropFeedforwardNeuronFactory extends FeedforwardNeuronFactoryBase {

   /**
    * @param function The activation function to use in all neurons.
    */
   public RpropFeedforwardNeuronFactory(final ActivationFunction function) {
      super(function);
   }

   @Override
   protected FeedforwardNeuron makeNeuron(final FeedforwardNeuron[] nextLayer,
         final ActivationFunction function) {
      return new RpropFeedforwardNeuron(nextLayer, function);
   }
}
