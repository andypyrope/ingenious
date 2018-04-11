package io.github.andypyrope.fitness.ai;

import io.github.andypyrope.fitness.ai.activation.LogisticFunction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FeedforwardNeuralNetworkTest {

   @Test
   void testCalculation() {
      final FeedforwardNeuralNetwork network = new FeedforwardNeuralNetwork(
         5,
         new int[] { 5, 1 },
         2,
         new LogisticFunction());

      assertEquals(25 + 5 + 2, network.getEdgeCount());

      final double[] input = new double[] { 0.12, 0.23, 0.64, 0.23, 0.1 };
      final double[] output = new double[] { 0.96, 0.11 };

      assertEquals(Math.sqrt(output[0] * output[0] + output[1] * output[1]),
            network.getEuclideanDistance(output));

      network.calculate(input);
      final double initialError = network.getEuclideanDistance(output);

      for (int i = 0; i < 100; i++) {
         network.adjust(output);
         network.calculate(input);
      }

      assertTrue(initialError > network.getEuclideanDistance(output));

      final double allowedError = .005;
      assertTrue(allowedError > network.getEuclideanDistance(output));

      final double[] actualOutput = network.getOutput();
      // 0.95566 and 0.10999
      assertArrayEquals(new int[] { 96002, 10998 },
         new int[] { (int) (actualOutput[0] * 100000),
            (int) (actualOutput[1] * 100000) });
   }

   @Test
   void testSimple() {
      final FeedforwardNeuralNetwork network = new FeedforwardNeuralNetwork(
         1,
         new int[] { 1 },
         1,
         new LogisticFunction());

      assertEquals(1 + 1, network.getEdgeCount());

      final double[] input = new double[] { 1.0 };
      final double[] output = new double[] { 0.0 };

      assertEquals(0.0, network.getEuclideanDistance(output));

      network.calculate(input);

      for (int i = 0; i < 100; i++) {
         network.adjust(output);
         network.calculate(input);
      }
      final double allowedError = .09;
      assertTrue(allowedError > network.getEuclideanDistance(output));
   }
}
