package io.github.andypyrope.ai.atomic.layers;

import io.github.andypyrope.ai.atomic.AtomicLayer;
import io.github.andypyrope.ai.testutil.DeterministicRandom;
import io.github.andypyrope.ai.testutil.TestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BiasLayerTest {

   private static final int LAYER_SIZE = 3;
   private static final double[] INPUT = new double[]{1, 2, 3};
   private static final double[] ACTUAL_OUTPUT = new double[]{0.6, 1.8, 3.2};
   private static final double[] TARGET_OUTPUT = new double[]{2, -3, 4};

   @Test
   void testGetCalculationComplexity() {
      Assertions.assertEquals(LAYER_SIZE, makeLayer().getCalculationComplexity());
   }

   @Test
   void testGetAdjustmentComplexity() {
      Assertions.assertEquals(LAYER_SIZE, makeLayer().getAdjustmentComplexity());
   }

   @Test
   void testCalculation() {
      final AtomicLayer layer = makeLayer();
      layer.calculate(INPUT);
      TestUtil.compareDoubleArrays(ACTUAL_OUTPUT, layer.getOutputAsAtomic());
   }

   @Test
   void testLearning() {
      final AtomicLayer layer = makeLayer();
      layer.calculate(INPUT);
      TestUtil.compareDoubles(5.05, layer.getEuclideanDistance(TARGET_OUTPUT));
      train(layer);
      TestUtil.compareDoubles(4.45, layer.getEuclideanDistance(TARGET_OUTPUT));
      TestUtil.compareDoubleArrays(new double[]{-1.06, 4.46, -0.47},
            layer.getInputGradientAsAtomic());
      train(layer);
      TestUtil.compareDoubles(3.44, layer.getEuclideanDistance(TARGET_OUTPUT));
      TestUtil.compareDoubleArrays(new double[]{-0.26, 3.66, 0.046},
            layer.getInputGradientAsAtomic());
   }

   private void train(final AtomicLayer layer) {
      for (int i = 0; i < 5; i++) {
         layer.adjust(TARGET_OUTPUT);
         layer.calculate(INPUT);
      }
   }

   private AtomicLayer makeLayer() {
      return new BiasLayer(LAYER_SIZE, new DeterministicRandom());
   }
}