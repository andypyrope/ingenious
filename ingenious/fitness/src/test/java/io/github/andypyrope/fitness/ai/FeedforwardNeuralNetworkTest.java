package io.github.andypyrope.fitness.ai;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.github.andypyrope.fitness.ai.activation.LogisticFunction;

class FeedforwardNeuralNetworkTest {

   @Test
   void testCalculation() {
      final FeedforwardNeuralNetwork network = new FeedforwardNeuralNetwork(
         5,
         new int[] { 5, 1 },
         2,
         new LogisticFunction(),
         10.0);

      final double[] input = new double[] { 0.12, 0.23, 0.64, 0.23, 0.1 };
      final double[] output = new double[] { 0.96, 0.11 };

      assertEquals(Math.sqrt(output[0] * output[0] + output[1] * output[1]),
         network.getEucliedanDistance(output));

      network.calculate(input);
      final double initialError = network.getEucliedanDistance(output);

      for (int i = 0; i < 100; i++) {
         network.adjust(output);
         network.calculate(input);
      }

      assertTrue(initialError > network.getEucliedanDistance(output));

      final double allowedError = .005;
      assertTrue(allowedError > network.getEucliedanDistance(output));
      assertArrayEquals(
         new double[] { 0.9556649180059656, 0.10999176239284271 },
         network.getOutput());
   }

   @Test
   void testSimple() {
      final FeedforwardNeuralNetwork network = new FeedforwardNeuralNetwork(
         1,
         new int[] { 1 },
         1,
         new LogisticFunction(),
         1.8);

      final double[] input = new double[] { 1.0 };
      final double[] output = new double[] { 0.0 };

      assertEquals(0.0, network.getEucliedanDistance(output));

      network.calculate(input);

      for (int i = 0; i < 100; i++) {
         network.adjust(output);
         network.calculate(input);
      }
      final double allowedError = .09;
      assertTrue(allowedError > network.getEucliedanDistance(output));
   }
}
