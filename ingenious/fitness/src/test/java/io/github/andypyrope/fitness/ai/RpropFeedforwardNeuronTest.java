package io.github.andypyrope.fitness.ai;

import io.github.andypyrope.fitness.ai.activation.LogisticFunction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RpropFeedforwardNeuronTest {

   @Test
   void testCalculateGradientAndAdjust() {
      final FeedforwardNeuron[][] neurons = makeNeurons(
         new int[] { 1, 10, 1 });

      neurons[0][0].setOutput(0.24);

      final double[] input = new double[] { 0.74 };
      final double[] output = new double[] { 0.14 };

      makePass(neurons, input);
      compareDoubles(0.993307, neurons[neurons.length - 1][0].getOutput());

      adjust(neurons, output);
      makePass(neurons, input);
      compareDoubles(0.984860, neurons[neurons.length - 1][0].getOutput());

      for (int i = 0; i < 30; i++) {
         adjust(neurons, output);
         makePass(neurons, input);
      }
      compareDoubles(0.081766, neurons[neurons.length - 1][0].getOutput());
   }

   private void makePass(FeedforwardNeuron[][] neurons, double[] input) {
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

   private void adjust(FeedforwardNeuron[][] neurons, double[] output) {
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

   private FeedforwardNeuron[][] makeNeurons(int[] count) {
      final RpropFeedforwardNeuron[][] result = new RpropFeedforwardNeuron[count.length][];

      for (int i = result.length - 1; i >= 0; i--) {
         result[i] = new RpropFeedforwardNeuron[count[i]];
         for (int j = 0; j < count[i]; j++) {
            result[i][j] = new RpropFeedforwardNeuron(
               i == result.length - 1 ? null : result[i + 1],
               new LogisticFunction());
         }
      }

      return result;
   }

   private void compareDoubles(double expected, double actual) {
      if (Math.abs(expected - actual) > 0.000001) {
         throw new RuntimeException(
            String.format("Expected '%f' to be '%f'", actual, expected));
      }
      assertTrue(Math.abs(expected - actual) < 0.000001);
   }

}
