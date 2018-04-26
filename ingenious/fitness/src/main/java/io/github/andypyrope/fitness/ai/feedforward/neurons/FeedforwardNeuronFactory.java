package io.github.andypyrope.fitness.ai.feedforward.neurons;

/**
 * Abstracts away the concrete neuron classes. Every class that implements this interface
 * should have a constructor accepting the things the specific neuron implementation
 * needs.
 * <p>
 * See https://sourcemaking.com/design_patterns/factory_method.
 */
public interface FeedforwardNeuronFactory {

   /**
    * Instantiate input, hidden and output neurons.
    *
    * @return A two-dimensional array where the first dimension is the layer index and the
    *       second dimension is the index of the neuron in that layer.
    */
   FeedforwardNeuron[][] makeAllNeurons(final int inputNeuronCount,
         final int[] hiddenLayers, final int outputNeuronCount);
}
