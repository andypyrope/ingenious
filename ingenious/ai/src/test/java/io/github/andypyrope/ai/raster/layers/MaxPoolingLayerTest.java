package io.github.andypyrope.ai.raster.layers;

import io.github.andypyrope.ai.data.CustomRasterData;
import io.github.andypyrope.ai.data.RasterData;
import io.github.andypyrope.ai.testutil.DeterministicRandom;
import io.github.andypyrope.ai.testutil.TestUtil;
import io.github.andypyrope.ai.util.StandardVector;
import io.github.andypyrope.ai.util.Vector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

class MaxPoolingLayerTest {
   private static final int COUNT = 2;
   private static final Vector INPUT_SIZE = new StandardVector(3, 3, 3);
   private static final Vector WINDOW_SIZE = new StandardVector(1, 2, 3);
   private static final Vector OUTPUT_SIZE = new StandardVector(3, 2, 1);
   private static final RasterData[] INPUT = new RasterData[COUNT];
   private static final RasterData[] TARGET_OUTPUT = new RasterData[COUNT];

   private static Random _random;

   @BeforeAll
   static void setUpAll() {
      _random = new DeterministicRandom();
      for (int i = 0; i < COUNT; i++) {
         INPUT[i] = makeDummyData(INPUT_SIZE);
         TARGET_OUTPUT[i] = makeDummyData(OUTPUT_SIZE);
      }
   }

   private static RasterData makeDummyData(final Vector size) {
      final RasterData result = new CustomRasterData(size);
      result.forEach((x, y, z) -> result.setCell(x, y, z, _random.nextDouble()));
      return result;
   }

   @BeforeEach
   void setUp() {
      _random = new DeterministicRandom();
   }

   @Test
   void testGetCalculationComplexity() {
      Assertions.assertEquals(72, makeLayer().getCalculationComplexity());
   }

   @Test
   void testGetAdjustmentComplexity() {
      Assertions.assertEquals(39, makeLayer().getAdjustmentComplexity());
   }

   @Test
   void testCalculation() {
      final RasterLayer layer = makeLayer();
      layer.calculate(INPUT);
      final RasterData[] output = layer.getOutputAsRaster();
      for (int i = 0; i < COUNT; i++) {
         final RasterData currentInput = INPUT[i];
         final RasterData currentOutput = output[i];
         currentOutput.verifyDimensions(OUTPUT_SIZE);
         currentOutput.forEach((yX, yY, yZ) -> {
            final double outputValue = currentOutput.getCell(yX, yY, yZ);
            WINDOW_SIZE.forEach((wX, wY, wZ) -> {
               final double inputValue = currentInput.getCell(yX + wX, yY + wY, yZ + wZ);
               Assertions.assertTrue(
                     inputValue < outputValue + 0.00000000000001,
                     String.format("The input value at (%d,%d,%d) - %.2f - should be " +
                                 "at most the output value at (%d,%d,%d) - %.2f",
                           yX + wX, yY + wY, yZ + wZ, inputValue,
                           yX, yY, yZ, outputValue));
            });
         });
      }
   }

   @Test
   void testLearning() {
      final RasterLayer layer = makeLayer();
      layer.calculate(INPUT);
      final double distance = 1.484;
      TestUtil.compareDoublesLoose(distance,
            TestUtil.getEuclideanDistance(layer, TARGET_OUTPUT));

      train(layer);
      TestUtil.compareDoublesLoose(distance,
            TestUtil.getEuclideanDistance(layer, TARGET_OUTPUT));
   }

   private void train(final RasterLayer layer) {
      for (int i = 0; i < 5; i++) {
         layer.adjust(TARGET_OUTPUT);
         layer.calculate(INPUT);
      }
   }

   private RasterLayer makeLayer() {
      return new MaxPoolingLayer(COUNT, INPUT_SIZE, WINDOW_SIZE, null, null);
   }
}