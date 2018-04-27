package io.github.andypyrope.ai.feedforward.neurons;

import io.github.andypyrope.ai.activation.ActivationFunction;
import io.github.andypyrope.ai.activation.IdentityFunction;

abstract class FeedforwardNeuronFactoryBase implements FeedforwardNeuronFactory {

   private final ActivationFunction _function;

   /**
    * @param function The activation function to use.
    */
   FeedforwardNeuronFactoryBase(final ActivationFunction function) {
      _function = function;
   }

   /**
    * Instantiate an input neuron by providing data that may be necessary for finishing
    * the construction of the neuron.
    *
    * @param nextLayer The neurons in the next layer of the neural network.
    * @return A new {@link FeedforwardNeuron} instance.
    */
   private FeedforwardNeuron makeInputNeuron(FeedforwardNeuron[] nextLayer) {
      return makeNeuron(nextLayer, new IdentityFunction());
   }

   /**
    * Instantiate a hidden neuron by providing data that may be necessary for finishing
    * the construction of the neuron.
    *
    * @param nextLayer The neurons in the next layer of the neural network.
    * @return A new {@link FeedforwardNeuron} instance.
    */
   private FeedforwardNeuron makeHiddenNeuron(FeedforwardNeuron[] nextLayer) {
      return makeNeuron(nextLayer, _function);
   }

   /**
    * Instantiate an output neuron by providing data that may be necessary for finishing
    * the construction of the neuron.
    *
    * @return A new {@link FeedforwardNeuron} instance.
    */
   private FeedforwardNeuron makeOutputNeuron() {
      return makeNeuron(null, new IdentityFunction());
   }

   /**
    * Creates a neuron, not caring if it is an input, hidden or output one.
    * <p>
    * This method helps avoid having to override {@link #makeInputNeuron(FeedforwardNeuron[])},
    * {@link #makeHiddenNeuron(FeedforwardNeuron[])} and {@link #makeOutputNeuron()} when
    * their implementations are the same.
    *
    * @param nextLayer The next layer in the neural network.
    * @param function The activation function to use.
    * @return The newly created {@link FeedforwardNeuron} instance.
    */
   protected abstract FeedforwardNeuron makeNeuron(final FeedforwardNeuron[] nextLayer,
         final ActivationFunction function);

   @Override
   public FeedforwardNeuron[][] makeAllNeurons(final int inputNeuronCount,
         final int[] hiddenLayers, final int outputNeuronCount) {

      final FeedforwardNeuron[][] result =
            new FeedforwardNeuron[hiddenLayers.length + 2][];

      result[result.length - 1] = new FeedforwardNeuron[outputNeuronCount];
      for (int i = 0; i < outputNeuronCount; i++) {
         result[result.length - 1][i] = makeOutputNeuron();
      }

      for (int i = result.length - 2; i >= 1; i--) {
         result[i] = new FeedforwardNeuron[hiddenLayers[i - 1]];
         for (int j = 0; j < hiddenLayers[i - 1]; j++) {
            result[i][j] = makeHiddenNeuron(result[i + 1]);
         }
      }

      result[0] = new FeedforwardNeuron[outputNeuronCount];
      for (int i = 0; i < outputNeuronCount; i++) {
         result[0][i] = makeInputNeuron(result[1]);
      }

      return result;
   }
}
