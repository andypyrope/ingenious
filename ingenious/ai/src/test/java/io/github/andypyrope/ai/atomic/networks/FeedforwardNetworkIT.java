package io.github.andypyrope.ai.atomic.networks;

import io.github.andypyrope.ai.activation.ActivationFunction;
import io.github.andypyrope.ai.activation.LeakyReLuFunction;
import io.github.andypyrope.ai.activation.LogisticFunction;
import io.github.andypyrope.ai.activation.TanhFunction;
import io.github.andypyrope.ai.atomic.AtomicNetwork;
import io.github.andypyrope.ai.testutil.DeterministicRandom;
import io.github.andypyrope.ai.testutil.TestUtil;
import org.junit.jupiter.api.Test;

class FeedforwardNetworkIT {

   private static final double[] INPUT = new double[]{0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7,
         0.8, 0.9, 1.0, 0.15, 0.25, 0.35, 0.45, 0.55};
   private static final double[] OUTPUT = new double[]{0.2, 0.4, 0.6, 0.8, 1.0};

   private static final int[] HIDDEN_SIZES = new int[]{10, 8};

   @Test
   void testWithLogistic() {
      TestUtil.compareDoubles(0.00000000028, getDistance(new LogisticFunction()));
   }

   @Test
   void testWithLeakyReLu() {
      TestUtil.compareDoubles(0.0000000012, getDistance(new LeakyReLuFunction(0.1)));
   }

   @Test
   void testWithTanh() {
      TestUtil.compareDoubles(0.00000000096, getDistance(new TanhFunction()));
   }

   private double getDistance(final ActivationFunction function) {
      final AtomicNetwork network = new FeedforwardNetwork(INPUT.length, HIDDEN_SIZES,
            OUTPUT.length, function, new DeterministicRandom());

      trainNetwork(network);
      return network.getEuclideanDistance(OUTPUT);
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
}
