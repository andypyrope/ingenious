package io.github.andypyrope.fitness.ai;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import io.github.andypyrope.fitness.ai.activation.LogisticFunction;

class BackpropFeedforwardNeuronTest {

   @Test
   public void testCalculateGradientAndAdjust() {
      final BackpropFeedforwardNeuron[][] neurons = makeNeurons(
         new int[] { 1, 2, 3, 2, 1 });

      neurons[0][0].setOutput(0.24);
      neurons[0][0].propagate();

      for (int i = 1; i < neurons.length; i++) {
         for (BackpropFeedforwardNeuron neuron : neurons[i]) {
            neuron.updateOutput();
            neuron.propagate();
         }
      }
      compareDoubles(0.859519, neurons[neurons.length - 1][0].getOutput());

      for (FeedforwardNeuron neuron : neurons[neurons.length - 1]) {
         neuron.calculateGradient(0.3);
      }

      for (int i = neurons.length - 2; i >= 1; i--) {
         for (BackpropFeedforwardNeuron neuron : neurons[i]) {
            neuron.calculateGradient();
            neuron.adjust();
         }
      }

      for (BackpropFeedforwardNeuron neuron : neurons[0]) {
         neuron.adjust();
      }

      for (int i = 1; i < neurons.length; i++) {
         for (BackpropFeedforwardNeuron neuron : neurons[i]) {
            neuron.resetNetInput();
         }
      }

      neurons[0][0].propagate();

      for (int i = 1; i < neurons.length; i++) {
         for (BackpropFeedforwardNeuron neuron : neurons[i]) {
            neuron.updateOutput();
            neuron.propagate();
         }
      }
      compareDoubles(0.854832, neurons[neurons.length - 1][0].getOutput());
   }

   @Test
   public void testResetInput() {
      final BackpropFeedforwardNeuron[][] neurons = makeNeurons(
         new int[] { 1, 1 });

      neurons[0][0].setOutput(0.5);
      neurons[0][0].propagate();
      compareDoubles(0.0, neurons[1][0].getOutput());

      neurons[1][0].updateOutput();
      compareDoubles(0.622459, neurons[1][0].getOutput());

      neurons[1][0].updateOutput();
      compareDoubles(0.622459, neurons[1][0].getOutput());

      neurons[1][0].resetNetInput();
      neurons[1][0].updateOutput();
      compareDoubles(0.5, neurons[1][0].getOutput());
   }

   private BackpropFeedforwardNeuron[][] makeNeurons(int[] count) {
      final BackpropFeedforwardNeuron[][] result = new BackpropFeedforwardNeuron[count.length][];

      for (int i = result.length - 1; i >= 0; i--) {
         result[i] = new BackpropFeedforwardNeuron[count[i]];
         for (int j = 0; j < count[i]; j++) {
            result[i][j] = new BackpropFeedforwardNeuron(
               i == result.length - 1 ? null : result[i + 1],
               new LogisticFunction(),
               0.34);
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
