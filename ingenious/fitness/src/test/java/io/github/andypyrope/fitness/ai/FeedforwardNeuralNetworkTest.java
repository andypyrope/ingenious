package io.github.andypyrope.fitness.ai;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.github.andypyrope.fitness.ai.activation.SigmoidFunction;

class FeedforwardNeuralNetworkTest {

   @Test
   void testCalculation() {
      final FeedforwardNeuralNetwork network = new FeedforwardNeuralNetwork(
         5,
         new int[] { 3, 10, 1 },
         2,
         new SigmoidFunction(),
         0.06);

      final double[] input = new double[] { 0.12, 0.23, 0.64, 0.23, 0.1 };
      final double[] output = new double[] { 0.96, 0.11 };

      // The output nodes have an output value of 0.0 in the beginning
      assertEquals(0.46685, network.getError(output));

      network.calculate(input);
      final double initialError = network.getError(output);

      for (int i = 0; i < 10; i++) {
         network.backpropagate(output);
         network.calculate(input);
      }

      assertTrue(initialError > network.getError(output));
   }

   @Test
   void testSimple() {
      final FeedforwardNeuralNetwork network = new FeedforwardNeuralNetwork(
         1,
         new int[] {},
         1,
         new SigmoidFunction(),
         0.8);

      final double[] input = new double[] { 1.0 };
      final double[] output = new double[] { 0.0 };

      // The output nodes have an output value of 0.0 in the beginning
      assertEquals(0.0, network.getError(output));

      network.calculate(input);

      for (int i = 0; i < 100; i++) {
         network.backpropagate(output);
         network.calculate(input);
      }
   }

}
