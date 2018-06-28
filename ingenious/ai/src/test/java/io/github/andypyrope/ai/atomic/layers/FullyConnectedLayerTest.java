package io.github.andypyrope.ai.atomic.layers;

import io.github.andypyrope.ai.testutil.DeterministicRandom;
import io.github.andypyrope.ai.testutil.TestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FullyConnectedLayerTest {

   private static final int INPUT_COUNT = 3;
   private static final int OUTPUT_COUNT = 2;
   private static final double[] INPUT = new double[]{1, 2, 3};
   private static final double[] TARGET_OUTPUT = new double[]{2, -3};

   @Test
   void testGetCalculationComplexity() {
      Assertions.assertEquals(INPUT_COUNT * OUTPUT_COUNT,
            makeLayer().getCalculationComplexity());
   }

   @Test
   void testGetAdjustmentComplexity() {
      Assertions.assertEquals(INPUT_COUNT * OUTPUT_COUNT,
            makeLayer().getAdjustmentComplexity());
   }

   @Test
   void testLearning() {
      final AtomicLayer layer = makeLayer();
      layer.calculate(INPUT);
      TestUtil.compareDoublesLoose(4.87, layer.getEuclideanDistance(TARGET_OUTPUT));
      train(layer);
      TestUtil.compareDoublesLoose(2.10, layer.getEuclideanDistance(TARGET_OUTPUT));
      train(layer);
      TestUtil.compareDoublesLoose(0.24, layer.getEuclideanDistance(TARGET_OUTPUT));
   }

   private void train(final AtomicLayer layer) {
      for (int i = 0; i < 5; i++) {
         layer.adjust(TARGET_OUTPUT);
         layer.calculate(INPUT);
      }
   }

   private AtomicLayer makeLayer() {
      return new FullyConnectedLayer(INPUT_COUNT, OUTPUT_COUNT,
            new DeterministicRandom());
   }
}