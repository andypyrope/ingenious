package io.github.andypyrope.ai.raster.layers;

import io.github.andypyrope.ai.data.CustomRasterData;
import io.github.andypyrope.ai.data.RasterData;
import io.github.andypyrope.ai.raster.RasterLayer;
import io.github.andypyrope.ai.testutil.DeterministicRandom;
import io.github.andypyrope.ai.testutil.HalfFunction;
import io.github.andypyrope.ai.testutil.TestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Random;

class RasterActivationLayerTest {
   private static final int COUNT = 2;
   private static final int WIDTH = 2;
   private static final int HEIGHT = 2;
   private static final int DEPTH = 2;
   private static final RasterData[] INPUT = new RasterData[COUNT];
   private static final RasterData[] TARGET_OUTPUT = new RasterData[COUNT];

   private static final Random STATIC_RANDOM = new DeterministicRandom();

   @BeforeAll
   static void setUpAll() {
      for (int i = 0; i < COUNT; i++) {
         INPUT[i] = makeDummyData();
         TARGET_OUTPUT[i] = makeDummyData();
      }
   }

   private static RasterData makeDummyData() {
      final RasterData result = new CustomRasterData(WIDTH, HEIGHT, DEPTH);
      for (int x = 0; x < WIDTH; x++) {
         for (int y = 0; y < HEIGHT; y++) {
            for (int z = 0; z < DEPTH; z++) {
               result.setCell(x, y, z, STATIC_RANDOM.nextDouble());
            }
         }
      }
      return result;
   }

   @Test
   void testGetCalculationComplexity() {
      Assertions.assertEquals(16, makeLayer().getCalculationComplexity());
   }

   @Test
   void testGetAdjustmentComplexity() {
      Assertions.assertEquals(16, makeLayer().getAdjustmentComplexity());
   }

   @Test
   void testCalculation() {
      final RasterLayer layer = makeLayer();
      layer.calculate(INPUT);
      final RasterData[] output = layer.getOutputAsRaster();
      for (int i = 0; i < COUNT; i++) {
         final RasterData currentInput = INPUT[i];
         final RasterData currentOutput = output[i];
         currentOutput.verifyDimensions(currentInput.getWidth(), currentInput.getHeight(),
               currentInput.getDepth());

         currentInput.forEach((x, y, z) -> TestUtil.compareDoubles(
               currentInput.getCell(x, y, z) / 2.0,
               currentOutput.getCell(x, y, z)));
      }
   }

   @Test
   void testLearning() {
      final RasterLayer layer = makeLayer();
      layer.calculate(INPUT);
      TestUtil.compareDoubles(1.81, TestUtil.getEuclideanDistance(layer, TARGET_OUTPUT));
      train(layer);
      TestUtil.compareDoubles(1.81, TestUtil.getEuclideanDistance(layer, TARGET_OUTPUT));
   }

   private void train(final RasterLayer layer) {
      for (int i = 0; i < 5; i++) {
         layer.adjust(TARGET_OUTPUT);
         layer.calculate(INPUT);
      }
   }

   private RasterLayer makeLayer() {
      return new RasterActivationLayer(COUNT, WIDTH, HEIGHT, DEPTH, new HalfFunction());
   }
}