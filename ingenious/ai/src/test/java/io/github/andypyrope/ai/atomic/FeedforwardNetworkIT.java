package io.github.andypyrope.ai.atomic;

import io.github.andypyrope.ai.activation.LeakyReLuFunction;
import io.github.andypyrope.ai.activation.LogisticFunction;
import io.github.andypyrope.ai.activation.ReLuFunction;
import io.github.andypyrope.ai.atomic.neurons.BackpropNeuronFactory;
import io.github.andypyrope.ai.atomic.neurons.RpropNeuronFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FeedforwardNetworkIT {

   private static final double[] INPUT = new double[]{0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7,
         0.8, 0.9, 1.0, 0.15, 0.25, 0.35, 0.45, 0.55};
   private static final double[] OUTPUT = new double[]{0.2, 0.4, 0.6, 0.8, 1.0};

   private static final int[] HIDDEN_SIZES = new int[]{10, 8};

   @Test
   void testWithBackprop() {
      final AtomicNetwork network = new FeedforwardNetwork(
            INPUT.length, HIDDEN_SIZES, OUTPUT.length,
            new BackpropNeuronFactory(new LogisticFunction(), 0.3));

      trainNetwork(network);
      Assertions.assertTrue(network.getEuclideanDistance(OUTPUT) < 0.0000000000000000001);
   }

   @Test
   void testWithBackpropWithReLu() {
      final AtomicNetwork network = new FeedforwardNetwork(
            INPUT.length, HIDDEN_SIZES, OUTPUT.length,
            new BackpropNeuronFactory(new ReLuFunction(), 0.3));

      trainNetwork(network);
      Assertions.assertTrue(network.getEuclideanDistance(OUTPUT) < 0.0000000002);
   }

   @Test
   void testWithBackpropWithLeakyReLu() {
      final AtomicNetwork network = new FeedforwardNetwork(
            INPUT.length, HIDDEN_SIZES, OUTPUT.length,
            new BackpropNeuronFactory(new LeakyReLuFunction(0.1), 0.3));

      trainNetwork(network);
      Assertions.assertTrue(network.getEuclideanDistance(OUTPUT) < 0.0000000002);
   }

   @Test
   void testWithRprop() {
      final AtomicNetwork network = new FeedforwardNetwork(
            INPUT.length, HIDDEN_SIZES, OUTPUT.length,
            new RpropNeuronFactory(new LogisticFunction()));

      trainNetwork(network);
      Assertions.assertTrue(network.getEuclideanDistance(OUTPUT) < 0.00004);
   }

   @Test
   void testWithRpropWithReLu() {
      final AtomicNetwork network = new FeedforwardNetwork(
            INPUT.length, HIDDEN_SIZES, OUTPUT.length,
            new RpropNeuronFactory(new LogisticFunction()));

      trainNetwork(network);
      Assertions.assertTrue(network.getEuclideanDistance(OUTPUT) < 0.00004);
   }

   private void trainNetwork(final AtomicNetwork network) {
      network.calculate(INPUT);

      for (int i = 0; i < 5; i++) {
         for (int j = 0; j < 10; j++) {
            network.adjust(OUTPUT);
            network.calculate(INPUT);
         }
      }
   }
}
