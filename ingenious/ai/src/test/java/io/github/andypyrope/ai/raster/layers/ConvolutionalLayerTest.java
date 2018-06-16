package io.github.andypyrope.ai.raster.layers;

import io.github.andypyrope.ai.data.CustomRasterData;
import io.github.andypyrope.ai.data.RasterData;
import io.github.andypyrope.ai.raster.RasterLayer;
import io.github.andypyrope.ai.testutil.DeterministicRandom;
import io.github.andypyrope.ai.testutil.TestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Random;

class ConvolutionalLayerTest {

   private static final int INPUT_COUNT = 3;
   private static final int INPUT_WIDTH = 2;
   private static final int INPUT_HEIGHT = 3;
   private static final int INPUT_DEPTH = 3;
   private static final RasterData[] INPUT = new RasterData[INPUT_COUNT];

   private static final int FILTER_COUNT = 2;
   private static final int FILTER_WIDTH = 1;
   private static final int FILTER_HEIGHT = 2;
   private static final int FILTER_DEPTH = 3;

   private static final int OUTPUT_COUNT = 6;
   private static final int OUTPUT_WIDTH = 2;
   private static final int OUTPUT_HEIGHT = 2;
   private static final int OUTPUT_DEPTH = 1;
   private static final RasterData[] TARGET_OUTPUT = new RasterData[OUTPUT_COUNT];

   private static final Random STATIC_RANDOM = new DeterministicRandom();

   @BeforeAll
   static void setUpAll() {
      for (int i = 0; i < INPUT_COUNT; i++) {
         INPUT[i] = makeDummyData(INPUT_WIDTH, INPUT_HEIGHT, INPUT_DEPTH);
      }

      for (int i = 0; i < OUTPUT_COUNT; i++) {
         TARGET_OUTPUT[i] = makeDummyData(OUTPUT_WIDTH, OUTPUT_HEIGHT, OUTPUT_DEPTH);
      }
   }

   private static RasterData makeDummyData(final int width, final int height,
         final int depth) {

      final RasterData result = new CustomRasterData(width, height, depth);
      for (int x = 0; x < width; x++) {
         for (int y = 0; y < height; y++) {
            for (int z = 0; z < depth; z++) {
               result.setCell(x, y, z, STATIC_RANDOM.nextDouble());
            }
         }
      }
      return result;
   }

   @Test
   void testGetCalculationComplexity() {
      Assertions.assertEquals(144, makeLayer().getCalculationComplexity());
   }

   @Test
   void testGetAdjustmentComplexity() {
      Assertions.assertEquals(2 * 144, makeLayer().getAdjustmentComplexity());
   }

   @Test
   void testLearning() {
      final RasterLayer layer = makeLayer();
      layer.calculate(INPUT);
      TestUtil.compareDoubles(1.81, TestUtil.getEuclideanDistance(layer, TARGET_OUTPUT));
      train(layer);
      TestUtil.compareDoubles(1.66, TestUtil.getEuclideanDistance(layer, TARGET_OUTPUT));
      train(layer);
      TestUtil.compareDoubles(1.66, TestUtil.getEuclideanDistance(layer, TARGET_OUTPUT));
   }

   private void train(final RasterLayer layer) {
      for (int i = 0; i < 5; i++) {
         layer.adjust(TARGET_OUTPUT);
         layer.calculate(INPUT);
      }
   }

   private RasterLayer makeLayer() {
      return new ConvolutionalLayer(
            INPUT_COUNT, INPUT_WIDTH, INPUT_HEIGHT, INPUT_DEPTH,
            FILTER_COUNT, FILTER_WIDTH, FILTER_HEIGHT, FILTER_DEPTH,
            new DeterministicRandom());
   }
}