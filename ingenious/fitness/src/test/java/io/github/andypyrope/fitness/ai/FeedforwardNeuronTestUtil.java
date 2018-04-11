package io.github.andypyrope.fitness.ai;

/**
 * Utility class for {@link FeedforwardNeuron} tests.
 */
class FeedforwardNeuronTestUtil {

   static void makePass(FeedforwardNeuron[][] neurons, double[] input) {
      for (final FeedforwardNeuron[] neuron1 : neurons) {
         for (FeedforwardNeuron neuron : neuron1) {
            neuron.resetNetInput();
         }
      }

      for (int i = 0; i < input.length; i++) {
         neurons[0][i].setOutput(input[i]);
      }

      for (int i = 1; i < neurons.length; i++) {
         for (FeedforwardNeuron neuron : neurons[i]) {
            neuron.updateOutput();
            neuron.propagate();
         }
      }
   }

   static void adjust(FeedforwardNeuron[][] neurons, double[] output) {
      // Output
      for (int i = 0; i < neurons[neurons.length - 1].length; i++) {
         neurons[neurons.length - 1][i].calculateGradient(output[i]);
         neurons[neurons.length - 1][i].adjust();
      }

      // Hidden
      for (int i = neurons.length - 2; i >= 1; i--) {
         for (FeedforwardNeuron neuron : neurons[i]) {
            neuron.calculateGradient();
            neuron.adjust();
         }
      }

      // Input
      for (FeedforwardNeuron neuron : neurons[0]) {
         neuron.adjust();
      }
   }
}
