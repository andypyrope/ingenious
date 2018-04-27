package io.github.andypyrope.ai.feedforward;

import io.github.andypyrope.ai.activation.LogisticFunction;
import io.github.andypyrope.ai.feedforward.neurons.BackpropFeedforwardNeuronFactory;
import io.github.andypyrope.ai.feedforward.neurons.RpropFeedforwardNeuronFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StandardFeedforwardNeuralNetworkIT {

   private static final double[] INPUT = new double[]{0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7,
         0.8, 0.9, 1.0, 0.15, 0.25, 0.35, 0.45, 0.55};
   private static final double[] OUTPUT = new double[]{0.2, 0.4, 0.6, 0.8, 1.0};

   private static final int[] HIDDEN_SIZES = new int[]{10, 8};

   @Test
   void testWithBackprop() {
      final FeedforwardNeuralNetwork network = new StandardFeedforwardNeuralNetwork(
            INPUT.length, HIDDEN_SIZES, OUTPUT.length,
            new BackpropFeedforwardNeuronFactory(new LogisticFunction(), 0.3));

      validateNetworkDistances(network, new double[0]);
      Assertions.assertTrue(network.getEuclideanDistance(OUTPUT) < 0.0000000000000000001);
   }

   @Test
   void testWithRprop() {
      final FeedforwardNeuralNetwork network = new StandardFeedforwardNeuralNetwork(
            INPUT.length, HIDDEN_SIZES, OUTPUT.length,
            new RpropFeedforwardNeuronFactory(new LogisticFunction()));

      validateNetworkDistances(network, new double[0]);
      Assertions.assertTrue(network.getEuclideanDistance(OUTPUT) < 0.00004);
   }

   private void validateNetworkDistances(FeedforwardNeuralNetwork network,
         double[] distances) {

      network.calculate(INPUT);

      for (int i = 0; i < 5; i++) {
         for (int j = 0; j < 10; j++) {
            network.adjust(OUTPUT);
            network.calculate(INPUT);
         }
      }
   }
}
