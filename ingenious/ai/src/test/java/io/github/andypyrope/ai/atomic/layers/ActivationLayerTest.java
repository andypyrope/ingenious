package io.github.andypyrope.ai.atomic.layers;

import io.github.andypyrope.ai.activation.ActivationFunction;
import io.github.andypyrope.ai.atomic.AtomicLayer;
import io.github.andypyrope.ai.testutil.TestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ActivationLayerTest {

   private static final int LAYER_SIZE = 3;
   private static final double[] INPUT = new double[]{1, 2, 3};
   private static final double[] ACTUAL_OUTPUT = new double[]{0.5, 1.0, 1.5};
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

      // NOTE: This layer does not adjust itself, it only calculates its input gradient
      final double distance = 5.20;
      TestUtil.compareDoubles(distance, layer.getEuclideanDistance(TARGET_OUTPUT));
      train(layer);
      TestUtil.compareDoubles(distance, layer.getEuclideanDistance(TARGET_OUTPUT));
      train(layer);
      TestUtil.compareDoubles(distance, layer.getEuclideanDistance(TARGET_OUTPUT));
      TestUtil.compareDoubleArrays(new double[]{-0.75, 2.0, -1.25},
            layer.getInputGradientAsAtomic());
   }

   private void train(final AtomicLayer layer) {
      for (int i = 0; i < 5; i++) {
         layer.adjust(TARGET_OUTPUT);
         layer.calculate(INPUT);
      }
   }

   private AtomicLayer makeLayer() {
      return new ActivationLayer(LAYER_SIZE, new HalfFunction());
   }

   private static class HalfFunction implements ActivationFunction {
      @Override
      public double getOutput(final double input) {
         return input * 0.5;
      }

      @Override
      public double getSlope(final double input, final double output) {
         return 0.5;
      }
   }
}