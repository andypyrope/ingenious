package io.github.andypyrope.ai.atomic;

import io.github.andypyrope.ai.activation.LeakyReLuFunction;
import io.github.andypyrope.ai.activation.LogisticFunction;
import io.github.andypyrope.ai.activation.ReLuFunction;
import io.github.andypyrope.ai.atomic.neurons.BackpropNeuronFactory;
import io.github.andypyrope.ai.atomic.neurons.RpropNeuronFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FeedforwardNetworkIT {

   private static final double DISTANCE_VERIFICATION_MULTIPLIER = 1.1;

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
      verifyDistance(network, 0.00000000000000016);
   }

   @Test
   void testWithBackpropWithLeakyReLu() {
      final AtomicNetwork network = new FeedforwardNetwork(
            INPUT.length, HIDDEN_SIZES, OUTPUT.length,
            new BackpropNeuronFactory(new LeakyReLuFunction(0.1), 0.3));

      trainNetwork(network);
      verifyDistance(network, 0.00000000000036);
   }

   @Test
   void testWithBackpropWithReLu() {
      final AtomicNetwork network = new FeedforwardNetwork(
            INPUT.length, HIDDEN_SIZES, OUTPUT.length,
            new BackpropNeuronFactory(new ReLuFunction(), 0.3));

      trainNetwork(network);
      verifyDistance(network, 0.00000000000023);
   }

   @Test
   void testWithRprop() {
      final AtomicNetwork network = new FeedforwardNetwork(
            INPUT.length, HIDDEN_SIZES, OUTPUT.length,
            new RpropNeuronFactory(new LogisticFunction()));

      trainNetwork(network);
      verifyDistance(network, 0.0000000012);
   }

   @Test
   void testWithRpropWithReLu() {
      final AtomicNetwork network = new FeedforwardNetwork(
            INPUT.length, HIDDEN_SIZES, OUTPUT.length,
            new RpropNeuronFactory(new LogisticFunction()));

      trainNetwork(network);
      verifyDistance(network, 0.0000000012);
   }

   private void trainNetwork(final AtomicNetwork network) {
      network.calculate(INPUT);

      for (int i = 0; i < 10; i++) {
         for (int j = 0; j < 10; j++) {
            network.adjust(OUTPUT);
            network.calculate(INPUT);
         }
      }
   }

   private void verifyDistance(final AtomicNetwork network, final double distance) {
      verifyDistance(network, distance * DISTANCE_VERIFICATION_MULTIPLIER,
            distance / DISTANCE_VERIFICATION_MULTIPLIER);
   }

   private void verifyDistance(final AtomicNetwork network, final double maxDistance,
         final double minDistance) {

      final double actualDistance = network.getEuclideanDistance(OUTPUT);

      Assertions.assertTrue(actualDistance < maxDistance,
            String.format("The distance - %.28f - is less than %.28f", actualDistance,
                  maxDistance));

      Assertions.assertTrue(actualDistance > minDistance - Double.MIN_NORMAL,
            String.format("The distance - %.28f - is at least %.28f", actualDistance,
                  minDistance));
   }
}
