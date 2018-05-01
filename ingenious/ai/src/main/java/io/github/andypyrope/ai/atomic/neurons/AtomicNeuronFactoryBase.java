package io.github.andypyrope.ai.atomic.neurons;

import io.github.andypyrope.ai.activation.ActivationFunction;
import io.github.andypyrope.ai.activation.IdentityFunction;

abstract class AtomicNeuronFactoryBase implements AtomicNeuronFactory {

   private final ActivationFunction _function;

   /**
    * @param function The activation function to use.
    */
   AtomicNeuronFactoryBase(final ActivationFunction function) {
      _function = function;
   }

   /**
    * Instantiate an input neuron by providing data that may be necessary for finishing
    * the construction of the neuron.
    *
    * @param nextLayer The neurons in the next layer of the neural network.
    * @return A new {@link AtomicNeuron} instance.
    */
   private AtomicNeuron makeInputNeuron(AtomicNeuron[] nextLayer) {
      return makeNeuron(nextLayer, new IdentityFunction());
   }

   /**
    * Instantiate a hidden neuron by providing data that may be necessary for finishing
    * the construction of the neuron.
    *
    * @param nextLayer The neurons in the next layer of the neural network.
    * @return A new {@link AtomicNeuron} instance.
    */
   private AtomicNeuron makeHiddenNeuron(AtomicNeuron[] nextLayer) {
      return makeNeuron(nextLayer, _function);
   }

   /**
    * Instantiate an output neuron by providing data that may be necessary for finishing
    * the construction of the neuron.
    *
    * @return A new {@link AtomicNeuron} instance.
    */
   private AtomicNeuron makeOutputNeuron() {
      return makeNeuron(null, new IdentityFunction());
   }

   /**
    * Creates a neuron, not caring if it is an input, hidden or output one.
    * <p>
    * This method helps avoid having to override {@link #makeInputNeuron(AtomicNeuron[])},
    * {@link #makeHiddenNeuron(AtomicNeuron[])} and {@link #makeOutputNeuron()} when
    * their implementations are the same.
    *
    * @param nextLayer The next layer in the neural network.
    * @param function The activation function to use.
    * @return The newly created {@link AtomicNeuron} instance.
    */
   protected abstract AtomicNeuron makeNeuron(final AtomicNeuron[] nextLayer,
         final ActivationFunction function);

   @Override
   public AtomicNeuron[][] makeAllNeurons(final int inputNeuronCount,
         final int[] hiddenLayers, final int outputNeuronCount) {

      final AtomicNeuron[][] result =
            new AtomicNeuron[hiddenLayers.length + 2][];

      result[result.length - 1] = new AtomicNeuron[outputNeuronCount];
      for (int i = 0; i < outputNeuronCount; i++) {
         result[result.length - 1][i] = makeOutputNeuron();
      }

      for (int i = result.length - 2; i >= 1; i--) {
         result[i] = new AtomicNeuron[hiddenLayers[i - 1]];
         for (int j = 0; j < hiddenLayers[i - 1]; j++) {
            result[i][j] = makeHiddenNeuron(result[i + 1]);
         }
      }

      result[0] = new AtomicNeuron[outputNeuronCount];
      for (int i = 0; i < outputNeuronCount; i++) {
         result[0][i] = makeInputNeuron(result[1]);
      }

      return result;
   }
}
